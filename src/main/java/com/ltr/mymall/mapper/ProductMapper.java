package com.ltr.mymall.mapper;

import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.ProductExample;
import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    List<Product> selectByExample(ProductExample example);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    
    int concurrentUpdateStock(@Param("product")Product record,@Param("buyNumber")int buyNumber);
    
    Product normalGet(@Param("id")Integer id);
}