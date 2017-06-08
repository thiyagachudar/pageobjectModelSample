package supportlibraries;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.*;


/**
 * Class to provide common assertions/verifications
 * @author Cognizant
 * @version 3.0
 * @since October 2011
 */
public class Asserts
{
	/**
	 * Function to verify whether a given text is present within the page
	 * @param driver The {@link WebDriver} object
	 * @param textToVerify The text to be verified within the page
	 * @return A boolean value indicating if the searched text is found
	 */
	public boolean isTextPresent(WebDriver driver, String textToVerify)
	{
		textToVerify = textToVerify.replace(" ", "\\s*");
		String pageSource = driver.getPageSource();
		String[] pageSourceLines = pageSource.trim().split("\\n");
		String pageSourceWithoutNewlines = "";
		for (String pageSourceLine : pageSourceLines)
		{
			pageSourceWithoutNewlines += pageSourceLine + " ";
		}
		
		pageSourceWithoutNewlines = pageSourceWithoutNewlines.trim();
		
		Pattern p = Pattern.compile(textToVerify);
		Matcher m = p.matcher(pageSourceWithoutNewlines);
		if(m.find())
			return true;
		
		return false;
	}
}