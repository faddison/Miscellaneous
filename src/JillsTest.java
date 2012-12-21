

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

import com.DeathByCaptcha.AccessDeniedException;
import com.DeathByCaptcha.Captcha;
import com.DeathByCaptcha.Client;
import com.DeathByCaptcha.SocketClient;
import com.DeathByCaptcha.HttpClient;

public class JillsTest
{
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	@Before
	public void setUp() throws Exception {
		System.out.println("setup");
		//driver = new HtmlUnitDriver();
		//driver = new FirefoxDriver(new FirefoxProfile(new File("C:/Users/user/AppData/Roaming/Mozilla/Firefox/Profiles/8j8vcfem.default")));
		System.setProperty("webdriver.chrome.driver","D:\\Downloads\\libs\\chromedriver.exe"); 
		System.out.println(System.getProperty("webdriver.chrome.driver"));
		driver = new ChromeDriver();
		baseUrl = "http://www.jillsclickcorner.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	public void saveCaptcha() throws MalformedURLException, IOException
	{
		String captchaLink = driver.findElement(By.xpath("//img[@target='_blank']")).getAttribute("src");
		System.out.println(captchaLink);
		ImageIO.write(ImageIO.read(new URL(captchaLink)),"png", new File("captcha.png"));
	}
	
	public String solveCaptcha()
	{
		String answer = "";
		boolean solved = false;
		int attempts = 0;
		while (!solved && attempts < 3)
		{
			try
			{
				CaptchaSolver captchaSolver = new CaptchaSolver(new SocketClient(
						"trialaccount", "Cappie1!"), "./captcha.png");
				captchaSolver.run();
				Captcha captcha = captchaSolver.getCaptcha();
				answer = captcha.text;
				solved = true;
			}
			catch (Exception e)
			{
				attempts++;
			}
		}
		return answer;
	}
	
	@Test
	public void testJills() throws Exception {
		System.out.println("execute");
		driver.get(baseUrl + "/members/login.php");
		saveCaptcha();
		String answer = solveCaptcha();
		if (answer.isEmpty())
		{
			System.out.println("Unable to solve captcha.");
			tearDown();
		}
		
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("faddison");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("Jillie1!");
		driver.findElement(By.name("secCode")).clear();
		driver.findElement(By.name("secCode")).sendKeys(answer);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		//System.out.println(driver.getPageSource());
		//driver.get(baseUrl+"/members/index.php");
		//System.out.println(driver.getPageSource());
		/*
		driver.findElement(By.xpath("//tr[3]/td/a/b")).click();
		driver.findElement(By.xpath("//div[6]/a/b")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [selectFrame | timer | ]]
		new Select(driver.findElement(By.name("code"))).selectByVisibleText("ikoldh");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.linkText("Return to Jillsclickcorner.")).click();
		*/
	
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
