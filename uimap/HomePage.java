package uimap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import componentgroups.GenericFunctions;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class HomePage extends ReusableLibrary{

	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper
	 */
	public HomePage(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
	//Home Page
	private final String Home_lable_id = "location-bar-label";
	private final String Home_link = "Home";
	private final String Case_Manager_link = "Case Manager";
	private final String AutomationAdmin_xpath = ".//*[@class='navbar-inner']/div/div/div[2]/div/strong/a";
	private final String Log_off_HmPg_Link = "Log Off";
	private final String Administration_link = "Administration";
	
	private GenericFunctions gen = new GenericFunctions(scriptHelper);
	
	public WebElement getHomeLable() {
		gen.waitForObjectExists(By.id(Home_lable_id));
		gen.checkElementExists("Home Lable", By.id(Home_lable_id));
		return driver.findElement(By.id(Home_lable_id));
	}
	
	public WebElement getHomeLink() {
		gen.waitForObjectExists(By.linkText(Home_link));
		gen.checkElementExists("Home Link", By.linkText(Home_link));
		return driver.findElement(By.linkText(Home_link));
	}
	
	public WebElement getCase_ManagerLink() {
		gen.waitForObjectExists(By.linkText(Case_Manager_link));
		gen.checkElementExists("Case Manager Link", By.linkText(Case_Manager_link));
		return driver.findElement(By.linkText(Case_Manager_link));
	}
	
	public WebElement getAutoAdminLink() {
		gen.waitForObjectExists(By.xpath(AutomationAdmin_xpath));
		gen.checkElementExists("admin admin link", By.xpath(AutomationAdmin_xpath));
		return driver.findElement(By.xpath(AutomationAdmin_xpath));
	}
	
	
	public WebElement getLoggOfflinkHmPg() {
		gen.waitForObjectExists(By.linkText(Log_off_HmPg_Link));
		gen.checkElementExists("Log Off Link", By.linkText(Log_off_HmPg_Link));
		return driver.findElement(By.linkText(Log_off_HmPg_Link));
	}
	
	public WebElement getAdminisstrationlink() {
		gen.waitForObjectExists(By.linkText(Administration_link));
		gen.checkElementExists("Administration Link", By.linkText(Administration_link));
		return driver.findElement(By.linkText(Administration_link));
	}


}
