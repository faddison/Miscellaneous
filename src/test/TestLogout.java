package test;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.testng.annotations.AfterTest;

public class TestLogout extends SeleniumTest
{
	@Test
	public void testLogout() throws Exception
	{
		selenium.click("link=Sign Out");
		selenium.waitForPageToLoad("30000");
		Thread.sleep(1000);
	}
	
	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
	
}
