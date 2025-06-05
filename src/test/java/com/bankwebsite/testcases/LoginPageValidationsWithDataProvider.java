
/**
 * Automation Framework Summary:
 * 
 * - Automates login functionality for the Guru99 Bank web application.
 * - Executes test cases by reading data from an Excel sheet (LoginTestData.xlsx).
 * - Validates login attempts and writes the test execution status back to the Excel sheet.
 * - Generates detailed HTML Extent Reports for each test run.
 * - Captures and attaches screenshots for failed or unexpected outcomes.

 * Key Features:
 * 
 * -Data-Driven Testing: Dynamically reads login credentials from Excel using Apache POI.
 * -Environment Configuration: Supports flexible environment targeting (QA, Dev, Prod) via TestNG XML parameters.
 * -Reusable Page Objects: Encapsulates UI interactions using Page Object Model for maintainable test logic.
 * -Advanced Reporting: Integrates ExtentReports for detailed, interactive HTML reports with step-wise logging.
 * -Screenshot Capture: Automatically captures and attaches screenshots on test failure for debugging.
 * -Result Logging: Writes pass/fail status directly back to the Excel test case sheet for traceability.
 * -TestNG Integration: Leverages TestNG for test execution, parallelism, and result aggregation.
 **/

package com.bankwebsite.testcases;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.bankwebsite.base.BasePageParameterized;
import com.bankwebsite.pages.LoginPage;
import com.bankwebsite.utils.CaptureScreenshot;
import com.bankwebsite.utils.ConfigReader;
import com.bankwebsite.utils.EmailReportUtil;
import com.bankwebsite.utils.EnvConfig;
import com.bankwebsite.utils.EnvConfigUtil;
import com.bankwebsite.utils.ExtentManager;
import com.bankwebsite.utils.LogFolderUtil;

public class LoginPageValidationsWithDataProvider extends BasePageParameterized {
	
	 private static final Logger logger = LogManager.getLogger(LoginPageValidationsWithDataProvider.class);

	    // Example usage
	    public void Logdetails() {
	        logger.info("Starting login validation test...");
	        logger.error("This is a test error log");
	    }

    ExtentReports extent;
    ExtentTest test;
    //String filePath = "C:\\eclipse\\Workspace\\Guru99BankWebsiteAutomation\\src\\test\\resources\\TestData\\LoginTestData.xlsx";
    //String sheetName = "TestCases";
    
    String filePath = System.getProperty("user.dir") + "/" + ConfigReader.get("testdata.path");
    String sheetName = ConfigReader.get("testdata.sheet");
    
    CaptureScreenshot cs = new CaptureScreenshot();

    @BeforeSuite
    public void startReport() {
    	LogFolderUtil.createLogFolder(); 
        extent = ExtentManager.getInstance(); // // Initializes the report only once
    }

    @AfterSuite
    public void tearDown() {
        if (extent != null) {
            extent.flush();
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            EmailReportUtil.sendReport("recipient@example.com", reportPath);
            }
    } 

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum();
        int colCount = sheet.getRow(0).getLastCellNum();
        Object[][] data = new Object[rowCount][2]; // 2 columns with username, password only
        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Cell testDataCell = row.getCell(3); // Since Test data is in 4th column (index 3)
            if (testDataCell == null || testDataCell.getCellType() == CellType.BLANK) continue;
            String testData = testDataCell.getStringCellValue();
            String username = extractValue(testData, "Username");
            String password = extractValue(testData, "Password");
            data[i - 1][0] = username;
            data[i - 1][1] = password;
        }
        workbook.close();
        fis.close();
        return data;
    }

    @Test(dataProvider = "loginData")
    public void testLoginWithMultipleCredentials(String userName, String userPwd, ITestContext context) throws InterruptedException, IOException {
        String environment = context.getCurrentXmlTest().getParameter("Environment");
        EnvConfig config = EnvConfigUtil.getLoginConfig(environment);
        test = extent.createTest("Login Test for user: " + userName);
        test.info("Navigating to URL: " + config.getUrl());
        test.info("Using credentials - Username: " + userName + ", Password: " + userPwd);
        driver.get(config.getUrl());
        LoginPage lp = new LoginPage(driver);
        lp.loginToPortal(userName, userPwd);
        Thread.sleep(2000); 

        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();

            if (alertText.contains("User is not valid")) {
                test.pass("Login successful for user: " + userName + " - Alert: " + alertText);
            } else {
                //String screenshotPath = captureScreenshot(driver, userName);
            	String screenshotPath = cs.captureScreenshot(driver, userName);
                test.fail("Unexpected alert: " + alertText).addScreenCaptureFromPath(screenshotPath);
                Assert.fail("Unexpected alert: " + alertText);
            }

        } catch (NoAlertPresentException e) {
            String title = driver.getTitle();
            if (title.contains("GTPL Bank Manager HomePage")) {
            	test.pass("Login successful for user: " + userName + ". Title: " + title);
            } else {
                //String screenshotPath = captureScreenshot(driver, userName);
                String screenshotPath = cs.captureScreenshot(driver, userName);
                test.fail("Unexpected page title: " + title).addScreenCaptureFromPath(screenshotPath);
                Assert.fail("Unexpected page title: " + title);
            }
        }
    }

    // Helper method to extract Username and Password from multi-line string (Username=" " \n Password =" ")
    
    public static String extractValue(String input, String key) {
        for (String pair : input.split("\\n")) {
            if (pair.trim().toLowerCase().startsWith(key.toLowerCase() + "=")) {
                return pair.split("=")[1].trim();
            }
        }
        return "";
    }
}
