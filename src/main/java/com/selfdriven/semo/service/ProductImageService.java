package com.selfdriven.semo.service;

import com.selfdriven.semo.dto.Product;
import com.selfdriven.semo.dto.ProductImage;
import com.selfdriven.semo.dto.S3Component;
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
    private final S3Component s3Component;
    private final S3UploadService s3UploadService;

    public int insertProductImage(MultipartFile file, String memberId, int productId) {
        int res = 0;
        StringBuilder s3FileKeyPrefixStringBuilder = null;
        String fileName = null;
        try {
            Boolean productValidation = getProductValidation(memberId, productId);
            if(!productValidation) throw new Exception("업체가 존재하지 않거나 권한이 없습니다.");
            s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
            fileName = s3UploadService.uploadImage(String.valueOf(s3FileKeyPrefixStringBuilder), file);
            ProductImage productImage = ProductImage.builder()
                    .imageUrl(fileName)
                    .productId(productId).build();
            res = productImageMapper.insertProductImage(productImage);
        } catch(Exception e) {
            e.printStackTrace();
            // TODO: room image delete function 구현 for db rollback scenario
            if(fileName != null){
                s3UploadService.deleteImage(String.valueOf(s3FileKeyPrefixStringBuilder.append(fileName)));
            }
        }
        return res;
    }

    public String getProductImage(int productId, String fileName) {
        String imageUrl = null;
        try {
            Boolean productImageValidation = getProductImageValidation(productId, fileName)  ? true : false;
            if(!productImageValidation) throw new Exception("업체 또는 객실이 존재하지 않습니다.");
            StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
            String s3FileKey = String.valueOf(s3FileKeyPrefixStringBuilder.append("/").append(fileName));
            imageUrl = s3UploadService.getImageUrl(s3FileKey);
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
    public List<String> getAllProductImagesByProductId(int productId){
        List<String> imageUrls = new ArrayList<>();
        try {
            List<String> imageUrlsList = productImageMapper.getAllImageUris(productId);
            Iterator imageUrlsIterator = imageUrlsList.stream().iterator();
            while(imageUrlsIterator.hasNext()){
                imageUrls.add(s3Component.getUrlBeforePrefix()
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

    public int deleteProductImage(String memberId, int productId, String fileName) {
        int res = 0;
        try{
            // memberId, productId 이용해 validation checking을 진행
            Boolean productValidation = getProductValidation(memberId, productId);
            if(!productValidation) throw new Exception("업체가 존재하지 않거나 권한이 없습니다.");
            // 업체가 존재한다면 productId와 fileName을 이용해 s3 내 파일 삭제 진행
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
                .append(s3Component.getProductPath())
                .append("/")
                .append(productId);
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

    private Boolean getProductImageValidation(int productId, String fileName) {
        ProductImage productImage = ProductImage.builder()
                .productId(productId)
                .imageUrl(fileName)
                .build();
        return productImageMapper.getProductImageValidation(productImage) == 1 ? true : false;
    }

}
