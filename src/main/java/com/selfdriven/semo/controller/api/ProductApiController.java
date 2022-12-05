package com.selfdriven.semo.controller.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.util.SessionUtil;
import org.springframework.web.bind.annotation.*;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.entity.Product;
import com.selfdriven.semo.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductApiController {
	private final ProductService productService;

	@PostMapping("/create")
	public ApiResponse createProduct(@Valid @RequestBody Product product) {
		int result = productService.addProduct(product);
		return ApiResponse.ok(result);
	}

	@GetMapping("/info/{productId}")
	public ApiResponse getProductById(@PathVariable int productId) {
		Map<String, Object> result = productService.getProductById(productId);
		return ApiResponse.ok(result);
	}

	// TODO: return에 들어가는 객체의 정보가 DB내 정보와 동일함에 따라 불필요한 정보가 노출됨 -> 선별해서 return 할 수 있도록 새로운 DTO 생성해서 return 하는 것이 나을 것 같음. 고려해 볼 것.
	@GetMapping("/list")
	public ApiResponse getProductList() {
		List<Product> result = productService.getProductList();
		return ApiResponse.ok(result);
	}

	@PostMapping("/edit")
	public ApiResponse editProduct(@Valid @RequestBody Product product) {
		int result = productService.updateProduct(product);
		return ApiResponse.ok(result);
	}

	// TODO: 삭제시 객실 및 이미지 정보들 모두 삭제? 아니면 DB 이전 보관? 고민 필요
	@PostMapping("/delete")
	public ApiResponse deleteProduct(@RequestParam int productId, HttpSession session) {
		Login login = SessionUtil.getLoginFromSession(session);
		int result = productService.deleteProduct(productId, login);
		return ApiResponse.ok(result);
	}

}
