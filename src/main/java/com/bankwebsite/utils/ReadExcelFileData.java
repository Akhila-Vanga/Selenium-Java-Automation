
package com.bankwebsite.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ReadExcelFileData{

	public static FileInputStream inputStream;
	public static XSSFWorkbook workBook;
	public static XSSFSheet excelSheet;
	public static XSSFRow row;
	public static XSSFCell cell;

	/*public static String getCellValue(String fileName, String sheetname, int rowno, int cellno) throws IOException {
		
		try {
			inputStream = new FileInputStream(fileName);
			workBook = new XSSFWorkbook(inputStream);
			excelSheet = workBook.getSheet(sheetname);
			row = excelSheet.getRow(rowno);
			cell = row.getCell(cellno);
			workBook.close();
			inputStream.close();
			return cell.getStringCellValue();
		} catch (Exception e) {
			return "error";
		}
	}
	*/
	public static String getCellValue(String fileName, String sheetname, int rowno, int cellno) throws IOException {
	    try {
	    	inputStream = new FileInputStream(fileName);
	         workBook = new XSSFWorkbook(inputStream); 
	    	excelSheet = workBook.getSheet(sheetname);
	        if (excelSheet == null) return "";

	        XSSFRow row = excelSheet.getRow(rowno);
	        if (row == null) return "";

	        XSSFCell cell = row.getCell(cellno);
	        if (cell == null) return "";

	        return cell.getStringCellValue();
	    } catch (Exception e) {
	        return "error";
	    }
	}
	
	public static int getRowCount(String fileName, String sheetname) throws IOException {

		try {
			inputStream = new FileInputStream(fileName);
			workBook = new XSSFWorkbook(inputStream);
			excelSheet = workBook.getSheet(sheetname);
			int rowcount=excelSheet.getLastRowNum()+1;
			workBook.close();
			inputStream.close();
			return rowcount;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int getColumnCount(String fileName, String sheetname) throws IOException {

		try {
			inputStream = new FileInputStream(fileName);
			workBook = new XSSFWorkbook(inputStream);
			excelSheet = workBook.getSheet(sheetname);
			//get total no. of column 
			int columncount= excelSheet.getRow(0).getLastCellNum();
			workBook.close();
			inputStream.close();
			return columncount;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/*public String getStringData(int sheetIndex,int row,int column)
	{
		return workBook.getSheetAt(sheetIndex).getRow(row).getCell(column).getStringCellValue();
	}
	*/
	public String getStringData(String sheetName,int row,int column)
	{
		return workBook.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();
	}
	
	public double getNumericData(String sheetName,int row,int column)
	{
		return workBook.getSheet(sheetName).getRow(row).getCell(column).getNumericCellValue();
	}

}
