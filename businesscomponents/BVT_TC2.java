package businesscomponents;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import uimap.AdministrationPage;
import uimap.CreateClUser;
import uimap.HomePage;
import uimap.LoginPage;

import com.cognizant.framework.Status;

public class BVT_TC2 extends ReusableLibrary{
	
	/*********************************************************************************************************************************
         **Log On - Verify whether user not able to give the current password as new password and login into the Web application**
	*********************************************************************************************************************************/
	
	public BVT_TC2(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
	LoginPage lPage = new LoginPage(scriptHelper);
	HomePage hPage = new HomePage(scriptHelper);
	AdministrationPage AdminPg = new AdministrationPage(scriptHelper);
	CreateClUser CUserCreatePg = new CreateClUser(scriptHelper);
	/**
	 * Change Password Business Component
	*/
	public void changePassword(){

		hPage.getAutoAdminLink().click();
		if(CUserCreatePg.getAccountLable().isDisplayed()){
			
			report.updateTestLog("Goto AccountPage", "Successfully navigated to Account Page", Status.PASS);
			String password = dataTable.getData("CreateUser_Data", "Password");
			CUserCreatePg.getCurrentPassword().sendKeys(password);
			CUserCreatePg.getNewPassword().sendKeys(password);
			CUserCreatePg.getClnConfirmPassword().sendKeys(password);
			
			report.updateTestLog("Enter Password", "Entered the current password as new password", Status.DONE);
			CUserCreatePg.getSaveChangesbtn().click();
			
			String message = dataTable.getData("CreateUser_Data", "EnterDiffPassword");
			if(CUserCreatePg.getEnterdiffPasswd().getText().contains(message)){
				
				report.updateTestLog("ValidateMessage", "User not able to give the current password as new password", Status.PASS);
			}else{
				report.updateTestLog("ValidateMessage", "Expected message is not displayed", Status.FAIL);
			}
		}else{
			report.updateTestLog("Goto AccountPage", "Unable to navigate to Account Page", Status.FAIL);
		}
	
	}
	
}
