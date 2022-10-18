package com.selfdriven.semo.controller.api;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.ImageProduct;
import com.selfdriven.semo.dto.Product;
import com.selfdriven.semo.enums.S3FileEnum;
import com.selfdriven.semo.service.S3UploadService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")

public class S3FileController {
	  private final S3UploadService s3Upload;

	    @PostMapping("/uploadImage")
	    public ApiResponse uploadImage(@RequestParam("image") MultipartFile multipartFile, Product product) throws IOException {
	    	
	    	String route = S3FileEnum.PRODUCT.getRoute() + product.getProductId();
	    	String imageUrl = s3Upload.upload(route, multipartFile);
	    	ImageProduct image = new ImageProduct();
	    	image.setImageUrl(imageUrl);
	    	image.setProductId(product.getProductId());	
	    	
	    	s3Upload.insertProductImage(image);
	    	 ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
		        return response;
	    }
	    @PostMapping("/deleteImage")
	    public ApiResponse deleteImage(String imageUrl) throws IOException {

			s3Upload.deleteFile(imageUrl);
	    	s3Upload.deleteProductImage(imageUrl);
	    	 ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");

				return response;

	    }
}
