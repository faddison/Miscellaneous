package test;

import org.junit.Test;
import org.openqa.selenium.WebDriverBackedSelenium;

public class TestPage extends SeleniumTest
{
	@Test
	public void test1() throws Exception
	{
		selenium.click("//td[@id='nameFieldContainer']/div/div[2]/div/div[5]/div/div");
		Thread.sleep(1000);
	}

	@Test
	public void test2() throws Exception
	{
		selenium.click("//td[@id='nameFieldContainer']/div/div[2]/div/div[4]/div/div");
		Thread.sleep(1000);
	}
	
	@Test
	public void test3() throws Exception 
	{
		selenium.click("//td[@id='nameFieldContainer']/div/div[2]/div/div[3]/div/div");
		Thread.sleep(1000);
	}

	@Test
	public void test4() throws Exception 
	{
		selenium.click("//td[@id='nameFieldContainer']/div/div[2]/div/div[2]/div/div");
		Thread.sleep(1000);
	}

	@Test
	public void test5() throws Exception 
	{
		selenium.click("css=div.gwt-TabLayoutPanelTabInner > div.gwt-Label");
		Thread.sleep(1000);
	}
	
}
