package com.qa.listeners;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import com.qa.BaseTest;
import com.qa.utils.TestUtils;

public class TestListener implements ITestListener{ //creating class which implements ITestListener from TestNG we can use methods from ITestListener class & customize as needed
    TestUtils utils = new TestUtils();
    
	public void onTestFailure(ITestResult result) { //this class describe the result of test, using this class we can get the exceptions thrown by our test methods
		if(result.getThrowable() != null) { //null check operation in case if their is no exception
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			result.getThrowable().printStackTrace(pw);//using getThrowable() method we are reading exception from test method then printing it to PrintWriter
			utils.log().info(sw.toString()); // using StringWriter to print stacktrace
		}
		
		BaseTest base = new BaseTest(); //initialize base class & get driver
		File file = base.getDriver().getScreenshotAs(OutputType.FILE); //collecting it in file object
		
		Map <String, String> params = new HashMap <String,String>(); //storing parameters in Hashmap
		params = result.getTestContext().getCurrentXmlTest().getAllParameters();
		
		String imagePath = "screenshots" + File.separator + params.get("platformName") + "_" + params.get("platformVersion") + "_"
		+ params.get("deviceName") + File.separator + base.getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName() + 
		File.separator + result.getName() + ".png" ;
	
		
		String completeImagePath = System.getProperty("user.dir") + File.separator + imagePath ; //this will give complete path using in html tag to add screenshot in village
		
		try {
		FileUtils.copyFile(file,new File(imagePath));//using file utility to copy file it will create this file in root directory
		Reporter.log("This is the sample screenshot"); //reporter is testng reporter class it is used to send testng logs to testng reports as well as screenshots
		Reporter.log("<a href ='" + completeImagePath + "'> <img src='" + completeImagePath + "' height='100' width='400'/></a>");
	     }catch(IOException e) {
		e.printStackTrace();
	     }
	}

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
}
