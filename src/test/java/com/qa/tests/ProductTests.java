package com.qa.tests;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
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

public class ProductTests extends BaseTest{ //creating at class level
	TestUtils utils = new TestUtils();
	LoginPage loginpage;
	ProductsPage productspage;
	SettingsPage settingspage;
	ProductDetailsPage productDetailsPage;
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
		  loginpage = new LoginPage();
		  utils.log().info("\n" + " ******* Starting Test :" + m.getName() + "*******" + "\n") ;
		  
		  productspage = loginpage.login(loginUsers.getJSONObject("validUser").getString("username"),
				  loginUsers.getJSONObject("validUser").getString("password"));
		  
	  }

	  @AfterMethod
	  public void afterMethod() {
		  settingspage = productspage.pressSettingsBtn();
		  loginpage = settingspage.pressLogoutBtn();
	  }
	  
	  @Test // Test method
	  public void validateProductOnProductsPage() {
		  SoftAssert sa = new SoftAssert(); //It is asserts which continue the execution even after the Assert condition fails.
		  
		  String SLBTitle = productspage.getSLBTitle();
		  sa.assertEquals(SLBTitle, getStrings().get("products_page_slb_title"));
		  
		  String SLBPrice = productspage.getSLBPrice();
		  sa.assertEquals(SLBPrice, getStrings().get("products_page_slb_price"));
		  
		  sa.assertAll();
	  }
	  
	  @Test // Test method
	  public void validateProductOnProductDetailsPage() {
		  SoftAssert sa = new SoftAssert(); //It is asserts which continue the execution even after the Assert condition fails.
		  
		  productDetailsPage = productspage.pressSLBTitle();
		  
		  productDetailsPage.scrollToSLBPrice();
		  
		  String SLBTitle = productDetailsPage.getSLBTitle();
		  sa.assertEquals(SLBTitle, getStrings().get("product_details_page_slb_title"));
		  
		  String SLBTxt = productDetailsPage.getSLBTxt();
		  sa.assertEquals(SLBTxt, getStrings().get("product_details_slb_txt"));
		  
		  String SLBPrice = productDetailsPage.getSLBPrice();
		  sa.assertEquals(SLBPrice, getStrings().get("products_details_slb_price"));
		  
		  //productspage = productDetailsPage.pressBackToProductsPage();
		  
		  sa.assertAll();
	  }
}
