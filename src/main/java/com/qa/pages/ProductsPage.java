package com.qa.pages;

import com.qa.BaseTest;
import com.qa.MenuPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsPage extends MenuPage {
	TestUtils utils = new TestUtils();
	@AndroidFindBy (xpath = "//android.widget.ScrollView[@content-desc=\"test-PRODUCTS\"]//preceding-sibling::android.view.ViewGroup//android.widget.TextView")
	private MobileElement productTitleTxt;
	
	@AndroidFindBy (xpath ="(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")private MobileElement SLBTitle;
	@AndroidFindBy (xpath ="(//android.widget.TextView[@content-desc=\"test-Price\"])[1]") private MobileElement SLBPrice;

	public String getTitle() { //return type will be string
		utils.log().info("Product page title is " + productTitleTxt);
		return getAttribute(productTitleTxt,"text");
	}
	
	public String getSLBTitle() { //return type will be string
		utils.log().info("Title is " + SLBTitle);
		return getAttribute(SLBTitle,"text");
	}
	
	public String getSLBPrice() { //return type will be string
		utils.log().info("Price is " + SLBPrice);
		return getAttribute(SLBPrice,"text");
	}
	
	public ProductDetailsPage pressSLBTitle() { //creating method for clicking on SLBTitle so that it can be navigated to ProductDetailsPage
		utils.log().info("Press SLB Title link ");
		click(SLBTitle);
		return new ProductDetailsPage();
	}
}
