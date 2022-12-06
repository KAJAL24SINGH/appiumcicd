package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class SettingsPage extends BaseTest {
	TestUtils utils = new TestUtils();
	
	@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-LOGOUT\"]/android.widget.TextView") private MobileElement logoutBtn;
	
	public LoginPage pressLogoutBtn() { //It will navigate to login page so pass the object of login page
		utils.log().info("press Logout button");
		click(logoutBtn);
		return new LoginPage(); //return the page to which it will be navigated
	}

}
