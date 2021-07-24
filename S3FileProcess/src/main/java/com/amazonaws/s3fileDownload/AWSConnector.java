/**
 * 
 */
package com.amazonaws.s3fileDownload;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @author animesh.badjatya
 *
 */
public class AWSConnector {

	private AWSCredentials connect() {
		/*
		 * * The ProfileCredentialsProvider will return your [csops/admin-full]
		 * credential profile by reading from the credentials file located at
		 * (/Users/animesh.badjatya/.aws/credentials).
		 */
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("csops/admin-full").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Users/animesh.badjatya/.aws/credentials), and is in valid format.", e);
		}
		return credentials;

	}

	public AmazonS3 awsS3Connect() {

		AWSCredentials credentials = connect();
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion("us-west-2").build();

		System.out.println("===========================================");
		System.out.println("Getting Started with Amazon S3");
		System.out.println("===========================================\n");

		return s3;
	}

	public AmazonRDS awsRDSConnect() {

		AWSCredentials credentials = connect();

		AmazonRDS amazonRDS = AmazonRDSClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("us-west-2").build();

		System.out.println("===========================================");
		System.out.println("Getting Started with Amazon RDS");
		System.out.println("===========================================\n");

		return amazonRDS;
	}

}
