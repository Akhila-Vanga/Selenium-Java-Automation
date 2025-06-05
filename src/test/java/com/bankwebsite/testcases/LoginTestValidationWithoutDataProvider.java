
//Login Page Validations without Data provider and returning output as single test case

package com.bankwebsite.testcases;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.bankwebsite.base.BasePageParameterized;
import com.bankwebsite.pages.LoginPage;
import com.bankwebsite.utils.EnvConfig;
import com.bankwebsite.utils.EnvConfigUtil;
import com.bankwebsite.utils.ExtentManager;

import java.io.*;

public class LoginTestValidationWithoutDataProvider extends BasePageParameterized {
	
	ExtentReports extent;
	ExtentTest test;
	
	@BeforeTest
	public void startReport() {
	    extent = ExtentManager.getInstance();
	}
	@AfterTest
    public void tearDown() {
    if (extent != null) {
        extent.flush(); } // write everything to the report file
    }

	public static FileInputStream fis;
	public static XSSFWorkbook workBook;
	public static XSSFSheet excelSheet;
	public static String filePath = "C:\\eclipse\\Workspace\\Guru99BankWebsiteAutomation\\src\\test\\resources\\TestData\\LoginTestData.xlsx";
	public static String sheetname = "TestCases";
	//public static GetTestCaseLogic tl = new GetTestCaseLogic();

	@Test
	public void getUserPwdDetails(ITestContext context) throws IOException, InterruptedException {

		fis = new FileInputStream(filePath);
		workBook = new XSSFWorkbook(fis);
		excelSheet = workBook.getSheet(sheetname);
		fis.close(); // Close input stream after reading
		String environment = context.getCurrentXmlTest().getParameter("Environment");
		EnvConfig config = EnvConfigUtil.getLoginConfig(environment);

		for (int i = 1; i <= excelSheet.getLastRowNum(); i++) {
			Row row = excelSheet.getRow(i);
			if (row == null) continue; // skip if row is null
			String testData = row.getCell(3).getStringCellValue();
			String username = extractValue(testData, "Username");
			String password = extractValue(testData, "Password");
			System.out.println(username);
			System.out.println(password);
			driver.get(config.getUrl());
			String result = VerifyLogin(username, password);
			System.out.println(result);
			Thread.sleep(3000);
			// Write result into column 5 (index 5)
			Cell statusCell = row.getCell(5);
			if (statusCell == null) {
				statusCell = row.createCell(5, CellType.STRING);
			}

		    Cell testDataCell = row.getCell(3); // assuming test data is here
		    if (testDataCell == null || testDataCell.getCellType() == CellType.BLANK) continue; // skip if empty

			statusCell.setCellValue(result);
			Thread.sleep(3000);
		}

		// Save changes back to file
		FileOutputStream fos = new FileOutputStream(filePath);
		workBook.write(fos);
		fos.close();
		workBook.close();
		System.out.println("Excel updated successfully.");
	}

	// Helper method to extract values from "Username=valid\nPassword=invalid"

	public static String extractValue(String input, String key) {
		for (String pair : input.split("\\n")) {
			if (pair.trim().toLowerCase().startsWith(key.toLowerCase() + "=")) {
				return pair.split("=")[1].trim();
			}
		}
		return "";
	}
	
	
	public String VerifyLogin(String userNme, String userPwd) throws InterruptedException, IOException {
		test = extent.createTest("Login Test for: " + userNme); 
	    LoginPage lp = new LoginPage(driver);
	    lp.loginToPortal(userNme, userPwd);
	    Thread.sleep(3000);

	    try {
	        // First: check and handle alert
	        Alert alert = driver.switchTo().alert();
	        String alertText = alert.getText();
	        alert.accept(); 
	        if (alertText.contains("User is not valid")) {
                test.pass("Login successful for user: " + userNme + " - Alert: " + alertText);
                return "Passed";
            } else {
                String screenshotPath = captureScreenshot(driver,userNme);
                test.fail("Unexpected alert: " + alertText).addScreenCaptureFromPath(screenshotPath);
                return "Failed";
            }

	    } catch (NoAlertPresentException e) {
	        // No alert present, safe to get title
	        String title = driver.getTitle();        
	        if (title.contains("GTPL Bank Manager HomePage")) {
	            test.pass("Login successful for user: " + userNme + ". Title: " + title);
	            return "Passed";
	        } else {
	            String screenshotPath = captureScreenshot(driver,userNme);
	            test.fail("Unexpected page title for user: " + userNme + " - Title: " + title)
	                .addScreenCaptureFromPath(screenshotPath);
	            Assert.fail("Unexpected login page title: " + title);
	            return "Failed";
	        }
	    }
	}
}
