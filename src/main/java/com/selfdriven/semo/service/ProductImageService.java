package com.selfdriven.semo.service;

import com.selfdriven.semo.config.S3Config;
import com.selfdriven.semo.entity.ProductImage;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import com.selfdriven.semo.repository.ProductImageMapper;
import com.selfdriven.semo.repository.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductImageService {

//    private final ProductService productService;
    private final ProductImageMapper productImageMapper;
    private final ProductMapper productMapper;
    private final S3UploadService s3UploadService;
    private final S3Config s3Config;


    public int insertProductImage(MultipartFile file, int productId, Login login) throws IOException {
//        if(!productService.checkProduct(productId, login.getId())) {
        if(!checkProduct(productId, login.getId())) {
            throw new ApiException(ResultCode.ACCESS_DENIED);
        }
        StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
        String fileName = s3UploadService.uploadImage(String.valueOf(s3FileKeyPrefixStringBuilder), file);
        ProductImage productImage = ProductImage.builder()
                .imageUrl(fileName)
                .productId(productId).build();
        int res = productImageMapper.insertProductImage(productImage);
        return res;
    }

    public String getProductImage(int productId, String fileName) throws UnsupportedEncodingException {
        if(!checkProductImage(productId, fileName)) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        }
        StringBuilder s3FileKeyPrefixStringBuilder = getPathStringBuilder(productId);
        String s3FileKey = String.valueOf(s3FileKeyPrefixStringBuilder.append("/").append(fileName));
        String imageUrl = s3UploadService.getImageUrl(s3FileKey);
        return imageUrl;
    }

//    // s3 aws ?????? ?????? ????????? ????????? ?????????
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

    // db?????? ????????? uri??? ????????? ????????? url??? ?????? ??? ??????????????? ??????
    public List<String> getAllProductImagesByProductId(int productId){
        List<String> imageUrls = new ArrayList<>();
        List<String> imageUrlsList = productImageMapper.getAllImageUris(productId);
        Iterator imageUrlsIterator = imageUrlsList.stream().iterator();
        while(imageUrlsIterator.hasNext()){
            // TODO: imageUrl ??? ???????????? ????????? ???????????? imageUrls??? ????????????????????? ????????? ??????...? ????????? ????????? ?????? ???????????? ????????? ???????
            imageUrls.add(s3Config.getUrlBeforePrefix()
                    .concat(getPathStringBuilder(productId).toString())
                    .concat("/")
                    .concat(imageUrlsIterator.next().toString()));
        }
        return imageUrls;
    }

    // TODO: (????????????) db?????? ????????? s3????????? ?????? ???????????? ???????????????????????????, ???????????? ?????? ???????????? ??????????????? ???????????? return?????? ???????????? ?????? ????????? -> ?????? ???????????? s3?????? ??????????????? ?????? ???????????????????
    public int deleteProductImage(int productId, String fileName, Login login) {
        int res = 0;
//        if(!productService.checkProduct(productId, login.getId())) {
        if(!checkProduct(productId, login.getId())) {
            throw new ApiException(ResultCode.ACCESS_DENIED);
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

    public  Boolean checkProduct(int productId, String memberId) {
		return productMapper.getProductByMemberId(productId, memberId) != null ? true : false;
	}

}
