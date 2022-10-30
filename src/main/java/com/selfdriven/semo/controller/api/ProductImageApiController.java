package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.selfdriven.semo.enums.ResultCode.*;

//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/product/image")
//
//public class ProductImageController {
//	private final S3UploadService s3Upload;

//	@PostMapping("/show")
//	// imageUrl이라는 명칭을 명확하게 productImageId또는 productImageName으로 변경 고려 필요
//	public ApiResponse getImage(String imageUrl) {
//		imageUrl = s3Upload.getImage(imageUrl);
//		ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
//		return response;
//	}
//
//	@PostMapping("/showAll")
//	public ApiResponse getAllImage(int productId) throws IOException {
//		String route = S3FileEnum.PRODUCT.getRoute() + productId +"/product";
//		String imageUrl = s3Upload.getImage(route);
//		ImageProduct image = new ImageProduct();
//		image.setImageUrl(imageUrl);
//		image.setProductId(productId);
//		s3Upload.insertProductImage(image);
//		ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
//		return response;
//	}
//
//	@PostMapping("/upload")
//	public ApiResponse uploadImage(@RequestPart("multipartFile") MultipartFile multipartFile,  int productId) throws IOException {
//		String route = S3FileEnum.PRODUCT.getRoute() + productId +"/productImage";
//		String imageUrl = s3Upload.uploadImage(route, multipartFile);
//		ProductImage image = new ProductImage();
//		image.setImageUrl(imageUrl);
//		image.setProductId(productId);
//		s3Upload.insertProductImage(image);
//		ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
//		return response;
//	}
//
//	@PostMapping("/delete")
//	public ApiResponse deleteImage(String imageUrl) throws IOException {
//		s3Upload.deleteFile(imageUrl);
//		s3Upload.deleteProductImage(imageUrl);
//		 ApiResponse response = imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
//
//			return response;
//
//	}


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product-image")

public class ProductImageApiController {

	private final ProductImageService productImageService;

	// member_id의 경우 interceptor 로 검증 과정 추가시 불필요 예상되나 확인 필요
	@PostMapping("/upload/{productId}")
	public ApiResponse uploadRoomImage(
			@RequestPart(value = "file", required = true) MultipartFile file,
			@RequestPart(required = true) String memberId,
			@PathVariable int productId) {
		int res = productImageService.insertProductImage(file, memberId, productId);
		return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_UPLOAD_IMAGE.getCode(), CANNOT_UPLOAD_IMAGE.getMessage());
	}

	@GetMapping("/load-one/{productId}")
	public ApiResponse loadProductImage(@PathVariable int productId, String fileName){
		String imageUrl = productImageService.getProductImage(productId, fileName);
		return imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(CANNOT_LOAD_IMAGE_URL.getCode(), CANNOT_LOAD_IMAGE_URL.getMessage());
	}

	@GetMapping("/load-all/{productId}")
	public ApiResponse loadAllProductImagesByRoomId(@PathVariable int productId){
		List<String> imageUrls = productImageService.getAllProductImagesByProductId(productId);
		return imageUrls.size() != 0 ? ApiResponse.ok(imageUrls) : ApiResponse.fail(CANNOT_LOAD_IMAGE_URL.getCode(), CANNOT_LOAD_IMAGE_URL.getMessage());
	}

	@PostMapping("/delete/{productId}/{fileName}")
	public ApiResponse deleteProductImage(String memberId, @PathVariable int productId, @PathVariable String fileName){
		int res = productImageService.deleteProductImage(memberId, productId, fileName);
		return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_DELETE_IMAGE.getCode(), CANNOT_DELETE_IMAGE.getMessage());
	}

}
