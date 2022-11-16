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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
			HttpSession session) throws IOException {
		Login login = SessionUtil.getLoginFromSession(session);
		int res = productImageService.insertProductImage(file, productId, login);
		return ApiResponse.ok(res);
	}

	@PostMapping("/load-one")
	public ApiResponse loadProductImage(
			@RequestParam int productId,
			@RequestParam @Pattern(regexp="^[가-힣a-zA-Z0-9-_.%]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName) throws UnsupportedEncodingException {
		String imageUrl = productImageService.getProductImage(productId, fileName);
		return ApiResponse.ok(imageUrl);
	}

	@PostMapping("/load-all")
	public ApiResponse loadAllProductImagesByRoomId(@RequestParam int productId) {
		List<String> imageUrls = productImageService.getAllProductImagesByProductId(productId);
		return ApiResponse.ok(imageUrls);
	}

	@PostMapping("/delete")
	public ApiResponse deleteProductImage(
			@RequestParam int productId,
			@RequestParam @Pattern(regexp="^[가-힣a-zA-Z0-9-_.%]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName,
			HttpSession session) {
		Login login = SessionUtil.getLoginFromSession(session);
		int res = productImageService.deleteProductImage(productId, fileName, login);
		return ApiResponse.ok(res);
	}
}
