package supportlibraries;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Report;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.ReportTheme;


/**
 * Class to extend the reporting features of the framework
 * @author Cognizant
 * @version 4.0
 * @since June 2012
 */
public class SeleniumReport extends Report
{
	private WebDriver driver;
	/**
	 * Function to set the {@link WebDriver} object
	 * @param driver The {@link WebDriver} object
	 */
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	
	/**
	 * Constructor to initialize the Report
	 * @param reportSettings The {@link ReportSettings} object
	 * @param reportTheme The {@link ReportTheme} object
	 */
	public SeleniumReport(ReportSettings reportSettings, ReportTheme reportTheme)
	{
		super(reportSettings, reportTheme);
	}
	
	@Override
	protected void takeScreenshot(String screenshotPath)
	{
		if (driver == null) {
			throw new FrameworkException("Report.driver is not initialized!");
		}
		
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		try {
			FileUtils.copyFile(scrFile, new File(screenshotPath), true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screenshot to file");
		}
	}
}