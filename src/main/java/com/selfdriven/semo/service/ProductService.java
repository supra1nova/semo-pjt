package com.selfdriven.semo.service;

import com.selfdriven.semo.entity.Product;
import com.selfdriven.semo.dto.login.Login;
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
@Transactional
public class ProductService {
	private final ProductMapper productMapper;
	private final ProductImageMapper productImageMapper;

	public int addProduct(Product product) {
		int result = 0;
		try {
			result = productMapper.addProduct(product);
		} catch(Exception e) {
		    e.printStackTrace();
		}
		return result;
	}

    // TODO: (확인필요) 특정 service 에서 관련되지 않은 다른 mapper를 호출해서 사용해도 좋은지?
	public  Map<String, Object> getProductById(int productId) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		try {
			result.put("productInfo", productMapper.getProductById(productId));
			result.put("imageList", productImageMapper.getProductImagesById(productId));
//			result.put("imageList", productImageService.getAllProductImagesByProductId(productId));
		} catch(Exception e) {
		    e.printStackTrace();
		}
		return result;
	}

	// TODO: 이미지 포함해서 가져올수 있도록 수정 필요?
	public List<Product> getProductList() {
		List<Product> result = null;
		try {
			result = productMapper.getProductList();
		} catch(Exception e) {
		    e.printStackTrace();
		}
		return result;
	}

	public int updateProduct(Product product, Login login) {
		int result = 0;
		try {
			// TODO: product의 productId 와 session의 memberId 를 이용해 db 조회를 통해 업체의 유효성 평가를 진행할지, 아니면 product의 memberId와 대조를 통해 단순 비교로 평가할지 결정 필요.
//			if(!checkProduct(product.getProductId(), login.getId())) {
			if(!String.valueOf(product.getMemberId()).equals(login.getId())) {
				throw new Exception("업체 또는 유저 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
			}
		    result = productMapper.updateProduct(product);
		} catch(Exception e) {
		    e.printStackTrace();
		}
		return result;
	}

	public int deleteProduct(int productId, Login login){
		int result = 0;
		try {
			if(!checkProduct(productId, login.getId())) {
				throw new Exception("업체 또는 유저 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
			}
			result = productMapper.deleteProduct(productId);
		} catch(Exception e) {
		    e.printStackTrace();
		}
		return result;
	}

	public  Boolean checkProduct(int productId, String memberId) {
		return productMapper.getProductByMemberId(String.valueOf(productId), memberId) != null ? true : false;
	}

}
