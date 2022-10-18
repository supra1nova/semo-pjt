package com.selfdriven.semo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.selfdriven.semo.dto.Member;
import com.selfdriven.semo.dto.Product;
import com.selfdriven.semo.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
	 private final ProductMapper productMapper;

	    public int addProduct(Product product) {
	        return productMapper.addProduct(product);
	    }


	    public List<Product> getProductList() {
	        return productMapper.getProductList();
	    }
	    
	    public int deleteProduct(String productId){
	        return productMapper.deleteProduct(productId);
	    }

}
