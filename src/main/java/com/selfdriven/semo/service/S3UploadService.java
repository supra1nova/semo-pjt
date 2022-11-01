package com.selfdriven.semo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.selfdriven.semo.dto.S3Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class S3UploadService {

//	@Value("${cloud.aws.s3.bucket}")
//	private String bucket;
//	private final ProductImageMapper productImageMapper;

	private final AmazonS3 amazonS3;
	private final S3Component s3Component;

//	public String uploadImage(String route, MultipartFile multipartFile) throws IOException {
//		String s3FileName = route+ "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename(); //폴더 경로 + 랜덤 아이디 + 파일명
//		ObjectMetadata objMeta = new ObjectMetadata();
//		objMeta.setContentLength(multipartFile.getInputStream().available());
//		objMeta.setContentType(multipartFile.getContentType());
//		amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
//		return URLDecoder.decode(amazonS3.getUrl(bucket, s3FileName).toString(), "utf-8");  // aws에서 반환한 url 강제 utf-8 decoding
//	}
//
//	public String editImage(String s3FileName, MultipartFile multipartFile) throws IOException {
//		ObjectMetadata objMeta = new ObjectMetadata();
//		objMeta.setContentLength(multipartFile.getInputStream().available());
//		objMeta.setContentType(multipartFile.getContentType());
//		amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
//		return URLDecoder.decode(amazonS3.getUrl(bucket, s3FileName).toString(), "utf-8");  // aws에서 반환한 url 강제 utf-8 decoding
//	}
//
//	public int insertProductImage(ProductImage image) {
//		return productImageMapper.insertProductImage(image);
//	}
//
//	public void deleteFile(String imageUrl){
//		int idx = imageUrl.indexOf("/", 10);   // db에 저장된 url에서 버킷 미만 주소 제거
//		String fileName = imageUrl.substring(idx + 1);
//		amazonS3.deleteObject(bucket, fileName);
//	}
//
//	public int deleteProductImage(String imageUrl) {
//
//		return productImageMapper.deleteProductImage(imageUrl);
//	}


	public String uploadImage(String uploadUri, MultipartFile file) throws IOException {
		String fileName = generateFileName(file);
		StringBuilder s3FileKeyStringBuilder = new StringBuilder()
				.append(uploadUri)
				.append("/")
				.append(fileName);
		String s3FileKey = String.valueOf(s3FileKeyStringBuilder);
		ObjectMetadata objMeta = new ObjectMetadata();
		objMeta.setContentLength(file.getInputStream().available());
		objMeta.setContentType(file.getContentType());
		amazonS3.putObject(s3Component.getBucketName(), s3FileKey, file.getInputStream(), objMeta);
		return fileName;
	}

	public String getImageUrl(String s3FileName) throws UnsupportedEncodingException {
		return URLDecoder.decode(amazonS3.getUrl(s3Component.getBucketName(), s3FileName).toString(), "utf-8");  // aws에서 반환한 url 강제 utf-8 decoding
	}

	public List<String> getAllImageUrls(String s3FileKeyPrefix) {
		List<String> imageUriList = new ArrayList<>();
		ListObjectsRequest listObject = new ListObjectsRequest();
		listObject.setBucketName(s3Component.getBucketName());
		listObject.setPrefix(s3FileKeyPrefix);
		ObjectListing objects = amazonS3.listObjects(listObject);
		do {
			objects = amazonS3.listObjects(objects.getBucketName(), objects.getPrefix());
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				String objectKey = objectSummary.getKey();
				if(objectKey.split(s3FileKeyPrefix)[1].split("/").length == 2) imageUriList.add(objectKey);
			}
			listObject.setMarker(objects.getNextMarker());
		} while (objects.isTruncated());
		return imageUriList;
	}

	public void deleteImage(String s3FileKey){
		amazonS3.deleteObject(s3Component.getBucketName(), s3FileKey);
	}

	private String generateFileName(MultipartFile file) {
		StringBuilder fileNameStringBuilder = new StringBuilder()
				.append(UUID.randomUUID())
				.append("-")
				.append(file.getOriginalFilename());
		return String.valueOf(fileNameStringBuilder);
	}
}
