package com.selfdriven.semo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.selfdriven.semo.dto.ImageProduct;

@Mapper
@Repository
public interface ProductImageMapper {
	
	 int insertProductImage(ImageProduct image);
	 List<ImageProduct> getProductImageById(String productId);
	 int deleteProductImage(String imageUrl);

}
