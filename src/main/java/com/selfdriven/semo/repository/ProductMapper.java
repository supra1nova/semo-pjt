package com.selfdriven.semo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.selfdriven.semo.dto.Product;

@Mapper
@Repository
public interface ProductMapper {
    int addProduct(Product product);
    Product getProductById(String productId);
    Product getProductByMemberId(@Param("productId") String productId, @Param("memberId") String memberId);
    List<Product> getProductList();
    int deleteProduct(String productId);
    int updateProduct(Product product);
    int getProductValidation(Product product);
}
