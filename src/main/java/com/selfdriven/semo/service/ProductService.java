package com.selfdriven.semo.service;

import com.selfdriven.semo.entity.Product;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import com.selfdriven.semo.repository.ProductImageMapper;
import com.selfdriven.semo.repository.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
//@Transactional -> transactional 은 각 메서드에 걸어사 사용
public class ProductService {
	private final ProductMapper productMapper;
	private final ProductImageMapper productImageMapper;

	public int addProduct(Product product) {
		int result = productMapper.addProduct(product);
		if(result != 1){
			throw new ApiException(ResultCode.INVALID_PARAMETER);
		}
		return result;
	}

	public  Map<String, Object> getProductById(int productId) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("productInfo", productMapper.getProductById(productId));
		result.put("imageList", productImageMapper.getProductImagesById(productId));
		return result;
	}

	// TODO: 이미지 포함해서 가져올수 있도록 수정 필요?
	public List<Product> getProductList() {
		return productMapper.getProductList();
	}

	public int updateProduct(Product product) {
		int result = productMapper.updateProduct(product);
		if(result != 1) {
			throw new ApiException(ResultCode.INVALID_PARAMETER);
		}
		return result;
	}

	public int deleteProduct(int productId, Login login){
		int result = productMapper.deleteProduct(productId, login.getId());
		if(result != 1){
			throw new ApiException(ResultCode.INVALID_PARAMETER);
		}
		return result;
	}

	public  Boolean checkProduct(int productId, String memberId) {
		return productMapper.getProductByMemberId(productId, memberId) != null ? true : false;
	}

}
