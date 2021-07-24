/**
 * 
 */
package com.amazonaws.s3fileDownload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * @author animesh.badjatya
 *
 */
public class S3FileDownload {

	private Connection connection = null;

	/**
	 * Displays the contents of the specified input stream as text.
	 *
	 * @param s3ObjectInputStream The input stream to display as text.
	 *
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void displayTextInputStream(InputStream input, AmazonRDS amazonRDS)
			throws IOException, SQLException, ClassNotFoundException {

		if (connection == null) {
			connection = new RDSInstance().connectToRDS(amazonRDS);
			System.out.println("Connected");

		}
		String Db = "USE numbertwo";
		connection.createStatement().executeQuery(Db).close();

		int batchSize = 20;
		try {
			// creating Workbook instance that refers to .xlsx file
			XSSFWorkbook wb = new XSSFWorkbook(input);
			XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
			String tableName = sheet.getSheetName();

			System.out.println("table name " + tableName);
			String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "1"
					+ "(id SERIAL PRIMARY KEY, Name VARCHAR(80), Interest Integer, Principle Integer, Time Integer, Date Date);";
			String insert = "INSERT INTO " + tableName + "1" + " VALUES (?,?,?,?,?,?)";

			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement(insert);

			int count = 0;
			Iterator<Row> rowIterator = sheet.iterator();

			rowIterator.next(); // skip the header row

			while (rowIterator.hasNext()) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();

					int columnIndex = nextCell.getColumnIndex();

					switch (columnIndex) {
					case 0:
						int id = (int) nextCell.getNumericCellValue();
						statement.setInt(1, id);
						break;
					case 1:
						String name = nextCell.getStringCellValue();
						statement.setString(2, name);
						break;
					case 2:
						int interest = (int) nextCell.getNumericCellValue();
						statement.setInt(3, interest);
						break;
					case 3:
						int principle = (int) nextCell.getNumericCellValue();
						statement.setInt(4, principle);
						break;
					case 4:
						int time = (int) nextCell.getNumericCellValue();
						statement.setInt(5, time);
						break;
					case 5:
						Date date = (Date) nextCell.getDateCellValue();
						statement.setDate(6, new java.sql.Date(date.getTime()));
					}
				}

				statement.addBatch();

				if (count % batchSize == 0) {
					statement.executeBatch();
				}

			}

			wb.close();
			input.close();

			connection.commit();
			connection.close();

			System.out.println("===========================================");
			System.out.println("Data Trasnferred to RDS");
			System.out.println("===========================================\n");
		} catch (IOException ex1) {
			System.out.println("Error reading file");
			ex1.printStackTrace();
		} catch (SQLException ex2) {
			System.out.println("Database error");
			ex2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void download(String bucketName, String key, AmazonS3 s3, AmazonRDS amazonRDS)
			throws IOException, SQLException, ClassNotFoundException {
		System.out.println("Downloading an object");
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());

		displayTextInputStream(object.getObjectContent(), amazonRDS);
	}

}
