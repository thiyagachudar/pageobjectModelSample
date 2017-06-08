package supportlibraries;

import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.cognizant.framework.CraftDataTable;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Settings;


/**
 * Abstract base class for reusable libraries created by the user
 * @author Cognizant
 * @version 3.0
 * @since October 2011
 */
public abstract class ReusableLibrary
{
	/**
	 * The {@link CraftDataTable} object (passed from the test script)
	 */
	protected CraftDataTable dataTable;
	/**
	 * The {@link SeleniumReport} object (passed from the test script)
	 */
	protected SeleniumReport report;
	/**
	 * The {@link WebDriver} object
	 */
	protected WebDriver driver;
	/**
	 * The {@link ScriptHelper} object (required for calling one reusable library from another)
	 */
	protected ScriptHelper scriptHelper;
	
	/**
	 * The {@link Properties} object with settings loaded from the framework properties file
	 */
	protected Properties properties = Settings.getInstance();
	/**
	 * The {@link FrameworkParameters} object
	 */
	protected FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	
	
	/**
	 * Constructor to initialize the {@link ScriptHelper} object and in turn the objects wrapped by it
	 * @param scriptHelper The {@link ScriptHelper} object
	 * @param driver The {@link WebDriver} object
	 */
	public ReusableLibrary(ScriptHelper scriptHelper)
	{
		this.scriptHelper = scriptHelper;
		
		this.dataTable = scriptHelper.getDataTable();
		this.report = scriptHelper.getReport();
		this.driver = scriptHelper.getDriver();
	}
}