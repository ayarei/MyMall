package com.ltr.mymall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ltr.mymall.dto.CreateOrderExecution;
import com.ltr.mymall.exception.ConcurrentChange;
import com.ltr.mymall.exception.OutOfStockException;
import com.ltr.mymall.mapper.OrderMapper;
import com.ltr.mymall.pojo.Order;
import com.ltr.mymall.pojo.OrderExample;
import com.ltr.mymall.pojo.OrderItem;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.User;
import com.ltr.mymall.service.OrderItemService;
import com.ltr.mymall.service.OrderService;
import com.ltr.mymall.service.ProductService;
import com.ltr.mymall.service.UserService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderMapper orderMapper;	
	@Autowired
	UserService userService;	
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ProductService productService;
	
	@Override
	public void add(Order c) {
		orderMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		orderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Order c) {
		orderMapper.updateByPrimaryKeySelective(c);
	}

	@Override
	public Order get(int id) {	
		return orderMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Order> list() {
		OrderExample example = new OrderExample();
		example.setOrderByClause("id desc");
		List<Order> result = orderMapper.selectByExample(example);
		setUser(result);
		return result;
	}
	
	/**
	 *  由于订单数据不是真的被删除
	 *  只是被定义为删除状态（delete status）
	 *  所以在查询用户订单时排除掉delete状态的订单
	 *  .andStatusNotEqualTo("delete")
	 */
	@Override
	public List<Order> list(int uid, String excludedStatus) {
		OrderExample example = new OrderExample();
		example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
		example.setOrderByClause("id desc");
		return orderMapper.selectByExample(example);
	}

	public void setUser(List<Order> os) {
		for (Order o : os)
			setUser(o);
	}
    public void setUser(Order o){
        int uid = o.getUid();
        User u = userService.get(uid);
        o.setUser(u);
    }

    /**
     * 将商品ois加入订单c，使用声明式事务
     * 没有库存、意外失败则回滚
     * 多线程下使用乐观锁更新库存
     * 
     * 返回-1则代表数据更新前被篡改
     * 返回-2代表商品售罄
     */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
	public CreateOrderExecution add(Order c, List<OrderItem> ois) throws RuntimeException {
		// 商品总价
		float total = 0;
		// 售罄商品ID表
		List<Integer> noStockID = new ArrayList<Integer>(); 
		try {
			// 添加订单
			add(c);
			// TODO:循环执行SQL，待改进
			for (OrderItem e : ois) {
				e.setOid(c.getId());
				// 更新库存成功则 > 0
				int stockUpdateCnt = 0;

				// 乐观锁实现多线程商品库存表更新
				Product nowProduct = productService.normalGet(e.getPid());
				if (nowProduct.getStock() > 0 && e.getNumber() <= nowProduct.getStock()) {
					stockUpdateCnt = productService.updateStock(nowProduct, e.getNumber());
					if (stockUpdateCnt > 0) {
						orderItemService.update(e);
						total += nowProduct.getPromotePrice() * e.getNumber();
					}else {
						throw new ConcurrentChange("多线程数据被篡改，需要重试");
					}
				}
				// 将售罄商品ID加入noStockID
				else {
					noStockID.add(nowProduct.getId());				
				}
			}
			// noStockID不为空则抛出商品售罄异常
			if(noStockID.size() != 0) {
				throw new OutOfStockException("商品售罄"); 
			}
			return new CreateOrderExecution(total, noStockID); // 订单正常，返回总价，noStockID.size() == 0
			
		} catch (OutOfStockException e) {
			// 由于捕获了异常不能自动回滚，所以这里手动回滚，下面也一样
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return new CreateOrderExecution(-2, noStockID); // 订单包含售罄商品
			
		} catch(ConcurrentChange e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return new CreateOrderExecution(-1, noStockID); // 并发购买冲突，在ForeController中重新提交事务
		}
	}

	
}
