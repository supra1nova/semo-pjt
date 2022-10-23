package com.selfdriven.semo.controller.api;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.Product;
import com.selfdriven.semo.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductApiController {
	 private final ProductService productService;
	 
	  @PostMapping("/createProduct")
	    public ApiResponse createProduct(@Valid @RequestBody Product product) {
	        int result = productService.addProduct(product);
	        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
	        return response;
	    }

	 @GetMapping("/getProductList")
	    public ApiResponse getProductList() {
	        List<Product> result = productService.getProductList();
	        ApiResponse response = result.size() != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
	        return response;	
	    }
	 @GetMapping("/getProductById")
	    public ApiResponse getProductById(String productId) {
	        Map<String, Object> result = productService.getProductById(productId);
	        ApiResponse response = result.size() != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
	        return response;	
	    }
	 @PostMapping("/deleteProduct")
	    public ApiResponse deleteProduct(@RequestParam String productId) {
	        int result = productService.deleteProduct(productId);
	        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
	        return response;
	    }

}
