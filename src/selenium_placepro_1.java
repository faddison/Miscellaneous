

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class selenium_placepro_1 extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://placeprocanada.com/");
		selenium.start();
	}

	@Test
	public void testSelenium_placepro_1() throws Exception {
		selenium.open("/");
		selenium.click("link=Student Login");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=txtLogin", "faddison");
		selenium.type("id=txtPassword", "Placie1!");
		selenium.type("id=txtAccessCode", "ubcstudent");
		selenium.click("id=btnLogin");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
