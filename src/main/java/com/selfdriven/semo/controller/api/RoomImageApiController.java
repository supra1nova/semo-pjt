package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.service.RoomImageService;
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
@RequestMapping("/api/room-image")
public class RoomImageApiController {

    private final RoomImageService roomImageService;

    @PostMapping("/upload")
    public ApiResponse uploadRoomImage(
            @RequestPart MultipartFile file,
            @RequestParam int productId,
            @RequestParam int roomId,
            HttpSession session) throws IOException {
        Login login = SessionUtil.getLoginFromSession(session);
        int res = roomImageService.insertRoomImage(file, productId, roomId, login);
        return ApiResponse.ok(res);
    }

    @PostMapping("/load-one")
    public ApiResponse loadRoomImage(
            @RequestParam int productId,
            @RequestParam int roomId,
            @RequestParam @Pattern(regexp="^[가-힣a-zA-Z0-9-_.%]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName) {
        String imageUrl = roomImageService.getRoomImage(productId, roomId, fileName);
        return ApiResponse.ok(imageUrl);
    }

    @PostMapping("/load-all")
    public ApiResponse loadAllRoomImagesByProductIdRoomId(
            @RequestParam int productId,
            @RequestParam int roomId){
        List<String> imageUrls = roomImageService.getAllRoomImagesByProductIdRoomId(productId, roomId);
        return ApiResponse.ok(imageUrls);
    }

    @PostMapping("/delete")
    public ApiResponse deleteRoomImage(
            @RequestParam int productId,
            @RequestParam int roomId,
            @RequestParam @Pattern(regexp="^[가-힣a-zA-Z0-9-_.%]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName,
            HttpSession session) {
        Login login = SessionUtil.getLoginFromSession(session);
        int res = roomImageService.deleteRoomImage(productId, roomId, fileName, login);
        return ApiResponse.ok(res);
    }
}
