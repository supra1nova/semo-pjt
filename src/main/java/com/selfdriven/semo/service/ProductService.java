package com.selfdriven.semo.service;

import com.selfdriven.semo.entity.Product;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import com.selfdriven.semo.repository.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
//@Transactional -> transactional 은 각 메서드에 걸어사 사용
public class ProductService {
	private final ProductMapper productMapper;
	private final ProductImageService productImageService;

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
		result.put("imageList", productImageService.getAllProductImagesByProductId(productId));
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

    public Map<String, Object> getProductIdByMemberId(String memberId) {
		// TODO: 프론트에서 넘어올때 따옴표가 붙어있어서 replace로 지워주는 과정을 넣었다. -> 혹시 뭔가 방법이 잘못되서 따옴표가 들어가 있는 것은 아닌지?
		memberId = memberId.replace("\"", "");
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("productList", productMapper.getProductIdByMemberId(memberId));
		if(result == null || ((List<?>) result.get("productList")).size() == 0){
			throw new ApiException(ResultCode.INVALID_PARAMETER);
		}
		System.out.println("result = " + result);
		return result;
    }

}
