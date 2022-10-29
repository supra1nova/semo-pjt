package com.selfdriven.semo.service;

import com.selfdriven.semo.dto.RoomImage;
import com.selfdriven.semo.dto.S3Component;
import com.selfdriven.semo.repository.RoomImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomImageService {

    private final ProductService productService;
    private final RoomService roomService;
    private final RoomImageMapper roomImageMapper;
    private final S3UploadService s3UploadService;
    private final S3Component s3Component;

    public int insertRoomImage(MultipartFile file, int productId, int roomId){
        int res = 0;
        try {
            Boolean productValidationChecking = productService.checkProductValidation(productId);
            Boolean roomValidationChecking = roomService.checkRoomValidation(roomId);
            if(productValidationChecking != true || roomValidationChecking != true) throw new Exception("유효한 업체가 아닙니다.");
            StringBuilder uploadPathStringBuilder = new StringBuilder()
                    .append(s3Component.getProductPath())
                    .append("/")
                    .append(productId)
                    .append("/")
                    .append(roomId);
            String fileName = s3UploadService.newUploadImage(String.valueOf(uploadPathStringBuilder), file);
            RoomImage roomImage = RoomImage.builder()
                    .imageUrl(fileName)
                    .roomId(roomId).build();
            res = roomImageMapper.insertRoomImage(roomImage);
        } catch(Exception e) {
            e.printStackTrace();
            // TODO: room image delete function 구현 for db rollback scenario
        }
//        s3UploadService.uploadImage(file);

        return res;
    }
}
