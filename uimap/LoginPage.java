package uimap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import componentgroups.GenericFunctions;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class LoginPage extends ReusableLibrary {

	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper
	 */
	
	public LoginPage(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	//LogOn Page
	private final String LogonFrm_id = "Log On"; 
	private final String Account_lable_id = "location-bar-label";
	private final String UserName_tbx_id = "UserName";
	private final String Password_tbx_id = "Password";
	private final String LogOn_buton_id = "btn-logon-submit";
	private final String LoggedoffHeader = "html/body/div[1]/div[2]/div[1]/div[1]/div[2]/h3";

	//Logged Off Page
	private final String LoggedOffLable_xpath = "html/body/div[1]/div[2]/div[1]/div[1]/div[2]/h3";
	//private final String ReturnToLogon_btn_xpath = "html/body/div[1]/div[3]/div[3]/div[2]/a";

	private GenericFunctions gen = new GenericFunctions(scriptHelper);
	
	public boolean isInnerFrameAppeared(WebDriver driver) {
		for (int i = 0; i < 5; i++) {
			gen.sleep(1000);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			if (jse.executeScript("return document.getElementById(LogonFrm_id)") != null)
				return true;
		}
		return false;
	}
	
	public WebElement getAccountLableLoginPg() {
		gen.waitForObjectExists(By.id(Account_lable_id));
		gen.checkElementExists("Account lable in Login Page", By.id(Account_lable_id));
		return driver.findElement(By.id(Account_lable_id));
	}
	
	public WebElement getLoginPgUsernameField() {
		gen.waitForObjectExists(By.id(UserName_tbx_id));
		gen.checkElementExists("User name field in Login Page", By.id(UserName_tbx_id));
		return driver.findElement(By.id(UserName_tbx_id));
	}
	
	public WebElement getLoginPgPasswordField() {
		gen.waitForObjectExists(By.id(Password_tbx_id));
		gen.checkElementExists("Password field in Login Page", By.id(Password_tbx_id));
		return driver.findElement(By.id(Password_tbx_id));
	}
	
	public WebElement getLoggedoffLable() {
		gen.waitForObjectExists(By.xpath(LoggedOffLable_xpath));
		gen.checkElementExists("Logged Off Lable", By.xpath(LoggedOffLable_xpath));
		return driver.findElement(By.xpath(LoggedOffLable_xpath));
	}
	
	

	public WebElement getLogonbtn() {
		gen.waitForObjectExists(By.id(LogOn_buton_id));
		gen.checkElementExists("Login button", By.id(LogOn_buton_id));
		return driver.findElement(By.id(LogOn_buton_id));
	}
	
	public WebElement getReturnToLogonbtn() {
		
		WebElement returntolog = null;
		try{
			returntolog = driver.findElement(By.xpath(".//*[@class='container-fluid main-container']/div[2]/div[1]/div[3]/div[2]/a"));
		}catch(Exception e){}
		
		return returntolog;
	}
	public WebElement getLoggedOffHeader() {
		gen.waitForObjectExists(By.xpath(LoggedoffHeader));
		gen.checkElementExists("Logged off header", By.xpath(LoggedoffHeader));
		return driver.findElement(By.xpath(LoggedoffHeader));
	}
	
	

}
	

