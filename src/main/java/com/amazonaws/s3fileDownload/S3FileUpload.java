/**
 * @author animesh.badjatya
 */
package com.amazonaws.s3fileDownload;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class S3FileUpload {
	/*
	 * Upload an object to your bucket - You can easily upload a file to S3, or
	 * upload directly an InputStream if you know the length of the data in the
	 * stream. You can also specify your own metadata when uploading to S3, which
	 * allows you set a variety of options like content-type and content-encoding,
	 * plus additional metadata specific to your applications.
	 */
	public PutObjectResult upload(String bucketName, String key, AmazonS3 s3) {

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/vnd.ms-excel");

		File f = new File("Book4.xlsx");
		System.out.println("Uploading a new object to S3 from a file\n");

		PutObjectRequest putObjectReq = new PutObjectRequest(bucketName, key, f).withMetadata(metadata);

		PutObjectResult putObjectResult = s3.putObject(putObjectReq);

		return putObjectResult;

	}

}
