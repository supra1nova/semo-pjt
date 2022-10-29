package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.service.RoomImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.selfdriven.semo.enums.ResultCode.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room-image")
public class RoomImageController {

    private final RoomImageService roomImageService;

    @PostMapping("/{productId}/{roomId}")
    public ApiResponse uploadRoomImage(
            @RequestPart(value = "file", required = true) MultipartFile file,
            @PathVariable int productId,
            @PathVariable int roomId) {
        int res = roomImageService.insertRoomImage(file, productId, roomId);
        return res != 0 ? ApiResponse.ok(res) : ApiResponse.fail(CANNOT_IMAGE_UPLOAD.getCode(), CANNOT_IMAGE_UPLOAD.getMessage());
    }

}
