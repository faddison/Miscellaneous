package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.junit.Before;
import org.junit.Test;

public class TestLogin extends SeleniumTest
{
	
	@Before
	public void setUp() throws Exception {
		//WebDriver driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", "D:/downloads/libs/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		String baseUrl = "http://parkingtester1.appspot.com/";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}
	
	@Test
	public void login() throws Exception 
	{
		String email = "";
		String password = "";
		selenium.open("/");
		selenium.waitForPageToLoad("30000");
		selenium.click("xpath=//*[@class='gwt-Anchor' and contains(text(), 'Sign')]");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=Email", email);
		selenium.type("id=Passwd", password);
		selenium.click("id=signIn");
		selenium.waitForPageToLoad("30000");
	}
}
