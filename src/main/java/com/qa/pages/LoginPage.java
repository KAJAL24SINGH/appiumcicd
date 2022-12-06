package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy; //it is only available for Appium 

public class LoginPage extends BaseTest {
	TestUtils utils = new TestUtils();
	@AndroidFindBy (accessibility = "test-Username")private MobileElement usernameTxtField;
	@AndroidFindBy (accessibility = "test-Password")private MobileElement passwordTxtField;
	@AndroidFindBy (accessibility = "test-LOGIN")private MobileElement loginBtn;
	@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")private MobileElement ErrorTxt;
	  
	public LoginPage enterUserName (String username) {
		utils.log().info("login with " + username);
		sendKeys(usernameTxtField , username);
		return this; //POM works in a flow u r on a page & u r performing some action on that page if that action is taking 
	}   //u to some other page then we need to return object of that page in this way no need to specifically initialize that particular page 
		//Here we are entering username & not navigating to any other page so we are returning object of the same class
		
	public LoginPage enterPassword (String password) {
		utils.log().info("login with " + password);
		sendKeys(passwordTxtField , password);
		return this;
	}
		
	public ProductsPage pressLoginBtn () {
		utils.log().info("press login button");
		click(loginBtn);
		return new ProductsPage();
	}
	
	public ProductsPage login(String username , String password) { //we are using login functionality as prerequisite step 
		enterUserName(username);
		enterPassword(password);
		return pressLoginBtn();
	}
	
	public String getErrTxt() { //return type will be string
		utils.log().info("Error Text is - " + ErrorTxt);
		return getAttribute(ErrorTxt,"text");
	}
}
