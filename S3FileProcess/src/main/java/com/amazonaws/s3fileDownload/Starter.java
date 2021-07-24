/**
 * 
 */
package com.amazonaws.s3fileDownload;

import java.io.IOException;
import java.sql.SQLException;

import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.s3.AmazonS3;

/**
 * @author animesh.badjatya s
 */
public class Starter {

	/**
	 * @param args
	 */
	public static void main(String args[]) {

		AmazonS3 s3 = new AWSConnector().awsS3Connect();
		AmazonRDS amazonRDS = new AWSConnector().awsRDSConnect();

		String bucketName = "testbucketforuploaddelphix";
		String key = "Book5-test.xls";

		// new S3FileUpload().upload(bucketName.trim(),key, s3);
		try {
			new S3FileDownload().download(bucketName.trim(), key, s3, amazonRDS);
		} catch (IOException e) {
			System.out.print("Error in Catch" + e);
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// new RDSInstance().listRdsInstances(amazonRDS);
	}

}
