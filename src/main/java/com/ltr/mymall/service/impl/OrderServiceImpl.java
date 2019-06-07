package com.ltr.mymall.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
     * 返回-1则代表数据更新前被篡改
     */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
	public float add(Order c, List<OrderItem> ois) throws OutOfStockException {
		float total = 0;
		try {
			// 添加订单
			add(c);
			// TODO:循环执行SQL，待改进
			for (OrderItem e : ois) {
				e.setOid(c.getId());
				int stockUpdateCnt = 0;

				// 乐观锁实现多线程商品库存表更新
				Product nowProduct = productService.normalGet(e.getPid());
				if (nowProduct.getStock() > 0 && e.getNumber() <= nowProduct.getStock()) {
					stockUpdateCnt = productService.updateStock(nowProduct, e.getNumber());
					if (stockUpdateCnt > 0) {
						orderItemService.update(e);
						total += nowProduct.getPromotePrice() * e.getNumber();
					}else {
						return -1;
					}
				} else {
					throw new OutOfStockException(nowProduct.getName() + "被抢购一空了！看看别的商品吧~");
				}
			}
			return total;
		} catch (OutOfStockException e) {
			throw e;
		}
	}
}
