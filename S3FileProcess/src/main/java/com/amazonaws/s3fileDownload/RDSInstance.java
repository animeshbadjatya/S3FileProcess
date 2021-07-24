/**
 * 
 */
package com.amazonaws.s3fileDownload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.CreateDBInstanceRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.Endpoint;

/**
 * @author animesh.badjatya
 *
 */
public class RDSInstance {

	public void createDB(AmazonRDS amazonRDS) {
		CreateDBInstanceRequest createDBInstanceRequest = new CreateDBInstanceRequest();
		createDBInstanceRequest.setDBInstanceIdentifier("NumberOne");
		createDBInstanceRequest.setDBInstanceClass("db.t2.micro");
		createDBInstanceRequest.setEngine("mysql");
		createDBInstanceRequest.setMultiAZ(false);
		createDBInstanceRequest.setMasterUsername("username");
		createDBInstanceRequest.setMasterUserPassword("password");
		createDBInstanceRequest.setDBName("ExcelDataDB");
		createDBInstanceRequest.setStorageType("gp2");
		createDBInstanceRequest.setAllocatedStorage(10);

		amazonRDS.createDBInstance(createDBInstanceRequest);
	}

	public void listRdsInstances(AmazonRDS amazonRDS) {
		DescribeDBInstancesResult result = amazonRDS.describeDBInstances();
		List<DBInstance> instances = result.getDBInstances();
		for (DBInstance instance : instances) {
			// Information about each RDS instance
			String identifier = instance.getDBInstanceIdentifier();
			String engine = instance.getEngine();
			String status = instance.getDBInstanceStatus();
			Endpoint endpoint = instance.getEndpoint();

			System.out.println("DataBase Identifier: " + identifier);
			System.out.println("Database engine: " + engine);
			System.out.println("Database status: " + status);
			System.out.println("Database endpoint: " + endpoint);
		}
	}

	public Connection connectToRDS(AmazonRDS amazonRDS) throws IOException, SQLException, ClassNotFoundException {
//		String propFileName = "db.properties";
//		Properties prop = new Properties();
//		
//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
//		if (inputStream != null) {
//			prop.load(inputStream);
//		} else {
//			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
//		}
//		String db_identifier = prop.getProperty("db_identifier");
//		String db_username = prop.getProperty("db_username");
//		String db_password = prop.getProperty("db_password");
//		String db_database = prop.getProperty("db_database");

		String db_identifier = "jdbc:mysql:aws://numbertwo.cwghtlxlqyeo.us-west-2.rds.amazonaws.com:3306";
		String db_username = "admin";
		String db_password = "password";
		String db_database = "numbertwo";

		System.out.println("Loading driver...");
		// Class.forName("com.mysql.jdbc.Driver");
		Class.forName("software.aws.rds.jdbc.Driver");
		Connection connection = DriverManager.getConnection(db_identifier, db_username, db_password);

		return connection;
	}

}
