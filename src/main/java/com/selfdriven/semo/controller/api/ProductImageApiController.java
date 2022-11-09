package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.service.ProductImageService;
import com.selfdriven.semo.util.SessionUtil;
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

	@PostMapping("/upload")
	public ApiResponse uploadRoomImage(
			@RequestPart MultipartFile file,
			@RequestParam int productId,
			HttpSession session) {
		Login login = SessionUtil.getLoginFromSession(session);
		int res = productImageService.insertProductImage(file, productId, login);
		return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_UPLOAD_IMAGE.getCode(), CANNOT_UPLOAD_IMAGE.getMessage());
	}

	@PostMapping("/load-one")
	public ApiResponse loadProductImage(
			@RequestParam int productId,
			@RequestParam @Pattern(regexp="^[a-zA-Z0-9-_]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName) {
		String imageUrl = productImageService.getProductImage(productId, fileName);
		return imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(CANNOT_LOAD_IMAGE_URL.getCode(), CANNOT_LOAD_IMAGE_URL.getMessage());
	}

	@PostMapping("/load-all")
	public ApiResponse loadAllProductImagesByRoomId(@RequestParam int productId) {
		List<String> imageUrls = productImageService.getAllProductImagesByProductId(productId);
		return imageUrls.size() != 0 ? ApiResponse.ok(imageUrls) : ApiResponse.fail(CANNOT_LOAD_IMAGE_URL.getCode(), CANNOT_LOAD_IMAGE_URL.getMessage());
	}

	@PostMapping("/delete")
	public ApiResponse deleteProductImage(
			@RequestParam int productId,
			@RequestParam @Pattern(regexp="^[a-zA-Z0-9-_]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName,
			HttpSession session) {
		Login login = SessionUtil.getLoginFromSession(session);
		int res = productImageService.deleteProductImage(productId, fileName, login);
		return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_DELETE_IMAGE.getCode(), CANNOT_DELETE_IMAGE.getMessage());
	}
}
