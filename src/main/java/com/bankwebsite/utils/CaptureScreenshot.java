package com.bankwebsite.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class CaptureScreenshot {
	
    //With config reader

	public static String captureScreenshot(WebDriver driver, String testName) throws IOException {
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File source = screenshot.getScreenshotAs(OutputType.FILE);
		String folderPath = System.getProperty("user.dir") + "/" + ConfigReader.get("screenshot.folder");
		File dir = new File(folderPath);
		if (!dir.exists())
			dir.mkdirs();
		String filePath = folderPath + File.separator + testName + "_" + System.currentTimeMillis() + ".png";
		FileUtils.copyFile(source, new File(filePath));
		return filePath;
	}

	/*	public static String captureScreenshot(WebDriver driver,String userName) throws IOException {
	    File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    String destPath = "test-output/screenshots/" + userName + "_" + System.currentTimeMillis() + ".png";
	    File dest = new File(destPath);
	    FileUtils.copyFile(src, dest);
	    return dest.getAbsolutePath();
	}
	*/
	    
	
	public static void takeScreenshot(WebDriver driver, String filename) {
		try {
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File dest = new File("screenshots/" + filename + ".png");
			// Create directory if not exists
			dest.getParentFile().mkdirs();
			Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Screenshot saved: " + dest.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
