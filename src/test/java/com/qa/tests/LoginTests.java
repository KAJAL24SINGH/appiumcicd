package com.qa.tests;

import org.testng.annotations.Test;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class LoginTests extends BaseTest{ //creating at class level
	TestUtils utils = new TestUtils();
	LoginPage loginpage;
	ProductsPage productspage;
	JSONObject loginUsers;
	
	@BeforeClass
	  public void beforeClass() throws Exception {
		InputStream datais = null;
		try {
			String dataFileName = "data/loginUsers.json"; //creating string for file path
			datais = getClass().getClassLoader().getResourceAsStream(dataFileName); //read file using InputStream
			JSONTokener tokener = new JSONTokener(datais);//creating JSONTokener
			loginUsers = new JSONObject(tokener);//using tokener to get JSONObject
		}catch (Exception e) {
			e.printStackTrace(); //printing exception
			throw e;
		}finally {
			if (datais != null) { //doing null check then closing InputStream
				datais.close();
			}
		}
		closeApp();
		launchApp();
	  }

	  @AfterClass
	  public void afterClass() {
	  }
	  
	  @BeforeMethod
	  public void beforeMethod(Method m) { //before any test execute this method will initialize in login page before executing test cases
		  utils.log().info("Login before method");
		  loginpage = new LoginPage();
		  utils.log().info("\n" + " ******* Starting Test :" + m.getName() + "*******" + "\n") ;
	  }

	  @AfterMethod
	  public void afterMethod() {
		  utils.log().info("Login after method");
	  }
	  
	  @Test // Test method
	  public void invalidUserName() {
		  loginpage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username")); //adding abstraction layer to test data i.e using JSON file
		  loginpage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
		  loginpage.pressLoginBtn();
		  
		  String actualErrTxt = loginpage.getErrTxt();
		  String expectedErrTxt = getStrings().get("err_invalid_username_or_password");
		  utils.log().info("actual error txt - " + actualErrTxt + "\n" + "expected error txt - " + expectedErrTxt);
		  
		  Assert.assertEquals(actualErrTxt, expectedErrTxt);
	  }
	  
	  @Test
	  public void invalidPassword() {
		  loginpage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
		  loginpage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
		  loginpage.pressLoginBtn();
		  
		  String actualErrTxt = loginpage.getErrTxt();
		  String expectedErrTxt = getStrings().get("err_invalid_username_or_password");
		  utils.log().info("actual error txt - " + actualErrTxt + "\n" + "expected error txt - " + expectedErrTxt);
		  
		  Assert.assertEquals(actualErrTxt, expectedErrTxt);
	  }
	  
	  @Test
	  public void successfulLogin() {
		  loginpage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
		  loginpage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
		  productspage = loginpage.pressLoginBtn();
		  
		  String actualproductTitle = productspage.getTitle();
		  String expectedproductTitle =getStrings().get("product_title");
		  utils.log().info("actual title - " + actualproductTitle + "\n" + "expected title - " + expectedproductTitle);
		  
		  Assert.assertEquals(actualproductTitle, expectedproductTitle);
	  }

}
