
//Simple User Configurable LoginPage Validations from EnvConfig.java util page

package com.bankwebsite.testcases;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.bankwebsite.base.BasePageParameterized;
import com.bankwebsite.pages.LoginPage;
import com.bankwebsite.utils.EnvConfig;
import com.bankwebsite.utils.EnvConfigUtil;

public class SimpleLoginTest extends BasePageParameterized {

	@Parameters({ "Environment" })
	@Test
	public void verifyLogin(String environment) {
	
		// Get config based on env
		EnvConfig config = EnvConfigUtil.getLoginConfig(environment);
		// Open the login page
		driver.get(config.getUrl());
		// Login with provided credentials
		LoginPage lp = new LoginPage(driver);
		lp.loginToPortal(config.getUsername(), config.getPassword());
	}
}



