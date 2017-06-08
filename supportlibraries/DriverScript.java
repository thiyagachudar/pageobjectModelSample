package supportlibraries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.cognizant.framework.*;
import com.cognizant.framework.ReportThemeFactory.Theme;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Driver script class which encapsulates the core logic of the CRAFT framework
 * @author Cognizant
 * @version 3.0
 * @since October 2011
 */
public class DriverScript {
	private ArrayList<String> businessFlowData;
	private int currentIteration, currentSubIteration;
	private Date startTime, endTime;
	private String timeStamp;
	private String reportPath;
	
	private CraftDataTable dataTable;
	private ReportSettings reportSettings;
	private SeleniumReport report;
	private RemoteWebDriver driver;
	private ScriptHelper scriptHelper;
	
	private Properties properties;
	private String gridMode;
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	
	/**
	 * The {@link TestParameters} object
	 */
	protected TestParameters testParameters = new TestParameters();

	/**
	 * Constructor to initialize the DriverScript
	 */
	public DriverScript() {
		setRelativePath();
		properties = Settings.getInstance();
		setDefaultTestParameters();
	}

	private void setRelativePath() {
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if (relativePath.contains("allocator")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}

	private void setDefaultTestParameters() {
		testParameters.setCurrentScenario(this.getClass().getSimpleName());
		testParameters.setIterationMode(IterationOptions.RunAllIterations);
		testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser").toLowerCase()));
		testParameters.setBrowserVersion(properties.getProperty("DefaultBrowserVersion"));
		testParameters.setPlatform(PlatformFactory.getPlatform(properties.getProperty("DefaultPlatform")));
	}
	
	/**
	 * Function to execute the given test case
	 */
	public void driveTestExecution() {
		initializeWebDriver();
		initializeTestReport();
		initializeDatatable();
		initializeTestScript();
		initializeTestIterations();

	//	setUp();
		executeTestIterations();
		tearDown();

		wrapUp();
	}
	
	private void initializeWebDriver() {
		startTime = Util.getCurrentTime();
		gridMode = properties.getProperty("GridMode");
		if (gridMode.equalsIgnoreCase("on")) {
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setBrowserName(testParameters.getBrowser().toString());
			desiredCapabilities.setVersion(testParameters.getBrowserVersion());
			desiredCapabilities.setPlatform(testParameters.getPlatform());
			URL gridHubUrl;
			try {
				gridHubUrl = new URL(properties.getProperty("GridHubUrl"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new FrameworkException("The specified Selenium Grid Hub URL is malformed");
			}
			driver = new RemoteWebDriver(gridHubUrl, desiredCapabilities);
		} else {
			driver = WebDriverFactory.getDriver(testParameters.getBrowser());
		}
	}
	
	private void initializeExecutionParameters() {
		if (testParameters.getExecParam().getRunConfiguration() == null) {
			testParameters.getExecParam().setRunConfiguration(properties.getProperty("RunConfiguration").trim());
		}
	}
	
	private void initializeTestReport() {
		initializeExecutionParameters();
		frameworkParameters.setRunConfiguration(testParameters.getExecParam().getRunConfiguration());
		timeStamp = TimeStamp.getInstance(testParameters.getExecParam().getReportFolderName());
		
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));
		report = new SeleniumReport(reportSettings, reportTheme);
		
		report.initializeReportTypes();
		report.setDriver(driver);
		report.initializeTestLog();
		report.addTestLogHeading(reportSettings.getProjectName() + " - " + reportSettings.getReportName() + " Automation Execution Results");
		report.addTestLogSubHeading("Date & Time", ": " + Util.getCurrentFormattedTime(properties.getProperty("DateFormatString")),
				"Iteration Mode", ": " + testParameters.getIterationMode());
		report.addTestLogSubHeading("Start Iteration", ": " + testParameters.getStartIteration(),
				"End Iteration", ": " + testParameters.getEndIteration());
		
		if (gridMode.equalsIgnoreCase("on")) {
			report.addTestLogSubHeading("Browser", ": " + testParameters.getBrowser(),
					"Version", ": " + testParameters.getBrowserVersion());
			report.addTestLogSubHeading("Platform", ": " + testParameters.getPlatform().toString(),
					"Application URL", ": " + properties.getProperty("ApplicationUrl"));
		} else {
			report.addTestLogSubHeading("Browser", ": " + testParameters.getBrowser(),
					"Version", ": " + testParameters.getBrowserVersion());
			report.addTestLogSubHeading("Platform", ": " + testParameters.getPlatform().toString(),
					"Application URL", ": " + properties.getProperty("ApplicationUrl"));
		}
		report.addTestLogTableHeadings();
	}
	
	private void initializeReportSettings() {
		reportPath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Results" + Util.getFileSeparator() + timeStamp;
		reportSettings = new ReportSettings(reportPath, testParameters.getCurrentScenario() + "_" + testParameters.getCurrentTestcase());
		
		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setLogLevel(Integer.parseInt(properties.getProperty("LogLevel")));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.generateExcelReports = Boolean.parseBoolean(properties.getProperty("ExcelReport"));
		reportSettings.generateHtmlReports = Boolean.parseBoolean(properties.getProperty("HtmlReport"));
		reportSettings.includeTestDataInReport = Boolean.parseBoolean(properties.getProperty("IncludeTestDataInReport"));
		reportSettings.takeScreenshotFailedStep = Boolean.parseBoolean(properties.getProperty("TakeScreenshotFailedStep"));
		reportSettings.takeScreenshotPassedStep = Boolean.parseBoolean(properties.getProperty("TakeScreenshotPassedStep"));
	}
	
	private void initializeDatatable() {
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
		String runTimeDatatablePath;
		if (reportSettings.includeTestDataInReport) {
			runTimeDatatablePath = reportPath + Util.getFileSeparator() + "Datatables";
			
			File runTimeDatatable = new File(runTimeDatatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");
			if (!runTimeDatatable.exists()) {
				File datatable = new File(datatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");
				try {
					FileUtils.copyFile(datatable, runTimeDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException("Error in creating run-time datatable: Copying the datatable failed...");
				}
			}
			File runTimeCommonDatatable = new File(runTimeDatatablePath + Util.getFileSeparator() + "Common Testdata.xls");
			if (!runTimeCommonDatatable.exists()) {
				File commonDatatable = new File(datatablePath + Util.getFileSeparator() + "Common Testdata.xls");
				try {
					FileUtils.copyFile(commonDatatable, runTimeCommonDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException("Error in creating run-time datatable: Copying the common datatable failed...");
				}
			}
		} else {
			runTimeDatatablePath = datatablePath;
		}
		dataTable = new CraftDataTable(runTimeDatatablePath, testParameters.getCurrentScenario());
	}
	
	private void initializeTestScript() {
		scriptHelper = new ScriptHelper(dataTable, report, driver);
		businessFlowData = getBusinessFlow();
	}
	
	private ArrayList<String> getBusinessFlow() {
		ExcelDataAccess businessFlowAccess = new ExcelDataAccess(frameworkParameters.getRelativePath() +
				Util.getFileSeparator() + "Datatables", testParameters.getCurrentScenario());
		businessFlowAccess.setDatasheetName("Business_Flow");
		int rowNum = businessFlowAccess.getRowNum(testParameters.getCurrentTestcase(), 0);
		if (rowNum == -1) {
			throw new FrameworkException("The test case \"" + testParameters.getCurrentTestcase() + "\" is not found in the Business Flow sheet!");
		}
		String dataValue;
		ArrayList<String> businessFlowData = new ArrayList<String>();
		int currentColumnNum = 1;
		while (true) {
			dataValue = businessFlowAccess.getValue(rowNum, currentColumnNum);
			if (dataValue.equals("")) {
				break;
			}
			businessFlowData.add(dataValue);
			currentColumnNum++;
		}
		if (businessFlowData.size() == 0) {
			throw new FrameworkException("No business flow found against the test case \"" + testParameters.getCurrentTestcase() + "\"");
		}
		return businessFlowData;
	}
	
	private synchronized void initializeTestIterations() {
		switch(testParameters.getIterationMode()) {
		case RunAllIterations:
			String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
			ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath, testParameters.getCurrentScenario());
			testDataAccess.setDatasheetName(properties.getProperty("DefaultDataSheet"));
			
			int startRowNum = testDataAccess.getRowNum(testParameters.getCurrentTestcase(), 0);
			int nTestcaseRows = testDataAccess.getRowCount(testParameters.getCurrentTestcase(), 0, startRowNum);
			int nSubIterations = testDataAccess.getRowCount("1", 1, startRowNum);	// Assumption: Every test case will have at least one iteration
			int nIterations = nTestcaseRows / nSubIterations;
			testParameters.setEndIteration(nIterations);
			
			currentIteration = 1;
			break;
			
		case RunOneIterationOnly:
			currentIteration = 1;
			break;
			
		case RunRangeOfIterations:
			if(testParameters.getStartIteration() > testParameters.getEndIteration()) {
				throw new FrameworkException("Error","StartIteration cannot be greater than EndIteration!");
			}
			currentIteration = testParameters.getStartIteration();
			break;
		}
	}
	
	/**
	 * Function to do required setup activities before starting the test
	 * execution
	 */
	protected void setUp() {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(properties.getProperty("ApplicationUrl"));
	}
	
	private void executeTestIterations() {
		while (currentIteration <= testParameters.getEndIteration()) {
			report.addTestLogSection("Iteration: " + Integer.toString(currentIteration));
			try {
				executeTestcase(businessFlowData);
			} catch (FrameworkException fx) {
				exceptionHandler(fx, fx.errorName);
			} catch (InvocationTargetException ix) {
				exceptionHandler((Exception) ix.getCause(), "Error");
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			}
			currentIteration++;
		}
	}
	
	private void executeTestcase(ArrayList<String> businessFlowData) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException, InstantiationException {
		HashMap<String, Integer> keywordDirectory = new HashMap<String, Integer>();
		for (int currentKeywordNum = 0; currentKeywordNum < businessFlowData.size(); currentKeywordNum++) {
			String[] currentFlowData = businessFlowData.get(currentKeywordNum).split(",");
			String currentKeyword = currentFlowData[0];
			
			int nKeywordIterations;
			if(currentFlowData.length > 1) {
				nKeywordIterations = Integer.parseInt(currentFlowData[1]);
			} else {
				nKeywordIterations = 1;
			}
			
			for (int currentKeywordIteration = 0; currentKeywordIteration < nKeywordIterations; currentKeywordIteration++) {
				if(keywordDirectory.containsKey(currentKeyword)) {
					keywordDirectory.put(currentKeyword, keywordDirectory.get(currentKeyword) + 1);
				} else {
					keywordDirectory.put(currentKeyword, 1);
				}
				currentSubIteration = keywordDirectory.get(currentKeyword);
				dataTable.setCurrentRow(testParameters.getCurrentTestcase(), currentIteration, currentSubIteration);
				if (currentSubIteration > 1) {
					report.addTestLogSubSection(currentKeyword + " (Sub-Iteration: " + currentSubIteration + ")");
				} else {
					report.addTestLogSubSection(currentKeyword);
				}
				invokeBusinessComponent(currentKeyword);
			}
		}
	}
	
	private void invokeBusinessComponent(String currentKeyword) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException, InstantiationException {
		Boolean isMethodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";
		File[] packageDirectories = {
				new File(frameworkParameters.getRelativePath() + Util.getFileSeparator() + "businesscomponents"),
				new File(frameworkParameters.getRelativePath() + Util.getFileSeparator() + "componentgroups") };

		for (File packageDirectory : packageDirectories) {
			File[] packageFiles = packageDirectory.listFiles();
			String packageName = packageDirectory.getName();

			for (int i = 0; i < packageFiles.length; i++) {
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();
				
				// We only want the .class files
				if (fileName.endsWith(CLASS_FILE_EXTENSION)) {
					// Remove the .class extension to get the class name
					String className = fileName.substring(0, fileName.length() - CLASS_FILE_EXTENSION.length());
					Class<?> reusableComponents = Class.forName(packageName + "." + className);
					Method executeComponent;
					
					try {
						// Convert the first letter of the method to lowercase (in line with java naming conventions)
						currentKeyword = currentKeyword.substring(0, 1).toLowerCase() + currentKeyword.substring(1);
						executeComponent = reusableComponents.getMethod(currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException ex) {
						// If the method is not found in this class, search the next class
						continue;
					}
					isMethodFound = true;
					Constructor<?> ctor = reusableComponents.getDeclaredConstructors()[0];
					Object businessComponent = ctor.newInstance(scriptHelper);
					executeComponent.invoke(businessComponent, (Object[]) null);
					break;
				}
			}
		}
		if (!isMethodFound) {
			throw new FrameworkException("Keyword " + currentKeyword + " not found within any class " + "inside the businesscomponents package");
		}
	}
	
	private void exceptionHandler(Exception ex, String exceptionName) {
		// Error reporting
		String exceptionDescription = ex.getMessage();
		if (exceptionDescription == null) {
			exceptionDescription = ex.toString();
		}

		if (ex.getCause() != null) {
			report.updateTestLog(exceptionName, exceptionDescription + " <b>Caused by: </b>" + ex.getCause(), Status.FAIL);
		} else {
			report.updateTestLog(exceptionName, exceptionDescription, Status.FAIL);
		}
		ex.printStackTrace();

		// Error response
		if (frameworkParameters.getStopExecution()) {
			report.updateTestLog("CRAFT Info", "Test execution terminated by user! All subsequent tests aborted...", Status.DONE);
		} else {
			OnError onError = OnError.valueOf(properties.getProperty("OnError"));
			switch (onError) {
			case NextIteration:
				report.updateTestLog("CRAFT Info", "Test case iteration terminated by user! Proceeding to next iteration (if applicable)...", Status.DONE);
				currentIteration++;
				executeTestIterations();
				break;

			case NextTestCase:
				report.updateTestLog("CRAFT Info", "Test case terminated by user! Proceeding to next test case (if applicable)...", Status.DONE);
				break;

			case Stop:
				frameworkParameters.setStopExecution(true);
				report.updateTestLog("CRAFT Info", "Test execution terminated by user! All subsequent tests aborted...", Status.DONE);
				break;
			}
		}
		// Wrap up execution
		tearDown();
		wrapUp();
	}
	
	/**
	 * Function to do required teardown activities at the end of the test
	 * execution
	 */
	protected void tearDown() {
		driver.quit();
	}

	private void wrapUp() {
		endTime = Util.getCurrentTime();
		closeTestReport();
		if (report.getTestStatus().equalsIgnoreCase("Failed")) {
			Assert.fail(report.getFailureDescription());
		}
	}

	private void closeTestReport() {
		String executionTime = Util.getTimeDifference(startTime, endTime);
		report.addTestLogFooter(executionTime);
	}
}