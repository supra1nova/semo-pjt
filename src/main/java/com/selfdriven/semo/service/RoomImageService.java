package com.selfdriven.semo.service;

import com.selfdriven.semo.config.S3Config;
import com.selfdriven.semo.entity.RoomImage;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import com.selfdriven.semo.repository.RoomImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomImageService {

//    private final ProductService productService;
    private final ProductImageService productImageService;
    private final RoomService roomService;
    private final RoomImageMapper roomImageMapper;
    private final S3UploadService s3UploadService;
    private final S3Config s3Config;

    public int insertRoomImage(MultipartFile file, int productId, int roomId, Login login) throws IOException {
        if(!productImageService.checkProduct(productId, login.getId())) {
            throw new ApiException(ResultCode.ACCESS_DENIED);
        } else if (!roomService.checkRoom(productId, roomId)) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        }
        StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
        String fileName = s3UploadService.uploadImage(String.valueOf(s3FileKeyPrefixStringBuilder), file);
        RoomImage roomImage = RoomImage.builder()
                .imageUrl(fileName)
                .roomId(roomId).build();
        int res = roomImageMapper.insertRoomImage(roomImage);
        return res;
    }

//    // s3 aws 에서 이미지 주소를 가져옴
//    public String getRoomImage(int productId, int roomId, String fileName){
//        String imageUrl = null;
//        try {
//            if(!roomService.checkRoom(productId, roomId)) throw new Exception("업체 또는 객실이 존재하지 않습니다.");
//            if(!checkRoomImage(roomId, fileName)) throw new Exception("이미지가 존재하지 않습니다.");
//            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
//            String s3FileKey = String.valueOf(s3FileKeyPrefixStringBuilder.append("/").append(fileName));
//            imageUrl = s3UploadService.getImageUrl(s3FileKey);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return imageUrl;
//    }

    // db를 통해 room 및 roomImage 관련 유효성 검증한 뒤, 별도의 추가 db 접속 없이 fileName과 s3 url주소를 조합해 파일 url을 생성해 반환
    public String getRoomImage(int productId, int roomId, String fileName){
        if(!roomService.checkRoom(productId, roomId)) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        } else if(!checkRoomImage(roomId, fileName)) {
            throw new ApiException(ResultCode.CANNOT_LOAD_IMAGE_URL);
        }
        StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
        String imageUrl = s3Config.getUrlBeforePrefix().concat(String.valueOf(s3FileKeyPrefixStringBuilder.append("/").append(fileName)));
        return imageUrl;
    }

//    // s3 aws 에서 모든 이미지 주소를 가져옴
//    public List<String> getAllRoomImagesByProductIdRoomId(int productId, int roomId){
//        List<String> imageUrls = new ArrayList<>();
//        try {
//            if(!roomService.checkRoom(productId, roomId)) throw new Exception("업체 또는 객실이 존재하지 않습니다.");
//            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
//            String s3FileKeyPrefix = String.valueOf(s3FileKeyPrefixStringBuilder);
//            List<String> imageUrlsList = s3UploadService.getAllImageUrls(s3FileKeyPrefix);
//            Iterator imageUrlsIterator = imageUrlsList.stream().iterator();
//            while(imageUrlsIterator.hasNext()){
//                imageUrls.add(s3Config.getUrlBeforePrefix()
//                        .concat(imageUrlsIterator.next().toString()));
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//            imageUrls.clear();
//        }
//        return imageUrls;
//    }

    // db에서 이미지 uri와 이름을 가져와 url과 합친 뒤 컨트롤러로 반환
    public List<String> getAllRoomImagesByProductIdRoomId(int productId, int roomId){
        List<String> imageUrls = new ArrayList<>();
        if(!roomService.checkRoom(productId, roomId)) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        }
        List<String> imageUrlsList = roomImageMapper.getAllImageUris(productId, roomId);
        Iterator imageUrlsIterator = imageUrlsList.stream().iterator();
        while(imageUrlsIterator.hasNext()){
            imageUrls.add(s3Config.getUrlBeforePrefix()
                    .concat(getPathStringBuilder(productId, roomId).toString())
                    .concat("/")
                    .concat(imageUrlsIterator.next().toString()));
        }
        return imageUrls;
    }

    // TODO: (확인필요) db에서 삭제시 s3에서도 삭제 되었는지 검증가능해야하는데, 올바르지 않은 이미지를 삭제하거나 조회해도 return으로 주소값이 항상 나온다 -> 그럼 지금처럼 s3에서 목록확인한 다음 삭제해야하나?
    public int deleteRoomImage(int productId, int roomId, String fileName, Login login){
        int res = 0;
        if(!productImageService.checkProduct(productId, login.getId())) {
            throw new ApiException(ResultCode.ACCESS_DENIED);
        } else if(!roomService.checkRoom(productId, roomId)) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        }
        StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
        String s3FileKey = String.valueOf(s3FileKeyPrefixStringBuilder);
        List<String> imageUriList = s3UploadService.getAllImageUrls(String.valueOf(s3FileKeyPrefixStringBuilder));
        Iterator i = imageUriList.stream().iterator();
        while(i.hasNext()){
            if(String.valueOf(i.next()).contains(fileName)) {
                s3UploadService.deleteImage(s3FileKey.concat("/").concat(fileName));
                res = roomImageMapper.deleteRoomImage(fileName);
                break;
            }
        }
        return res;
    }

    private StringBuilder getPathStringBuilder(int productId, int roomId) {
        StringBuilder s3FileKeyPrefixStringBuilder = new StringBuilder()
                .append(s3Config.getProductPath())
                .append("/")
                .append(productId)
                .append("/")
                .append(roomId);
        return s3FileKeyPrefixStringBuilder;
    }

    private Boolean checkRoomImage(int roomId, String fileName) {
        return roomImageMapper.countValidRoomImage(roomId, fileName) == 1 ? true : false;
    }

}
