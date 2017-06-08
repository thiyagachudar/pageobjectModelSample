package supportlibraries;

import java.io.File;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.Settings;
import com.opera.core.systems.OperaDriver;



/**
 * Factory for creating the Driver object based on the required browser
 * @author Cognizant
 * @version 3.0
 * @since October 2011
 */
public class WebDriverFactory {
	private static Properties properties;
	
	/**
	 * Function to return the appropriate {@link RemoteWebDriver} object based on the {@link Browser} passed
	 * @param browser The {@link Browser} to be used for the test execution
	 * @return The {@link RemoteWebDriver} object corresponding to the {@link Browser} specified
	 */
	public static RemoteWebDriver getDriver(Browser browser) {
		WebDriver driver = null;
		switch (browser) {
		case chrome:
			properties = Settings.getInstance();
			System.setProperty("webdriver.chrome.driver", properties.getProperty("ChromeDriverPath"));
			driver = new ChromeDriver();
			break;
		case firefox:
			driver = new FirefoxDriver();
			break;
		case htmlunit:
			driver = new HtmlUnitDriver();
			break;
		case iexplore:
			properties = Settings.getInstance();
			System.setProperty("webdriver.ie.driver", properties.getProperty("InternetExplorerDriverPath"));
			driver = new InternetExplorerDriver(getInternetExplorerCapabilities());
			break;
		case opera:
			driver = new OperaDriver();
			break;
		}
		return (RemoteWebDriver)driver;
	}
	
	/**
	 * @return
	 */
	public static DesiredCapabilities getInternetExplorerCapabilities() {
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true); 
		return capabilities;
	}

	/**
	 * @return
	 */
	public static FirefoxProfile getFirefoxDownloadProfile() {
		String downloadPath = new File("").getAbsolutePath() + "\\Downloads";
		new File(downloadPath).mkdir();
		
		FirefoxProfile fProfile = new FirefoxProfile();
		fProfile.setPreference("browser.download.folderList", 2);
		fProfile.setPreference("browser.download.dir", downloadPath);
		fProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/rtf,text/plain,text/csv,text/html");
		fProfile.setPreference("browser.download.manager.showWhenStarting", false);
		fProfile.setPreference("browser.download.useDownloadDir", "false");
		return fProfile;
	}
}