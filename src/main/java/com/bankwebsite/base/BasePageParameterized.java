package com.bankwebsite.base;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BasePageParameterized {
	protected static WebDriver driver;
	
	/* When both browser and urls are provided as parameters
	
	@Parameters({"browser", "url"})
	@BeforeClass
	public void setup(String browser, String url) {
		switch (browser.toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			driver = new FirefoxDriver(options);
			break;
		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		default:
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}
		driver.get(url); 
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.manage().window().maximize();
	}
*/
	
	//When Env urls are configured in Envconfig util files and only browser is sent in parameters
	
	@Parameters({"browser"})
	@BeforeClass
	public void setup(String browser ) {
		switch (browser.toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			driver = new FirefoxDriver(options);
			break;
		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		default:
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.manage().window().maximize();
	}
	
	 @AfterClass
	    public void quitBrowser() {
	        if (driver != null) {
	            driver.quit(); 
	        }
	    }
	 
	 public String captureScreenshot(WebDriver driver, String testName) throws IOException {
		 // Converting web driver object to TakesScreenshot interface
		 TakesScreenshot screenshot= (TakesScreenshot)driver;
		 
		 // calling getScreenshotAs method to take the screenshot and specify the output type as file
		 
		 File filesource= screenshot.getScreenshotAs(OutputType.FILE);
		 File filepath = new File (System.getProperty("user.dir")+"//Screenshots//"+testName+".png"); 
		 //Save the screenshot using the FileUtils.copyFile method to copy the screenshot to a specified location on your local system		 
		 
		 FileUtils.copyFile(filesource,filepath);
		 return (System.getProperty("user.dir")+"//Screenshots//"+testName+".png");	 
	}
}



/*note: driver!=null is used to prevent NullPointerException
When the @AfterClass method runs, if for any reason the driver was not initialized in 
@BeforeClass (e.g., a failure in browser setup, or @BeforeClass didn’t run due to a skipped test), 
then driver will be null.
Calling driver.quit() directly without checking will cause a runtime error*/
