package allocator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import supportlibraries.*;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Util;


/**
 * Class to facilitate parallel execution of test scripts
 */
public class ParallelRunner implements Runnable
{
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	private TestParameters testParameters;
	private Date startTime, endTime;
	private String testStatus;
	private SeleniumReport report;
	
	
	/**
	 * Constructor to initialize the details of the test case to be executed
	 * @param testParameters The {@link TestParameters} object (passed from the {@link Allocator})
	 * @param report The {@link SeleniumReport} object (passed from the {@link Allocator})
	 */
	public ParallelRunner(TestParameters testParameters, SeleniumReport report)
	{
		super();

		this.testParameters = testParameters;
		this.report = report;
	}
	
	@Override
	public void run()
	{
		startTime = Util.getCurrentTime();
		testStatus = invokeTestScript();
		setTestStatus();
		endTime = Util.getCurrentTime();
		String executionTime = Util.getTimeDifference(startTime, endTime);
		report.updateResultSummary(testParameters.getCurrentScenario(),
									testParameters.getCurrentTestcase(),
									testParameters.getCurrentTestDescription(),
									executionTime, testStatus);
	}
	
	private void setTestStatus() {
		String status = "FAILED*";
		ExecutionParameters execParam = testParameters.getExecParam();
		if (testStatus.equalsIgnoreCase("passed")) {
			status = "PASSED";
			execParam.setTotalPassed(execParam.getTotalPassed() + 1);
		} else {
			execParam.setTotalFailed(execParam.getTotalFailed() + 1);
		}
		testParameters.setExecParam(execParam);
		System.out.println(testParameters.getCurrentScenario() + "/" + testParameters.getCurrentTestcase() + "		"
				+ status + "		" + testParameters.getCurrentTestDescription());
	}
	
	private String invokeTestScript()
	{
		if(frameworkParameters.getStopExecution()) {
			testStatus = "Test Execution Aborted";
		} else {
			try {
				Class<?> driverScriptClass = Class.forName("supportlibraries.DriverScript");
				Object driverScript = driverScriptClass.newInstance();

				Field testParameters = driverScriptClass.getDeclaredField("testParameters");
				testParameters.setAccessible(true);
				testParameters.set(driverScript, this.testParameters);

				Method driveTestExecution =
						driverScriptClass.getMethod("driveTestExecution", (Class<?>[]) null);
				driveTestExecution.invoke(driverScript, (Object[]) null);

				Field testReport = driverScriptClass.getDeclaredField("report");
				testReport.setAccessible(true);
				SeleniumReport report = (SeleniumReport) testReport.get(driverScript);
				testStatus = report.getTestStatus();
			} catch (ClassNotFoundException e) {
				testStatus = "Reflection Error - ClassNotFoundException";
				e.printStackTrace();
			}  catch (IllegalArgumentException e) {
				testStatus = "Reflection Error - IllegalArgumentException";
				e.printStackTrace();
			} catch (InstantiationException e) {
				testStatus = "Reflection Error - InstantiationException";
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				testStatus = "Reflection Error - IllegalAccessException";
				e.printStackTrace();
			} catch (SecurityException e) {
				testStatus = "Reflection Error - SecurityException";
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				testStatus = "Reflection Error - NoSuchFieldException";
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				testStatus = "Reflection Error - NoSuchMethodException";
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				testStatus = "Failed";
				e.printStackTrace();
			}
		}

		return testStatus;
	}
}