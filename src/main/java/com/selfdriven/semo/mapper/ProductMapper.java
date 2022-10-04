package com.selfdriven.semo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.selfdriven.semo.dto.Product;

@Mapper
@Repository
public interface ProductMapper {
    int addProduct(Product product);

    List<Product> getProductList();
    int deleteProduct(String productId);


}
