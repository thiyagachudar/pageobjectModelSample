package uimap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import componentgroups.GenericFunctions;

public class AdministrationPage extends ReusableLibrary{

	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper
	 */
	public AdministrationPage(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	//Administration Page
	private final String AdministrationLable_xpath = ".//*[@id='well-location-bar']/h1";
	private final String Users_link = "Users";
	private final String CreateUser_btn_id ="a-create-user-top";
	private final String Logoff_xpath = ".//*[@id='a-logoff']";
	
	String FirstName = dataTable.getData("CreateUser_Data", "FirstName"); 
	String LastName = dataTable.getData("CreateUser_Data", "LastName"); 
	String ClientUserNm = FirstName +" " +LastName;
	
	//Assign a case to Client User
	String ClUserName_link = ClientUserNm;
	private final String CaseAssignmetnTab_link = "Case Assignment";
	private final String AddRemoveCasesBtn_xpath = ".//*[@id='main_body container-fluid']/div[1]/div[2]/div/div[2]/div/div[2]/a";
	private final String AddRemoveLable_xpath = ".//*[@id='add-remove-cases']/form/div[1]/h3";
	private final String SaveChangesBtn_xpath = ".//*[@id='btn-submit-add-remove-cases']";
	private final String AddedCsName_xpath = ".//*[@id='table-user-cases']/tbody/tr/td";
	private final String AddRemoveCaseTable_xpath = ".//*[@id='case-add-remove-table']/tbody";
	private final String DeleteUser_xpath = ".//*[@id='a-edit-delete-user']";
	private final String DeleteUserpp_xpath = ".//*[@id='delete-user-btn']"; 
	private final String UserDeletedMsg_xpath = ".//*[@id='main_body container-fluid']/div[1]/div[2]/div/div[1]/div/div";
	
	private GenericFunctions gen = new GenericFunctions(scriptHelper);
	
	public WebElement getAdministrationLable() {
		gen.waitForObjectExists(By.xpath(AdministrationLable_xpath));
		gen.checkElementExists("AdministrationLable", By.xpath(AdministrationLable_xpath));
		return driver.findElement(By.xpath(AdministrationLable_xpath));
		
	}
	
	public WebElement getUserslink() {
		gen.waitForObjectExists(By.linkText(Users_link));
		gen.checkElementExists("Users Link", By.linkText(Users_link));
		return driver.findElement(By.linkText(Users_link));
	}
	
	public WebElement getCreaeUsersbtn() {
		gen.waitForObjectExists(By.id(CreateUser_btn_id));
		gen.checkElementExists("Create User Button", By.id(CreateUser_btn_id));
		return driver.findElement(By.id(CreateUser_btn_id));
	}
	
	public WebElement getLogofflink() {
		gen.waitForObjectExists(By.xpath(Logoff_xpath));
		gen.checkElementExists("Log off link", By.xpath(Logoff_xpath));
		return driver.findElement(By.xpath(Logoff_xpath));
	}
	
	public WebElement getClientUserlink() {
		gen.waitForObjectExists(By.linkText(ClUserName_link));
		gen.checkElementExists("Client User Name Link", By.linkText(ClUserName_link));
		return driver.findElement(By.linkText(ClUserName_link));
	}
	
	public WebElement getCaseAssignmentTab() {
		gen.waitForObjectExists(By.linkText(CaseAssignmetnTab_link));
		gen.checkElementExists("Case Assignment Tab", By.linkText(CaseAssignmetnTab_link));
		return driver.findElement(By.linkText(CaseAssignmetnTab_link));
	}
	
	public WebElement getAddRemoveCaseBtn() {
		gen.waitForObjectExists(By.xpath(AddRemoveCasesBtn_xpath));
		gen.checkElementExists("Add/Remove Case Button", By.xpath(AddRemoveCasesBtn_xpath));
		return driver.findElement(By.xpath(AddRemoveCasesBtn_xpath));
	}
	
	public WebElement getAddRemoveLable() {
		gen.waitForObjectExists(By.xpath(AddRemoveLable_xpath));
		gen.checkElementExists("Add/Remove Lable", By.xpath(AddRemoveLable_xpath));
		return driver.findElement(By.xpath(AddRemoveLable_xpath));
	}
	
	public WebElement getSaveChangesBtn() {
		gen.waitForObjectExists(By.xpath(SaveChangesBtn_xpath));
		gen.checkElementExists("Save Changes Button", By.xpath(SaveChangesBtn_xpath));
		return driver.findElement(By.xpath(SaveChangesBtn_xpath));
	}
	
	public WebElement getAddedCaseName() {
		gen.waitForObjectExists(By.xpath(AddedCsName_xpath));
		gen.checkElementExists("Changes Saved button", By.xpath(AddedCsName_xpath));
		return driver.findElement(By.xpath(AddedCsName_xpath));
	}
		
	public WebElement getAddRemovePopupTable() {
		gen.waitForObjectExists(By.xpath(AddRemoveCaseTable_xpath));
		gen.checkElementExists("Add/remove pop-up-Cases Table", By.xpath(AddRemoveCaseTable_xpath));
		return driver.findElement(By.xpath(AddRemoveCaseTable_xpath));
	}
	
	public WebElement getDeleteUserBtn() {
		gen.waitForObjectExists(By.xpath(DeleteUser_xpath));
		gen.checkElementExists("DeleteUser button", By.xpath(DeleteUser_xpath));
		return driver.findElement(By.xpath(DeleteUser_xpath));
	}
	
	public WebElement getDeleteUserpp() {
		gen.waitForObjectExists(By.xpath(DeleteUserpp_xpath));
		gen.checkElementExists("DeleteUser button in the confirmation pop-up", By.xpath(DeleteUserpp_xpath));
		return driver.findElement(By.xpath(DeleteUserpp_xpath));
	}
	
	public WebElement getUserDeletedMsg() {
		gen.waitForObjectExists(By.xpath(UserDeletedMsg_xpath));
		gen.checkElementExists("User Deleted Msg", By.xpath(UserDeletedMsg_xpath));
		return driver.findElement(By.xpath(UserDeletedMsg_xpath));
	}
	
	
}
