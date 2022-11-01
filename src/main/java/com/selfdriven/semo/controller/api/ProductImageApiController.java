package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.selfdriven.semo.enums.ResultCode.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product-image")
public class ProductImageApiController {

	private final ProductImageService productImageService;

	@PostMapping("/upload/{productId}")
	public ApiResponse uploadRoomImage(
			@RequestPart MultipartFile file,
			// TODO: product_id의 경우에도 @PathVariable을 통해 가져올지 확인 필요 -> 나머지 메서드들도 같이 고민 필요
			@PathVariable int productId,
			HttpSession session) {
		String memberId = ((Login)session.getAttribute("login")).getId();
		int res = productImageService.insertProductImage(file, memberId, productId);
		return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_UPLOAD_IMAGE.getCode(), CANNOT_UPLOAD_IMAGE.getMessage());
	}

	@GetMapping("/load-one/{productId}")
	public ApiResponse loadProductImage(
			@PathVariable int productId,
			@RequestParam @Pattern(regexp="^[a-zA-Z0-9-_]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName) {
		String imageUrl = productImageService.getProductImage(productId, fileName);
		return imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(CANNOT_LOAD_IMAGE_URL.getCode(), CANNOT_LOAD_IMAGE_URL.getMessage());
	}

	@GetMapping("/load-all/{productId}")
	public ApiResponse loadAllProductImagesByRoomId(@PathVariable int productId) {
		List<String> imageUrls = productImageService.getAllProductImagesByProductId(productId);
		return imageUrls.size() != 0 ? ApiResponse.ok(imageUrls) : ApiResponse.fail(CANNOT_LOAD_IMAGE_URL.getCode(), CANNOT_LOAD_IMAGE_URL.getMessage());
	}

	@PostMapping("/delete/{productId}")
	public ApiResponse deleteProductImage(
			@PathVariable int productId,
			@RequestParam @Pattern(regexp="^[a-zA-Z0-9-_]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName,
			HttpSession session) {
		String memberId = ((Login)session.getAttribute("login")).getId();
		int res = productImageService.deleteProductImage(memberId, productId, fileName);
		return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_DELETE_IMAGE.getCode(), CANNOT_DELETE_IMAGE.getMessage());
	}
}
