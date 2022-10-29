package com.selfdriven.semo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImageService {

    public List<String> getAllImageUrlsByProductId(int productId){
        List<String> productImageUrls = new ArrayList<>();
        return productImageUrls;
    }
}
