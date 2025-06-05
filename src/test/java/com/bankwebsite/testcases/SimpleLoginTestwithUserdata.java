// LoginPage Validations with inputing Just username and password from excel file.

package com.bankwebsite.testcases;

import java.io.IOException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.bankwebsite.base.BasePageParameterized;
import com.bankwebsite.pages.LoginPage;
import com.bankwebsite.utils.CaptureScreenshot;
import com.bankwebsite.utils.EnvConfig;
import com.bankwebsite.utils.EnvConfigUtil;
import com.bankwebsite.utils.ExtentManager;
import com.bankwebsite.utils.ReadExcelFileData;

public class SimpleLoginTestwithUserdata extends BasePageParameterized {


	String fileName=System.getProperty("user.dir")+"\\src\\test\\resources\\TestData\\LoginTestData.xlsx";
	String sheetname="LogInData";
	ExtentReports extent;
	ExtentTest test;
	//CaptureScreenshot cs = new CaptureScreenshot();

	@BeforeTest
	public void startReport() {
	    extent = ExtentManager.getInstance();
	}
	@AfterTest
    public void tearDown() {
    if (extent != null) {
        extent.flush();  // write everything to the report file
    }
}

	/*@Test(priority = 1, dataProvider = "LoginDataProvider")
	public void VerifyLogin(String userNme, String userPwd, ITestContext context) throws InterruptedException {
	    String environment = context.getCurrentXmlTest().getParameter("Environment");
	    EnvConfig config = EnvConfigUtil.getLoginConfig(environment);
	    driver.get(config.getUrl());
	    LoginPage lp = new LoginPage(driver);
	    lp.loginToPortal(userNme, userPwd);
	    Thread.sleep(3000);
	}*/
	
	@Test(priority = 1, dataProvider = "LoginDataProvider")
	public void VerifyLogin(String userName, String userPwd, ITestContext context) throws InterruptedException, IOException {
		test = extent.createTest("Login Test for user: " + userName);
		String environment = context.getCurrentXmlTest().getParameter("Environment");
	    EnvConfig config = EnvConfigUtil.getLoginConfig(environment);
	    test = extent.createTest("Login Test for user: " + userName);
	    driver.get(config.getUrl());
	    LoginPage lp = new LoginPage(driver);
	    lp.loginToPortal(userName, userPwd);
	    Thread.sleep(2000); // just for demo delay, avoid in real-world

	    /* try catch with report logging and without extent reports
	     
	     try {
	       
	        Alert alert = driver.switchTo().alert();
	        String alertText = alert.getText();
	        alert.accept();
	       
	       if (alertText.contains("User is not valid")) {
	            Reporter.log("Test PASSED for User: " + userName + " - Got expected alert: " + alertText, true);
	        } else {
	            Reporter.log("Test FAILED for User: " + userName + " - Unexpected alert: " + alertText, true);
	            Assert.fail("Unexpected alert: " + alertText);
	        }
	        
	      catch (NoAlertPresentException e) {

	        String actualTitle = driver.getTitle();
	       
	        if (actualTitle.contains("GTPL Bank Manager HomePage")) {
	            Reporter.log("Test PASSED for User: " + userName + " - Login successful. Title: " + actualTitle, true);
	        } else {
	            Reporter.log("Test FAILED for User: " + userName + " - Unexpected title: " + actualTitle, true);
	            Assert.fail("Unexpected login page title: " + actualTitle);
	        }
	    }
	    
	    */
	        
	    try {
	            Alert alert = driver.switchTo().alert();
	            String alertText = alert.getText();
	            alert.accept();

	            if (alertText.contains("User is not valid")) {
	                test.pass("Login successful for user: " + userName + " - Alert: " + alertText);
	            } else {
	                String screenshotPath = captureScreenshot(driver,userName);
	                test.fail("Unexpected alert: " + alertText).addScreenCaptureFromPath(screenshotPath);
	            }

	    }
	    
	    catch (NoAlertPresentException e) {
	        String actualTitle = driver.getTitle();
	        if (actualTitle.contains("GTPL Bank Manager HomePage")) {
	            test.pass("Login successful for user: " + userName + ". Title: " + actualTitle);
	        } else {
	            String screenshotPath = captureScreenshot(driver,userName);
	            test.fail("Unexpected page title for user: " + userName + " - Title: " + actualTitle)
	                .addScreenCaptureFromPath(screenshotPath);
	            Assert.fail("Unexpected login page title: " + actualTitle);
	        }
	    }
	}
	
	@DataProvider(name="LoginDataProvider")
	public String[][] LoginDataProvider() throws IOException{
		int noofrows = ReadExcelFileData.getRowCount(fileName, sheetname);
		int noofcolms= ReadExcelFileData.getColumnCount(fileName, sheetname);
		
		String data [] [] = new String[noofrows-1][noofcolms] ;
		
		for (int i=1;i<noofrows;i++) {
			for (int j=0;j<noofcolms;j++) {
				data[i-1][j]=ReadExcelFileData.getCellValue(fileName, sheetname, i, j);
				
			}			
		}
		
		return data;
		}

}

/*Note: context : This is an instance of ITestContext, which is automatically provided by TestNG.

	It contains information about the current test run — like test name, parameters, results, etc.

	Think of it as TestNG's way of letting you access test configuration at runtime.

	2. context.getCurrentXmlTest()
	This gets the current <test> tag from your testng.xml file.
	It gives access to things defined in this section:

	<test name="Test">
	  <parameter name="Environment" value="qa" />
	</test>
	
	So now you're inside that <test> context.

	3. .getParameter("Environment")
	This retrieves the value of the parameter named "Environment" that you defined in the XML.

	<parameter name="Environment" value="qa" />
	will return "qa".

So putting it all together:

	String environment = context.getCurrentXmlTest().getParameter("Environment");
	This line means: "Get me the value of the Environment parameter from the current <test> in testng.xml."

Why It's Useful: You avoid hardcoding values like "qa", "prod", etc.

	You can change the environment for different test runs just by editing testng.xml or passing from CI/CD tools.

	It makes your test logic flexible, clean, and scalable.
*/
