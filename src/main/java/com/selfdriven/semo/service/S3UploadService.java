package com.selfdriven.semo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.selfdriven.semo.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3UploadService {

	private final AmazonS3 amazonS3;
	private final S3Config s3Config;

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
		amazonS3.putObject(s3Config.getBucketName(), s3FileKey, file.getInputStream(), objMeta);
		return fileName;
	}

//	public String getImageUrl(String s3FileName) throws UnsupportedEncodingException {
//		return URLDecoder.decode(amazonS3.getUrl(s3ComponentTest.getBucketName(), s3FileName).toString(), "utf-8");  // aws에서 반환한 url 강제 utf-8 decoding
//	}

	public List<String> getAllImageUrls(String s3FileKeyPrefix) {
		List<String> imageUriList = new ArrayList<>();
		ListObjectsRequest listObject = new ListObjectsRequest();
		listObject.setBucketName(s3Config.getBucketName());
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
		amazonS3.deleteObject(s3Config.getBucketName(), s3FileKey);
	}

	private String generateFileName(MultipartFile file) {
		StringBuilder fileNameStringBuilder = new StringBuilder()
				.append(UUID.randomUUID())
				.append("-")
				.append(file.getOriginalFilename());
		return String.valueOf(fileNameStringBuilder);
	}
}
