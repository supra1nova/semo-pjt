package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.service.RoomImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.selfdriven.semo.enums.ResultCode.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room-image")
public class RoomImageApiController {

    private final RoomImageService roomImageService;

    @PostMapping("/upload/{productId}/{roomId}")
    public ApiResponse uploadRoomImage(
            @RequestPart MultipartFile file,
            @PathVariable int productId,
            @PathVariable int roomId,
            HttpSession session) {
        String memberId = ((Login)session.getAttribute("login")).getId();
        int res = roomImageService.insertRoomImage(file, productId, roomId, memberId);
        return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_UPLOAD_IMAGE.getCode(), CANNOT_UPLOAD_IMAGE.getMessage());
    }

    @GetMapping("/load-one/{productId}/{roomId}")
    public ApiResponse loadRoomImage(
            @PathVariable int productId,
            @PathVariable int roomId,
            @RequestParam @Pattern(regexp="^[a-zA-Z0-9-_]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName){
        String imageUrl = roomImageService.getRoomImage(productId, roomId, fileName);
        return imageUrl != null ? ApiResponse.ok(imageUrl) : ApiResponse.fail(CANNOT_LOAD_IMAGE_URL.getCode(), CANNOT_LOAD_IMAGE_URL.getMessage());
    }

    @GetMapping("/load-all/{productId}/{roomId}")
    public ApiResponse loadAllRoomImagesByProductIdRoomId(
            @PathVariable int productId,
            @PathVariable int roomId){
        List<String> imageUrls = roomImageService.getAllRoomImagesByProductIdRoomId(productId, roomId);
        return imageUrls.size() != 0 ? ApiResponse.ok(imageUrls) : ApiResponse.fail(CANNOT_LOAD_IMAGE_URL.getCode(), CANNOT_LOAD_IMAGE_URL.getMessage());
    }

    @PostMapping("/delete/{productId}/{roomId}")
    public ApiResponse deleteRoomImage(
            @PathVariable int productId,
            @PathVariable int roomId,
            @RequestParam @Pattern(regexp="^[a-zA-Z0-9-_]{1,144}[.]((jpg|jpeg|gif|bmp|png){1})$", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.") String fileName,
            HttpSession session) {
        String memberId = ((Login)session.getAttribute("login")).getId();
        int res = roomImageService.deleteRoomImage(productId, roomId, fileName, memberId);
        return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_DELETE_IMAGE.getCode(), CANNOT_DELETE_IMAGE.getMessage());
    }
}
