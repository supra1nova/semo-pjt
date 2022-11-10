package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductImageMapper {
	
	Integer insertProductImage(ProductImage image);
	List<ProductImage> getProductImagesById(int productId);
	Integer deleteProductImage(String fileName);
	Integer countValidRoomImage(ProductImage productImage);
	List<String> getAllImageUris(int productId);
}
