package componentgroups;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import supportlibraries.DriverScript;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

import com.cognizant.framework.FrameworkException;

/**
 * Generic Component Group Library
 */
public class GenericFunctions extends ReusableLibrary {

	private static final int MAX_TIMEOUT = 80;
	private static final int MIN_TIMEOUT = 30;
	
	/**
	 * Constructor to initialize the component group library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link DriverScript}
	 */
	public GenericFunctions(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	/**
	 * @return
	 * @param element
	 */
	public WebElement getParent(WebElement element) {
		return element.findElement(By.xpath("parent::*"));
	}
	
	/**
	 * @param millis
	 */
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) { }
	}

	/**
	 * @return
	 * @param locator
	 * @param text
	 */
	public boolean hasText(By locator, String text) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
		wait.until(ExpectedConditions.textToBePresentInElement(locator, text));
		List<WebElement> ele = driver.findElements(locator);
		if (ele.size() != 0 && ele.get(0).isDisplayed() && ele.get(0).getText().indexOf(text) != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return
	 * @param locator
	 */
	public boolean waitForObjectClickable(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * @return
	 * @param locator
	 */
	public boolean isObjectExists(By locator) {
		List<WebElement> elements = driver != null ? driver.findElements(locator) : null;
		return (elements != null && elements.size() > 0) ? true : false;
	}
	
	/**
	 * @return
	 * @param locator
	 */
	public boolean waitForObjectExists(By locator) {
		return waitForObjectExists(locator, MAX_TIMEOUT);
	}
	
	/**
	 * @return
	 * @param pElement
	 * @param locator
	 */
	public boolean waitForObjectExists(WebElement pElement, By locator) {
		return waitForObjectExists(pElement, locator, MAX_TIMEOUT);
	}
	
	/**
	 * @return
	 * @param locator
	 */
	public boolean waitForObjectExists(By locator, int timeout) {
		ExecutorService executor = Executors.newCachedThreadPool();
		WaitForObjectExists task = new WaitForObjectExists(driver, null, locator);
		Future<Boolean> future = executor.submit(task);
		try {
			return future.get(timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
		} finally {
			executor.shutdownNow();
		}
		return false;
	}
	
	/**
	 * @return
	 * @param pElement
	 * @param locator
	 */
	public boolean waitForObjectExists(WebElement pElement, By locator, int timeout) {
		ExecutorService executor = Executors.newCachedThreadPool();
		WaitForObjectExists task = new WaitForObjectExists(null, pElement, locator);
		Future<Boolean> future = executor.submit(task);
		try {
			return future.get(timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
		} finally {
			executor.shutdownNow();
		}
		return false;
	}
	
	/**
	 * @return
	 * @param objDesc
	 * @param locator
	 */
	public boolean checkElementExists(String objDesc, By locator) {
		ExecutorService executor = Executors.newCachedThreadPool();
		CheckForObjectExists task = new CheckForObjectExists(driver, null, locator);
		Future<Boolean> future = executor.submit(task);
		try {
			if (future.get(MIN_TIMEOUT, TimeUnit.SECONDS))
				return true;
		} catch (TimeoutException e) {
			throw new FrameworkException("Object Recognition Error", "Timout Exception: '" + objDesc + "' Element(s) NOT Found");
		} catch (Exception e) {
		} finally {
			executor.shutdownNow();
		}
		throw new FrameworkException("Object Recognition Error", "'" + objDesc + "' Element(s) NOT Found");
	}
	
	/**
	 * @return
	 * @param pElement
	 * @param objDesc
	 * @param locator
	 */
	public boolean checkElementExists(WebElement pElement, String objDesc, By locator) {
		ExecutorService executor = Executors.newCachedThreadPool();
		CheckForObjectExists task = new CheckForObjectExists(null, pElement, locator);
		Future<Boolean> future = executor.submit(task);
		try {
			if (future.get(MIN_TIMEOUT, TimeUnit.SECONDS))
				return true;
		} catch (TimeoutException e) {
			throw new FrameworkException("Object Recognition Error", "Timout Exception: '" + objDesc + "' Element(s) NOT Found");
		} catch (Exception e) {
		} finally {
			executor.shutdownNow();
		}
		throw new FrameworkException("Object Recognition Error", "'" + objDesc + "' Element(s) NOT Found");
	}
	
	/**
	 * @return
	 */
	public boolean waitForAlertBox() {
		ExecutorService executor = Executors.newCachedThreadPool();
		AlertExists alert = new AlertExists(driver);
		Future<Boolean> future = executor.submit(alert);
		try {
			return future.get(MAX_TIMEOUT, TimeUnit.SECONDS);
		} catch (Exception e) {
		} finally {
			executor.shutdownNow();
		}
		return false;
	}
	
	public boolean checkifObjectClickable(By locator) {
		try {
			ExpectedConditions.elementToBeClickable(locator);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}

class WaitForObjectExists implements Callable<Boolean> {
	WebDriver driver = null;
	WebElement pElement = null;
	By locator = null;

	WaitForObjectExists(WebDriver driver, WebElement pElement, By locator) {
		this.driver = driver;
		this.pElement = pElement;
		this.locator = locator;
	}

	@Override
	public Boolean call() throws Exception {
		for (;;) {
			try {
				Thread.sleep(500);
				if ((driver != null && driver.findElements(locator).size() != 0)
						|| (pElement != null && pElement.findElements(locator).size() != 0))
					return true;
			} catch (Exception e) { }
		}
	}
}

class CheckForObjectExists implements Callable<Boolean> {
	WebDriver driver = null;
	WebElement pElement = null;
	By locator = null;

	CheckForObjectExists(WebDriver driver, WebElement pElement, By locator) {
		this.driver = driver;
		this.pElement = pElement;
		this.locator = locator;
	}

	@Override
	public Boolean call() throws Exception {
		Thread.sleep(500);
		List<WebElement> elements = driver != null ? driver.findElements(locator)
				: pElement != null ? pElement.findElements(locator) : null;
		return (elements != null && elements.size() > 0) ? true : false;
	}
}

class AlertExists implements Callable<Boolean> {
	WebDriver driver = null;

	AlertExists(WebDriver driver) {
		this.driver = driver;
	}
	
	@Override
	public Boolean call() throws Exception {
		for (;;) {
			try {
				Thread.sleep(1000);
				if (driver.switchTo().alert() != null)
					return true;
			} catch (Exception e) { }
		}
	}
}