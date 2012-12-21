import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleneseTestCase;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public class parkingapptest extends SeleneseTestCase {
	
	WebDriver driver;
	@Before
	public void setUp() throws Exception {
		//WebDriver driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", "D:/downloads/libs/chromedriver.exe");
		driver = new ChromeDriver();
		String baseUrl = "http://parkingtester1.appspot.com/";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}

	//*[@id="nameFieldContainer"]/table[2]/tbody/tr/td[1]/div
	
	@Test
	public void testParkingapptest() throws Exception {
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
		
		/*
		selenium.click("xpath=//*[@class='gwt-Label' and contains(text(),'STATISTICS')]");
		//"[@class='gwt-PopupPanel']"
		Thread.sleep(2000);
		
		String parentWindowHandle = driver.getWindowHandle(); // save the current window handle.
		System.out.println(parentWindowHandle);    
		WebDriver popup = null;
	      Set<String> windowIterator = driver.getWindowHandles();
	     for (String windowHandle: windowIterator) 
	     { 
	        System.out.println(windowHandle);
	      }
		
		throw new Exception();
		//selenium.click("xpath=//*[@id=\"nameFieldContainer\"]/table[2]/tbody/tr/td[1]/div");
		// /html/body/div[2]/div/table/tbody/tr[2]/td/button
		
		*/
		
		/*
		selenium.click("css=div.gwt-Label.underlined");
		selenium.click("xpath=(//button[@type='button'])[7]");
		selenium.click("css=div.gwt-Label.underlined");
		selenium.click("xpath=(//button[@type='button'])[8]");
		selenium.click("css=div.gwt-Label.underlined");
		selenium.click("xpath=(//button[@type='button'])[7]");
		selenium.click("css=div.gwt-Label.underlined");
		selenium.click("xpath=(//button[@type='button'])[7]");
		*/
		
		/*
		selenium.click("css=div.latlngStyle > button.gwt-Button");
		selenium.click("xpath=/html/body/table/tbody/tr[2]/td/div[2]/button");
		//selenium.click("xpath=//*[@class='gwt-Button' and contains(text(),'Directions')]");
		selenium.waitForPageToLoad("30000");
		*/
		Thread.sleep(1000);
		selenium.click("//td[@id='nameFieldContainer']/div/div[2]/div/div[5]/div/div");
		Thread.sleep(1000);
		selenium.click("//td[@id='nameFieldContainer']/div/div[2]/div/div[4]/div/div");
		Thread.sleep(1000);
		selenium.click("//td[@id='nameFieldContainer']/div/div[2]/div/div[3]/div/div");
		Thread.sleep(1000);
		selenium.click("//td[@id='nameFieldContainer']/div/div[2]/div/div[2]/div/div");
		Thread.sleep(1000);
		selenium.click("css=div.gwt-TabLayoutPanelTabInner > div.gwt-Label");
		Thread.sleep(1000);
		selenium.click("link=Sign Out");
		selenium.waitForPageToLoad("30000");
		Thread.sleep(1000);
		
		
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
