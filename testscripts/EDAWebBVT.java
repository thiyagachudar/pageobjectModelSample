package testscripts;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import supportlibraries.Browser;
import supportlibraries.DriverScript;
import supportlibraries.TestParameters;

import com.cognizant.framework.IterationOptions;

/**
 *@Author 
 * 
 */
@RunWith(Parameterized.class)
public class EDAWebBVT  extends DriverScript {

	@Parameters
	public static Collection<Object[]> data() {
			Object[][] data = new Object[][] {
					{ "BVT_Scenario", "TC13", IterationOptions.RunOneIterationOnly, Browser.firefox }
					};
			return Arrays.asList(data);
		}

		private TestParameters tParameters = new TestParameters();
		
		public EDAWebBVT(String scenario, String tcName, IterationOptions iter, Browser browser) {
			tParameters.setCurrentScenario(scenario);
			tParameters.setCurrentTestcase(tcName);
			tParameters.setIterationMode(iter);
			tParameters.setBrowser(browser);
		}

		@Test
		public void testScripts() {
			testParameters = tParameters;
			driveTestExecution();
		}
}

