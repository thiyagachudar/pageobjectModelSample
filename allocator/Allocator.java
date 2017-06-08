package allocator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import supportlibraries.Browser;
import supportlibraries.ExecutionParameters;
import supportlibraries.PlatformFactory;
import supportlibraries.SeleniumReport;
import supportlibraries.TestParameters;
import supportlibraries.TimeStamp;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.ReportTheme;
import com.cognizant.framework.ReportThemeFactory;
import com.cognizant.framework.ReportThemeFactory.Theme;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Util;

/**
 * Class to manage the batch execution of test scripts within the framework
 * 
 * @author Cognizant
 * @version 3.0
 * @since October 2011
 */
public class Allocator {
	private static ArrayList<TestParameters> testInstancesToRun;
	private static SeleniumReport report;
	private static Properties properties;
	private static FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	private static Date startTime, endTime;
	private static ReportSettings reportSettings;
	private static String timeStamp;
	private static String reportPath;
	private static ExecutionParameters eParam;
	private static int totalPassed = 0;
	private static int totalFailed = 0;
	private static String overallExecStatus = null;
	
	public static void main(String[] args) throws FileNotFoundException {
		setRelativePath();
		setExecutionParameters(args);
		
		initializeTestBatch();
		initializeSummaryReport();
		setupErrorLog();

		driveBatchExecution();
		wrapUp();
		consoleMessage();
		System.exit(0);
	}
	
	private static void setRelativePath() {
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if (relativePath.contains("allocator")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}

	private static void setExecutionParameters(String[] args) {
		eParam = new ExecutionParameters();
		if (args != null && args.length > 0) {
			if (args.length >= 1 && args[0] != null && !args[0].trim().equals(""))
				eParam.setRunConfiguration(args[0]);
			if (args.length >= 2 && args[1] != null && !args[1].trim().equals(""))
				eParam.setReportFolderName(args[1]);
		}
		if (eParam.getRunConfiguration() == null || eParam.getRunConfiguration().trim().equals("")) {
			properties = Settings.getInstance();
			eParam.setRunConfiguration(properties.getProperty("RunConfiguration"));
		}
	}

	private static void initializeTestBatch() {
		startTime = Util.getCurrentTime();
		properties = Settings.getInstance();
		testInstancesToRun = getRunInfo();
	}

	private static void initializeSummaryReport() {
		frameworkParameters.setRunConfiguration(eParam.getRunConfiguration());
		timeStamp = TimeStamp.getInstance(eParam.getReportFolderName());

		reportSettings = initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));
		report = new SeleniumReport(reportSettings, reportTheme);

		report.initializeReportTypes();
		report.initializeResultSummary();
		report.addResultSummaryHeading(reportSettings.getProjectName() + " - " + " Automation Execution Result Summary");
		report.addResultSummarySubHeading("Date & Time", ": "+ Util.getCurrentFormattedTime(properties.getProperty("DateFormatString")), "OnError", ": " + properties.getProperty("OnError"));
		report.addResultSummaryTableHeadings();
	}

	private static ReportSettings initializeReportSettings() {
		reportPath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Results" + Util.getFileSeparator() + timeStamp;
		ReportSettings reportSettings = new ReportSettings(reportPath, "");

		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.generateExcelReports = Boolean.parseBoolean(properties.getProperty("ExcelReport"));
		reportSettings.generateHtmlReports = Boolean.parseBoolean(properties.getProperty("HtmlReport"));
		return reportSettings;
	}

	private static void setupErrorLog() throws FileNotFoundException {
		String errorLogFile = reportPath + Util.getFileSeparator() + "ErrorLog.txt";
		System.setErr(new PrintStream(new FileOutputStream(errorLogFile)));
	}

	private static ArrayList<TestParameters> getRunInfo() {
		ExcelDataAccess runManagerAccess = new ExcelDataAccess(frameworkParameters.getRelativePath(), "Run Manager");
		runManagerAccess.setDatasheetName(eParam.getRunConfiguration());

		int nTestInstances = runManagerAccess.getLastRowNum();
		ArrayList<TestParameters> testInstancesToRun = new ArrayList<TestParameters>();
		for (int currentTestInstance = 1; currentTestInstance <= nTestInstances; currentTestInstance++) {
			String executeFlag = runManagerAccess.getValue(currentTestInstance, "Execute");
			if (executeFlag.equalsIgnoreCase("Yes")) {
				TestParameters testParameters = new TestParameters();
				
				testParameters.setExecParam(eParam);
				testParameters.setCurrentScenario(runManagerAccess.getValue(currentTestInstance, "Test_Scenario"));
				testParameters.setCurrentTestcase(runManagerAccess.getValue(currentTestInstance, "Test_Case"));
				testParameters.setCurrentTestDescription(runManagerAccess.getValue(currentTestInstance, "Description"));
				testParameters.setIterationMode(IterationOptions.valueOf(runManagerAccess.getValue(currentTestInstance, "Iteration_Mode")));
				
				String startIteration = runManagerAccess.getValue(currentTestInstance, "Start_Iteration");
				if (!startIteration.equals("")) {
					testParameters.setStartIteration(Integer.parseInt(startIteration));
				}
				String endIteration = runManagerAccess.getValue(currentTestInstance, "End_Iteration");
				if (!endIteration.equals("")) {
					testParameters.setEndIteration(Integer.parseInt(endIteration));
				}

				String browser = runManagerAccess.getValue(currentTestInstance, "Browser");				
				if (!browser.equals("")) {
					testParameters.setBrowser(Browser.valueOf(browser));
				}
				String browserVersion = runManagerAccess.getValue(currentTestInstance, "Browser_Version");
				if (!browserVersion.equals("")) {
					testParameters.setBrowserVersion(browserVersion);
				}
				String platform = runManagerAccess.getValue(currentTestInstance, "Platform");
				if (!platform.equals("")) {
					testParameters.setPlatform(PlatformFactory.getPlatform(platform));
				}
				testInstancesToRun.add(testParameters);
			}
		}
		return testInstancesToRun;
	}

	private static void driveBatchExecution() {
		int nThreads = Integer.parseInt(properties.getProperty("NumberOfThreads"));
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(nThreads);

		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun.size(); currentTestInstance++) {
			ParallelRunner testRunner = new ParallelRunner(testInstancesToRun.get(currentTestInstance), report);
			parallelExecutor.execute(testRunner);
			if (frameworkParameters.getStopExecution()) {
				break;
			}
		}
		parallelExecutor.shutdown();
		while (!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void wrapUp() throws FileNotFoundException {
		endTime = Util.getCurrentTime();
		closeSummaryReport();
		
		String openReport = properties.getProperty("OpenSummaryReport");
		if (openReport != null && openReport.trim().equalsIgnoreCase("true")) {
			if (reportSettings.generateHtmlReports) {
				try {
					Runtime.getRuntime().exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + reportPath + "\\HTML Results\\Summary.Html");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (reportSettings.generateExcelReports) {
				try {
					Runtime.getRuntime().exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + reportPath + "\\Excel Results\\Summary.xls");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void closeSummaryReport() {
		String totalExecutionTime = Util.getTimeDifference(startTime, endTime);
		report.addResultSummaryFooter(totalExecutionTime);
	}
	
	private static void consoleMessage() {
		setExecutionStatus();
		setupStatusLog();
		String summaryPath = reportPath.replace('\\', '/');
		if (summaryPath.indexOf("jobs") != -1) {
			summaryPath = summaryPath.substring(summaryPath.indexOf("jobs"));
			summaryPath = "file://///10.0.66.76/" + summaryPath;
		}
		System.out.println("");
		System.out.println("TOTAL EXECUTED: " + (totalPassed + totalFailed) + "	====>	PASSED: " + totalPassed + " & FAILED: " + totalFailed);
		System.out.println("HTML SUMMARY RESULT: " + summaryPath + "/HTML%20Results/Summary.html");
		System.out.println("");
		if (!overallExecStatus.equalsIgnoreCase("Passed")) {
			System.exit(1);
		}
	}

	private static void setExecutionStatus() {
		overallExecStatus = "No Run";
		if (testInstancesToRun != null && testInstancesToRun.size() > 0) {
			totalPassed = testInstancesToRun.get(0).getExecParam().getTotalPassed();
			totalFailed = testInstancesToRun.get(0).getExecParam().getTotalFailed();
			if (totalFailed > 0) {
				overallExecStatus = "Failed";
			} else if (totalPassed != 0) {
				overallExecStatus = "Passed";
			}
		}
	}
	
	private static void setupStatusLog() {
		String statusFile = reportPath + Util.getFileSeparator() + "Status.txt";
		try (FileOutputStream fop = new FileOutputStream(statusFile)) {
			fop.write(overallExecStatus.getBytes());
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}