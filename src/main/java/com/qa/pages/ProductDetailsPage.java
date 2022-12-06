package com.qa.pages;

import com.qa.BaseTest;
import com.qa.MenuPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductDetailsPage extends MenuPage {
	TestUtils utils = new TestUtils();
	@AndroidFindBy (xpath ="//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")private MobileElement SLBTitle;
	@AndroidFindBy (xpath ="//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]") private MobileElement SLBTxt;
	@AndroidFindBy (accessibility = "test-Price")private MobileElement SLBPrice;
	
	@AndroidFindBy (accessibility = "test-BACK TO PRODUCTS")private MobileElement backToProductsBtn;


	
	public String getSLBTitle() { //return type will be string
		utils.log().info("Title is " + SLBTitle);
		return getAttribute(SLBTitle,"text");
	}
	
	public String getSLBTxt() { //return type will be string
		utils.log().info("Text is " + SLBTxt);
		return getAttribute(SLBTxt,"text");
	}
	
	public String getSLBPrice() { //return type will be string
		utils.log().info("Price is " + SLBPrice);
		return getAttribute(SLBPrice,"text");
	}
	
	public ProductDetailsPage scrollToSLBPrice() {
		scrollToElement();
		return this;
	}
	
	public ProductsPage pressBackToProductsPage() { //creating method for clicking on backToProductsBtn so that it can be navigated to ProductsPage
		click(backToProductsBtn);
		return new ProductsPage();
	}
}
