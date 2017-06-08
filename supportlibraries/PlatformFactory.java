package supportlibraries;

import org.openqa.selenium.Platform;


public class PlatformFactory
{
	/**
	 * Function to return the appropriate {@link Platform} object based on the platform name passed
	 * @param platformName The name of the platform
	 * @return The corresponding {@link Platform} object
	 */
	public static Platform getPlatform(String platformName)
	{
		Platform  platform = null;
		
		if(platformName.equalsIgnoreCase("windows"))
			platform = Platform.WINDOWS;
		else if(platformName.equalsIgnoreCase("android"))
			platform = Platform.ANDROID;
		else if(platformName.equalsIgnoreCase("any"))
			platform = Platform.ANY;
		else if(platformName.equalsIgnoreCase("xp"))
			platform = Platform.XP;
		else if(platformName.equalsIgnoreCase("vista"))
			platform = Platform.VISTA;
		else if(platformName.equalsIgnoreCase("unix"))
			platform = Platform.UNIX;
		else if(platformName.equalsIgnoreCase("mac"))
			platform = Platform.MAC;
		else if(platformName.equalsIgnoreCase("linux"))
			platform = Platform.LINUX;
		
		return platform;
	}
}