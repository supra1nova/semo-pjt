package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.ProductImage;
import com.selfdriven.semo.enums.S3FileEnum;
import com.selfdriven.semo.service.ProductImageService;
import com.selfdriven.semo.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product/image")

public class ProductImageController {
	private final S3UploadService s3Upload;
	private final ProductImageService productImageService;

//	@PostMapping("/show")
//	// imageUrl이라는 명칭을 명확하게 productImageId또는 productImageName으로 변경 고려 필요
//	public ApiResponse getImage(String imageUrl) {
//		imageUrl = s3Upload.getImage(imageUrl);
//		ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
//		return response;
//	}

	@PostMapping("/showAll")
	public ApiResponse getAllImageUrlsByProductId(int productId) throws IOException {
//		String route = S3FileEnum.PRODUCT.getRoute() + productId +"/product";
//		String imageUrl = s3Upload.getImage(route);
//		ProductImage image = new ImageProduct();
//		image.setImageUrl(imageUrl);
//		image.setProductId(productId);
//		s3Upload.insertProductImage(image);
		productImageService.getAllImageUrlsByProductId(productId);
		String imageUrl = null;
		ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
		return response;
	}

	@PostMapping("/upload")
	public ApiResponse uploadImage(@RequestPart("multipartFile") MultipartFile multipartFile,  int productId) throws IOException {
		String route = S3FileEnum.PRODUCT.getRoute() + productId +"/productImage";
		String imageUrl = s3Upload.uploadImage(route, multipartFile);
		ProductImage image = new ProductImage();
		image.setImageUrl(imageUrl);
		image.setProductId(productId);
		s3Upload.insertProductImage(image);
		ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
		return response;
	}
	    
	@PostMapping("/delete")
	public ApiResponse deleteImage(String imageUrl) throws IOException {
		s3Upload.deleteFile(imageUrl);
		s3Upload.deleteProductImage(imageUrl);
		 ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");

			return response;

	}
}
