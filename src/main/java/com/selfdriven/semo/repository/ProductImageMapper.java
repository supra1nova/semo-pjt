package com.selfdriven.semo.repository;

import java.util.List;

import com.selfdriven.semo.dto.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.selfdriven.semo.dto.ProductImage;

@Mapper
@Repository
public interface ProductImageMapper {
	
	int insertProductImage(ProductImage image);
	List<ProductImage> getProductImageById(String productId);
//	int deleteProductImage(String imageUrl);
	int deleteProductImage(String fileName);
	int getProductImageValidation(ProductImage productImage);
	List<String> getAllImageUris(int productId);
}
