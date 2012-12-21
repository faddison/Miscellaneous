package test;

import org.junit.runner.RunWith;

import org.junit.runners.Suite;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;



@RunWith(Suite.class)

@Suite.SuiteClasses(
{
	TestLogin.class, 
	TestPage.class,
	TestLogout.class
})

public class SeleniumTestSuite 
{
	
}