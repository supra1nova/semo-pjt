package com.selfdriven.semo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.selfdriven.semo.entity.Product;

@Mapper
@Repository
public interface ProductMapper {
    Integer addProduct(Product product);
    Product getProductById(int productId);
    List<Product> getProductList();
    Integer updateProduct(Product product);
    Integer deleteProduct(int productId);
    Product getProductByMemberId(@Param("productId") String productId, @Param("memberId") String memberId);
    Integer deleteAllProduct();
}
