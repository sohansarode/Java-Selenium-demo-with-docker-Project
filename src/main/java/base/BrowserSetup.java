package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.URL;

import utils.ConfigReader;
import utils.ReportUtils;

public class BrowserSetup extends ReportUtils {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static boolean reuseBrowser;
	private static boolean useSeleniumGrid;

	public static WebDriver getDriver() {
		return driver.get();
	}

	// -----------------------------------------------------------------------------------------------------------
	// //

	@BeforeSuite(alwaysRun = true)
	public void globalConfig() throws Exception {
		ConfigReader.Read_Property_File();
		reuseBrowser = "yes"
				.equalsIgnoreCase(System.getProperty("reuseBrowser", ConfigReader.prop.getProperty("reuseBrowser")));
		useSeleniumGrid = "yes".equalsIgnoreCase(System.getProperty("useSeleniumGrid", ConfigReader.prop.getProperty("useSeleniumGrid")));
	}

	@BeforeMethod(alwaysRun = true)
	public void setUp(Method method) throws Exception {
		if (!reuseBrowser) {
			launchBrowser(method.getName());
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() throws Exception {
		if (!reuseBrowser) {
			closeBrowser();
		}
	}

	@BeforeClass(alwaysRun = true)
	public void classSetUp() throws Exception {
		if (reuseBrowser) {
			launchBrowser(this.getClass().getSimpleName());
		}
	}

	@AfterClass(alwaysRun = true)
	public void classTearDown() throws Exception {
		if (reuseBrowser) {
			closeBrowser();
		}
	}

	// -----------------------------------------------------------------------------------------------------------
	// //

	@SuppressWarnings("deprecation")
	private void launchBrowser(String testName) throws Exception {
		// Start recording
		if ("yes".equalsIgnoreCase(ConfigReader.prop.getProperty("Recording"))) {
			My_Screen_Recorder.Start_Recording("Test_" + testName + "_" + System.currentTimeMillis());
		}

		String environment = System.getProperty("environment", ConfigReader.prop.getProperty("Environment"));
		String browser;
		boolean headless = Boolean
				.parseBoolean(System.getProperty("headless", ConfigReader.prop.getProperty("headless")));

		if ("QA".equalsIgnoreCase(environment)) {
			ConfigReader.Read_QA_Property();
			browser = System.getProperty("browser", ConfigReader.prop1.getProperty("Browser"));
		} else if ("Live".equalsIgnoreCase(environment)) {
			ConfigReader.Read_Live_Property();
			browser = System.getProperty("browser", ConfigReader.prop2.getProperty("Browser"));
		} else {
			throw new RuntimeException("Invalid environment specified: " + environment);
		}

		WebDriver drv;

		if (useSeleniumGrid) {
			String seleniumGridUrl = System.getProperty("seleniumGridUrl",
					ConfigReader.prop.getProperty("seleniumGridUrl"));

			if (seleniumGridUrl == null || seleniumGridUrl.trim().isEmpty()) {
				throw new RuntimeException("Selenium Grid URL is not specified in config or system property.");
			}

			// Remote execution on Selenium Grid / Docker container
			switch (browser.toLowerCase()) {
			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--disable-notifications");
				if (headless)
					chromeOptions.addArguments("--headless=new", "--window-size=1920,1080");
				drv = new RemoteWebDriver(new URL(seleniumGridUrl), chromeOptions);
				break;
			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				if (headless)
					firefoxOptions.addArguments("-headless");
				drv = new RemoteWebDriver(new URL(seleniumGridUrl), firefoxOptions);
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				if (headless)
					edgeOptions.addArguments("--headless=new", "--window-size=1920,1080");
				drv = new RemoteWebDriver(new URL(seleniumGridUrl), edgeOptions);
				break;
			default:
				throw new RuntimeException("Invalid browser specified: " + browser);
			}
		} else {
			// Local execution
			switch (browser.toLowerCase()) {
			case "chrome":
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-notifications");
				if (headless)
					options.addArguments("--headless=new", "--window-size=1920,1080");
				drv = new ChromeDriver(options);
				break;
			case "firefox":
				FirefoxOptions ffOptions = new FirefoxOptions();
				if (headless)
					ffOptions.addArguments("-headless");
				drv = new FirefoxDriver(ffOptions);
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				if (headless)
					edgeOptions.addArguments("--headless=new", "--window-size=1920,1080");
				drv = new EdgeDriver(edgeOptions);
				break;
			default:
				throw new RuntimeException("Invalid browser specified: " + browser);
			}
		}

		driver.set(drv);

		ConfigReader.logger.info("::::Maximizing Window::::");
		getDriver().manage().window().maximize();

		String url = ("QA".equalsIgnoreCase(environment)) ? ConfigReader.prop1.getProperty("url")
				: ConfigReader.prop2.getProperty("url");

		ConfigReader.logger.info("::::Navigating to URL::::");
		getDriver().navigate().to(url);
	}

	private void closeBrowser() throws Exception {
		if (getDriver() != null) {
			getDriver().quit();
			driver.remove();
		}
		if ("yes".equalsIgnoreCase(ConfigReader.prop.getProperty("Recording"))) {
			My_Screen_Recorder.Stop_Recording();
		}
	}
}
