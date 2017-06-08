package businesscomponents;

import java.util.List;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import uimap.AdministrationPage;
import uimap.CreateClUser;
import uimap.HomePage;
import uimap.LoginPage;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import componentgroups.GenericFunctions;

public class BVT_TC1 extends ReusableLibrary {
	
	/**************************************************************************************************************
	                      **Case Manager - User - Creation of Client User using Admin User**
	 ************************************************************************************************************/

	public BVT_TC1(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}


	LoginPage lPage = new LoginPage(scriptHelper);
	HomePage hPage = new HomePage(scriptHelper);
	AdministrationPage AdminPg = new AdministrationPage(scriptHelper);
	CreateClUser CUserCreatePg = new CreateClUser(scriptHelper);

	private GenericFunctions gen = new GenericFunctions(scriptHelper);
	
	/**
	 * LoadURL Business Component
	*/
	public void loadURL() {
		launchApp();
	}
	
	/**
	 * StartApp Business Component
	 */
	public void launchApp() {
		String ssURL = dataTable.getData("General_Data", "SS_URL");
		System.out.print(ssURL);
		if (ssURL == null || ssURL.trim().equals("")) {
			
			throw new FrameworkException("'SS_URL' Input Testdata value cannot be BLANK");
		}
		report.updateTestLog("LaunchApplication", "Start EDA Web Application URL: '" + ssURL + "'", Status.DONE);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();		
		driver.get(ssURL);
		if(lPage.getAccountLableLoginPg().isDisplayed()){
			report.updateTestLog("LaunchApplication", "Launch of EDA Web Application Successful", Status.PASS);
		} else {
			throw new FrameworkException("Failed to Launch EDA Web Application !!!");
		}
		
	}
	
	/**
	 * Login Business Component
	 */
	
	public void logon() {
		
		if(lPage.getReturnToLogonbtn() != null){
			lPage.getReturnToLogonbtn().click();
			try{
			Thread.sleep(1000);
			}catch(Exception e){}
		}
		String userName = dataTable.getData("General_Data", "Username");
		String password = dataTable.getData("General_Data", "Password");
		
		report.updateTestLog("Login EDAWebApp", "EDA Web Application Login Username: '" + userName + "'", Status.DONE);
		
		lPage.getLoginPgUsernameField().clear();
		lPage.getLoginPgUsernameField().sendKeys(userName);
		lPage.getLoginPgPasswordField().clear();
		lPage.getLoginPgPasswordField().sendKeys(password);
		lPage.getLogonbtn().click();
		
		if (hPage.getHomeLable().isDisplayed()) {
			report.updateTestLog("VerifySuccessfulLogin", "Successfully Logged into EDA Web Application", Status.PASS);
		}else{
			report.updateTestLog("VerifySuccessfulLogin", "Login Unsuccessfull. Home Lable Link not Appeared/Displayed", Status.FAIL);
		}
	
	}
	
	/*private void verifySuccessfulLogin() {
		
			if (hPage.getHomeLable().isDisplayed()) {
				report.updateTestLog("VerifySuccessfulLogin", "Successfully Logged into EDA Web Application", Status.PASS);
			}else{
				report.updateTestLog("VerifySuccessfulLogin", "Login Unsuccessfull. Home Lable Link not Appeared/Displayed", Status.FAIL);
			}
	}*/
	
	public void logonAsAnotherUser() {
		String userName = dataTable.getData("General_Data", "Username2");
		String password = dataTable.getData("General_Data", "Password2");
		
		report.updateTestLog("Login EDAWebApp", "EDA Web Application Login Username: '" + userName + "'", Status.DONE);
		
		lPage.getLoginPgUsernameField().clear();
		lPage.getLoginPgUsernameField().sendKeys(userName);
		lPage.getLoginPgPasswordField().clear();
		lPage.getLoginPgPasswordField().sendKeys(password);
		lPage.getLogonbtn().click();
		
		if (hPage.getHomeLable().isDisplayed()) {
			report.updateTestLog("VerifySuccessfulLogin", "Successfully Logged into EDA Web Application", Status.PASS);
		}else{
			report.updateTestLog("VerifySuccessfulLogin", "Login Unsuccessfull. Home Lable Link not Appeared/Displayed", Status.FAIL);
		}
	
	}
	
	/**
	 * Administration Page Business Component
	 */
	public void gotoAdministrationPg(){
		hPage.getAdminisstrationlink().click();
		try {
			if (AdminPg.getAdministrationLable().isDisplayed()) {
				report.updateTestLog("GotoAdministrationPage", "Successfully navigated to Administration Page", Status.PASS);
				return;
			}
		} catch (Exception e) { }
		throw new FrameworkException("Not able to Navigate to Administration Page. Administration Lable not Appeared/Displayed");
	
	}
	
	/**
	 * Create client user Business Component
	 */
	public void create_ClientUser(){
		
		String userName = dataTable.getData("CreateUser_Data", "UserName");
		String FirstName = dataTable.getData("CreateUser_Data", "FirstName");
		String LastName = dataTable.getData("CreateUser_Data", "LastName");
		String EmailAddress = dataTable.getData("CreateUser_Data", "EmailAddress");
		String password = dataTable.getData("CreateUser_Data", "TempPassword");
	
		hPage.getAdminisstrationlink().click();
		AdminPg.getUserslink().click();
		AdminPg.getCreaeUsersbtn().click();
				
		checkAccountType();
		
		CUserCreatePg.getClnUserName().sendKeys(userName);
		CUserCreatePg.getClnFirstName().sendKeys(FirstName);
		CUserCreatePg.getClnLastName().sendKeys(LastName);
		CUserCreatePg.getClnEmailAddress().sendKeys(EmailAddress);
		
		
		
		//String ClientName = dataTable.getData("CreateUser_Data", "ClientName");
		
		new Select(CUserCreatePg.getClnClientName()).selectByValue("1");
	
		
		CUserCreatePg.getClnPassword().sendKeys(password);
		CUserCreatePg.getClnConfirmPassword().sendKeys(password);
		
		//GeneratePassw();
		
		if(!CUserCreatePg.getClnChangePasswinNextLoginChkbx().isSelected()){
			CUserCreatePg.getClnChangePasswinNextLoginChkbx().click();
		}
		if(!CUserCreatePg.getClnActiveChkbx().isSelected()){
			CUserCreatePg.getClnActiveChkbx().click();
		}
		
		caseaccesslevel();
		
		CUserCreatePg.getClnCreateUserbtn().click();
		
		validateUsercreatedMsg();
												
		}
	
	
	/** Logon as New Client User
	*/
	public void changePasswordClientUser() {
		
		String userName = dataTable.getData("CreateUser_Data", "UserName");
		String Tpassword = dataTable.getData("CreateUser_Data", "TempPassword");
		String password = dataTable.getData("CreateUser_Data", "Password");
		report.updateTestLog("Login as ClientUser", "Username of new ClientUser: '" + userName + "'", Status.DONE);
		
		lPage.getLoginPgUsernameField().clear();
		lPage.getLoginPgUsernameField().sendKeys(userName);
		lPage.getLoginPgPasswordField().clear();
		lPage.getLoginPgPasswordField().sendKeys(Tpassword);
		lPage.getLogonbtn().click();
		
			String ChgPass = dataTable.getData("CreateUser_Data", "ChangePasswordLable");
			if(CUserCreatePg.getChangePasswlable().getText().contains(ChgPass)){
				
				CUserCreatePg.getCurrentPassword().sendKeys(Tpassword);
				CUserCreatePg.getNewPassword().sendKeys(password);
				CUserCreatePg.getConfirmPassword().sendKeys(password);
				CUserCreatePg.getChangePasswordbtn().click();
				String PchMsg = dataTable.getData("CreateUser_Data", "PasswordChangedMessage");
				if(CUserCreatePg.getPasswordChangedMsg().getText().contains(PchMsg)){
				report.updateTestLog("ChangePassword", "Password Changed successfully", Status.PASS);
			}else{
				report.updateTestLog("ChangePassword", "Password Change unsuccessful", Status.PASS);
			}	
	}
}
		
	
	/* Default Account Type */
	private void checkAccountType() {
		try {
			
			String AccessType = dataTable.getData("CreateUser_Data", "AccountType");
			
			Select acctype = new Select(CUserCreatePg.getClnAccType());
			String selected = acctype.getFirstSelectedOption().getText();
			if(selected.equals(AccessType)){
				report.updateTestLog("Check AccountType", "Default account type is Early Data Analyzer Account", Status.PASS);
				return;
			}
				/*List<WebElement> Options = acctype.getOptions();
				for(WebElement option:Options){
				  if(option.getText().equals(AccessType)){
				       option.click();  
					}
				}*/
		} catch (Exception e) { }
		throw new FrameworkException("Early Data Analyzer Account is not the default account type");
	}
	
	
	/* Select Client */
	private void SelectClient() {
		try {
			
			
			
		} catch (Exception e) { }
		
	}
	
	/* Read generated password */
	private void GeneratePassw() {
		
		CUserCreatePg.getClnGeneratePassword().click();
		
		try {					
			String passwordgenerated = CUserCreatePg.getClnPassword().getText();
			if(passwordgenerated != null)
			dataTable.putData("CreateUser_Data", "TempPassword", passwordgenerated);
			report.updateTestLog("Generate Password", "Temporary password generated:" + passwordgenerated, Status.PASS);
			return;
		
	} catch (Exception e) { }
	throw new FrameworkException("Unable to read temporary password generated");
}
	
	/* Default Account Type */
	private void caseaccesslevel() {
		try {
			
			String accesslevel = dataTable.getData("CreateUser_Data", "CaseLevelAccess");
			
			Select acclevel = new Select(CUserCreatePg.getClnAccessLevel());
			String selected = acclevel.getFirstSelectedOption().getText();
			if(selected.equals(accesslevel)){
				report.updateTestLog("Case AccessLevel", "Default case access level is Full Access", Status.PASS);
				return;
			}
				/*List<WebElement> Options = acctype.getOptions();
				for(WebElement option:Options){
				  if(option.getText().equals(AccessType)){
				       option.click();  
					}
				}*/
		} catch (Exception e) { }
		throw new FrameworkException("Full Access is not the default case access level");
	}
	
	/* User Created Message Validation*/
	private void validateUsercreatedMsg(){
	try{
		Thread.sleep(3000);
		String UserCrMsg = dataTable.getData("CreateUser_Data", "UserCreatedMsg");
		if(CUserCreatePg.getClnUserCreatedMsg().getText().contains(UserCrMsg)) {
			report.updateTestLog("User Created Message", "User successfully created message is displayed", Status.PASS);
			return;
		}
		
	}catch (Exception e) { }
	throw new FrameworkException("User successfully created message is not displayed");
}
	
	
		
	/**
	 * Return to Logon page Business Component
	 */
	public void returnTologon(){
		lPage.getReturnToLogonbtn().click();
		if(lPage.getLogonbtn().isDisplayed()){
		report.updateTestLog("ReturnToLogOn", "Successfully Returned to Logon Page", Status.PASS);
	}else{			
		report.updateTestLog("ReturnToLogOn", "Unable to return to Logon page", Status.FAIL);
	}
}
	
	/**
	 * Logout from Administration Page Business Component
	 */
	public void logoffAdminPage(){
		report.updateTestLog("Logout", "Processing Logout Functionality from Admin Page", Status.DONE);
		AdminPg.getLogofflink().click();
		//String LgOff = dataTable.getData("CreateUser_Data", "LoggedOff");
		if(lPage.getLoggedoffLable().getText().contains("Logged Off")){
			report.updateTestLog("Logout", "Logout from Admin page was Successfully", Status.PASS);
			
		}else{
			report.updateTestLog("Logout", "Logout from Admin page unsuccessful", Status.FAIL);
		}
		
	}
	

	/**
	 * Logout Business Component
	 */
	public void logoff() {
		report.updateTestLog("Logout", "Processing Logout Functionality", Status.DONE);
		try{
			Thread.sleep(2000);
		}catch(Exception e){}
		hPage.getLoggOfflinkHmPg().click();
		if (lPage.getLoggedOffHeader().getText().equals("Logged Off")) {
			report.updateTestLog("Logout", "Logout was Successfully", Status.PASS);
			driver.close();
		} else {
			report.updateTestLog("Logout", "Logout Unsuccessful", Status.FAIL);
			
		}
	}
	
	
	public void userLogoff() {
		report.updateTestLog("Logout", "Processing Logout Functionality", Status.DONE);
		hPage.getLoggOfflinkHmPg().click();
		if (lPage.getLoggedOffHeader().getText().equals("Logged Off")) {
			report.updateTestLog("Logout", "Logout was Successfully", Status.PASS);
		} else {
			report.updateTestLog("Logout", "Logout Unsuccessful", Status.FAIL);
		}
	}
}

