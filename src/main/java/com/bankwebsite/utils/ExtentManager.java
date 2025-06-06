package com.bankwebsite.utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static ExtentReports extent;

    /*public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo("Framework", "Selenium TestNG");
            extent.setSystemInfo("QA", "Tester1");
        }
        return extent;
    }
    */
    
    //with Config reader
	public static ExtentReports getInstance() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/" + ConfigReader.get("report.path");
			ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath); 
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
			extent.setSystemInfo("Framework", "Selenium TestNG");
			extent.setSystemInfo("QA", "Tester1");
		}
		return extent;
	}

}
