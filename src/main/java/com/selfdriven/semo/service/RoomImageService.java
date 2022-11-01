package com.selfdriven.semo.service;

import com.selfdriven.semo.dto.Product;
import com.selfdriven.semo.dto.Room;
import com.selfdriven.semo.dto.RoomImage;
import com.selfdriven.semo.dto.S3Component;
import com.selfdriven.semo.repository.RoomImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomImageService {

    private final ProductService productService;
    private final RoomService roomService;
    private final RoomImageMapper roomImageMapper;
    private final S3UploadService s3UploadService;
    private final S3Component s3Component;

    public int insertRoomImage(MultipartFile file, int productId, int roomId, String memberId) {
        int res = 0;
        StringBuilder s3FileKeyPrefixStringBuilder = null;
        String fileName = null;
        try {
            Boolean productValidation = getProductValidation(memberId, productId);
            if(!productValidation) throw new Exception("업체가 존재하지 않거나 권한이 없습니다.");
            Boolean roomValidation = getRoomValidation(productId, roomId);
            if(!roomValidation) throw new Exception("객실이 존재하지 않습니다.");
            s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
            fileName = s3UploadService.uploadImage(String.valueOf(s3FileKeyPrefixStringBuilder), file);
            RoomImage roomImage = RoomImage.builder()
                    .imageUrl(fileName)
                    .roomId(roomId).build();
            res = roomImageMapper.insertRoomImage(roomImage);
        } catch(Exception e) {
            e.printStackTrace();
            if(fileName != null){
                s3UploadService.deleteImage(String.valueOf(s3FileKeyPrefixStringBuilder.append(fileName)));
            }
        }
        return res;
    }

    // s3 aws 에서 이미지 주소를 가져옴
    public String getRoomImage(int productId, int roomId, String fileName){
        String imageUrl = null;
        try {
            Boolean roomValidation = getRoomValidation(productId, roomId);
            Boolean roomImageValidation = getRoomImageValidation(roomId, fileName)  ? true : false;
            if(!roomValidation || !roomImageValidation) throw new Exception("업체 또는 객실이 존재하지 않습니다.");
            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
            String s3FileKey = String.valueOf(s3FileKeyPrefixStringBuilder.append("/").append(fileName));
            imageUrl = s3UploadService.getImageUrl(s3FileKey);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }

//    // db에서 room 및 roomImage 관련 유효성 검증한 뒤, 별도의 추가 db 접속 없이 fileName과 s3 url주소를 조합해 파일 url을 생성해 반환
//    public String getRoomImage(int productId, int roomId, String fileName){
//        String imageUrl = null;
//        try {
//            Boolean roomValidation = getRoomValidation(productId, roomId);
//            Boolean roomImageValidation = getRoomImageValidation(roomId, fileName)  ? true : false;
//            if(!roomValidation || !roomImageValidation) throw new Exception("업체 또는 객실이 존재하지 않습니다.");
//            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
//            imageUrl = s3Component.getUrlBeforePrefix().concat(String.valueOf(s3FileKeyPrefixStringBuilder.append("/").append(fileName)));
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return imageUrl;
//    }

//    // s3 aws 에서 모든 이미지 주소를 가져옴
//    public List<String> getAllRoomImagesByProductIdRoomId(int productId, int roomId){
//        List<String> imageUrls = new ArrayList<>();
//        try {
//            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId, roomId);
//            String s3FileKeyPrefix = String.valueOf(s3FileKeyPrefixStringBuilder);
//            List<String> imageUrlsList = s3UploadService.getAllImageUrls(s3FileKeyPrefix);
//            Iterator imageUrlsIterator = imageUrlsList.stream().iterator();
//            while(imageUrlsIterator.hasNext()){
//                imageUrls.add(s3Component.getUrlBeforePrefix()
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
        try {
            Room room = Room.builder()
                    .productId(productId)
                    .roomId(roomId)
                    .build();
            List<String> imageUrlsList = roomImageMapper.getAllImageUris(room);
            Iterator imageUrlsIterator = imageUrlsList.stream().iterator();
            while(imageUrlsIterator.hasNext()){
                imageUrls.add(s3Component.getUrlBeforePrefix()
                        .concat(getPathStringBuilder(productId, roomId).toString())
                        .concat("/")
                        .concat(imageUrlsIterator.next().toString()));
            }
        } catch(Exception e) {
            e.printStackTrace();
            imageUrls.clear();
        }
        return imageUrls;
    }

    public int deleteRoomImage(int productId, int roomId, String fileName, String memberId){
        int res = 0;
        try {
            Boolean productValidation = getProductValidation(memberId, productId);
            if(!productValidation) throw new Exception("업체가 존재하지 않거나 권한이 없습니다.");
            Boolean roomValidationChecking = getRoomValidation(productId, roomId);
            if(!roomValidationChecking) throw new Exception("업체 또는 객실이 존재하지 않습니다.");
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
        } catch(Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private StringBuilder getPathStringBuilder(int productId, int roomId) {
        StringBuilder s3FileKeyPrefixStringBuilder = new StringBuilder()
                .append(s3Component.getProductPath())
                .append("/")
                .append(productId)
                .append("/")
                .append(roomId);
        return s3FileKeyPrefixStringBuilder;
    }

    private Boolean getProductValidation(String memberId, int productId) {
        Product product = Product.builder()
                .memberId(memberId)
                .productId(productId)
                .build();
        Boolean productValidation = productService.checkProductValidation(product);
        return productValidation;
    }

    private Boolean getRoomValidation(int productId, int roomId) {
        Room room = Room.builder()
                .productId(productId)
                .roomId(roomId)
                .build();
        Boolean roomValidation = roomService.checkRoomValidation(room);
        return roomValidation;
    }

    private Boolean getRoomImageValidation(int roomId, String fileName) {
        RoomImage roomImage = RoomImage.builder()
                .roomId(roomId)
                .imageUrl(fileName)
                .build();
        return roomImageMapper.getRoomImageValidation(roomImage) == 1 ? true : false;
    }

}
