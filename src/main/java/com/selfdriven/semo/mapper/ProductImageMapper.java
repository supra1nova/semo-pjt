package com.selfdriven.semo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.selfdriven.semo.dto.ImageProduct;

@Mapper
@Repository
public interface ProductImageMapper {
	 int insertProductImage(ImageProduct image);


}
