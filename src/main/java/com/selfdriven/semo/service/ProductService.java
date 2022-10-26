package com.selfdriven.semo.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.selfdriven.semo.dto.ImageProduct;
import com.selfdriven.semo.dto.Member;
import com.selfdriven.semo.dto.Product;
import com.selfdriven.semo.mapper.ProductImageMapper;
import com.selfdriven.semo.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
	 private final ProductMapper productMapper;
	 private final ProductImageMapper productImageMapper;
	 

	    public int addProduct(Product product) {
	        return productMapper.addProduct(product);
	    }

	    public List<Product> getProductList() {
	        return productMapper.getProductList();
	    }
	    
	    public  Map<String, Object> getProductById(String productId) {
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			result.put("productInfo", productMapper.getProductById(productId));
			List<ImageProduct> image = productImageMapper.getProductImageById(productId);
			result.put("imageList", image);	    	 
	        return result;
	    }

		public  Boolean checkOwner(int productId, String memberId) {
			System.out.println("productId = " + productId);
			Boolean result = productMapper.getProductByMemberId(String.valueOf(productId), memberId) != null ? true : false;
			return result;
		}
	    
	    public int updateProduct(Product product) {
	        return productMapper.updateProduct(product);
	    }
	    
	    public int deleteProduct(String productId){
	        return productMapper.deleteProduct(productId);
	    }

}
