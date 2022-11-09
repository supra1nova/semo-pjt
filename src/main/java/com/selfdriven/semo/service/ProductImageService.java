package com.selfdriven.semo.service;

import com.selfdriven.semo.config.S3Config;
import com.selfdriven.semo.entity.ProductImage;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.repository.ProductImageMapper;
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
public class ProductImageService {

    private final ProductService productService;
    private final ProductImageMapper productImageMapper;
    private final S3UploadService s3UploadService;
    private final S3Config s3Config;


    public int insertProductImage(MultipartFile file, int productId, Login login) {
        int res = 0;
        StringBuilder s3FileKeyPrefixStringBuilder = null;
        String fileName = null;
        try {
            if(!productService.checkProduct(productId, login.getId())) {
                throw new Exception("업체가 존재하지 않거나 권한이 없습니다.");
            }
            s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
            fileName = s3UploadService.uploadImage(String.valueOf(s3FileKeyPrefixStringBuilder), file);
            ProductImage productImage = ProductImage.builder()
                    .imageUrl(fileName)
                    .productId(productId).build();
            res = productImageMapper.insertProductImage(productImage);
        } catch(Exception e) {
            e.printStackTrace();
            if(fileName != null){
                s3UploadService.deleteImage(String.valueOf(s3FileKeyPrefixStringBuilder.append(fileName)));
            }
        }
        return res;
    }

//    public String getProductImage(int productId, String fileName) {
//        String imageUrl = null;
//        try {
//            if(!checkProductImage(productId, fileName)) {
//                throw new Exception("업체 또는 객실이 존재하지 않습니다.");
//            }
//            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
//            String s3FileKey = String.valueOf(s3FileKeyPrefixStringBuilder.append("/").append(fileName));
//            imageUrl = s3UploadService.getImageUrl(s3FileKey);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return imageUrl;
//    }

    // db를 통해 product 및 productImage 관련 유효성 검증한 뒤, 별도의 추가 db 접속 없이 fileName과 s3 url주소를 조합해 파일 url을 생성해 반환
    public String getProductImage(int productId, String fileName){
        String imageUrl = null;
        try {
            if(!checkProductImage(productId, fileName)) {
                throw new Exception("업체 또는 객실이 존재하지 않습니다.");
            }
            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
            imageUrl = s3Config.getUrlBeforePrefix().concat(String.valueOf(s3FileKeyPrefixStringBuilder.append("/").append(fileName)));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }

//    // s3 aws 에서 모든 이미지 주소를 가져옴
//    public List<String> getAllProductImagesByProductId(int productId){
//        List<String> imageUrls = new ArrayList<>();
//        try {
//            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
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
    public List<String> getAllProductImagesByProductId(int productId){
        List<String> imageUrls = new ArrayList<>();
        try {
            List<String> imageUrlsList = productImageMapper.getAllImageUris(productId);
            Iterator imageUrlsIterator = imageUrlsList.stream().iterator();
            while(imageUrlsIterator.hasNext()){
                imageUrls.add(s3Config.getUrlBeforePrefix()
                        .concat(getPathStringBuilder(productId).toString())
                        .concat("/")
                        .concat(imageUrlsIterator.next().toString()));
            }
        } catch(Exception e) {
            e.printStackTrace();
            imageUrls.clear();
        }
        return imageUrls;
    }

    // TODO: (확인필요) db에서 삭제시 s3에서도 삭제 되었는지 검증가능해야하는데, 올바르지 않은 이미지를 삭제하거나 조회해도 return으로 주소값이 항상 나온다 -> 그럼 지금처럼 s3에서 목록확인한 다음 삭제해야하나?
    public int deleteProductImage(int productId, String fileName, Login login) {
        int res = 0;
        try{
            if(!productService.checkProduct(productId, login.getId())) {
                throw new Exception("업체가 존재하지 않거나 권한이 없습니다.");
            }
            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
            String s3FileKey = String.valueOf(s3FileKeyPrefixStringBuilder);
            List<String> imageUriList = s3UploadService.getAllImageUrls(String.valueOf(s3FileKeyPrefixStringBuilder));
            Iterator i = imageUriList.stream().iterator();
            while(i.hasNext()){
                if(String.valueOf(i.next()).contains(fileName)) {
                    s3UploadService.deleteImage(s3FileKey.concat("/").concat(fileName));
                    res = productImageMapper.deleteProductImage(fileName);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private StringBuilder getPathStringBuilder(int productId) {
        StringBuilder s3FileKeyPrefixStringBuilder = new StringBuilder()
                .append(s3Config.getProductPath())
                .append("/")
                .append(productId);
        return s3FileKeyPrefixStringBuilder;
    }

    private Boolean checkProductImage(int productId, String fileName) {
        ProductImage productImage = ProductImage.builder()
                .productId(productId)
                .imageUrl(fileName)
                .build();
        return productImageMapper.countValidRoomImage(productImage) == 1 ? true : false;
    }

}
