
package com.bankwebsite.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.bankwebsite.base.BasePageParameterized;

public class LoginPage extends BasePageParameterized {
	
	WebDriver driver;
	
	//creating constructor
	
	public LoginPage(WebDriver InstDriver) {
		this.driver=InstDriver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(linkText="Bank Project") WebElement BankProject;
	@FindBy(name="uid") WebElement UserID ;
	@FindBy(name="password") WebElement Password ;
	@FindBy(xpath="//input[@type='submit']") WebElement loginbutton;
	@FindBy(css="input[type='reset']") WebElement resetbutton;
	
	public void loginToPortal(String username, String password) {
		// TODO Auto-generated method stub
		BankProject.click();
		UserID.sendKeys(username);
		Password.sendKeys(password);
		loginbutton.click();	
	}
	
	
}