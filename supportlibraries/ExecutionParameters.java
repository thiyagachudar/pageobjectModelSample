package supportlibraries;

public class ExecutionParameters {

	private String runConfiguration = null;
	private String reportFolderName = null;
	private int totalPassed = 0;
	private int totalFailed = 0;

	/**
	 * @return the runConfiguration
	 */
	public String getRunConfiguration() {
		return runConfiguration;
	}

	/**
	 * @param runConfiguration
	 *            the runConfiguration to set
	 */
	public void setRunConfiguration(String runConfiguration) {
		this.runConfiguration = runConfiguration;
	}

	/**
	 * @return the reportFolderName
	 */
	public String getReportFolderName() {
		return reportFolderName;
	}

	/**
	 * @param reportFolderName
	 *            the reportFolderName to set
	 */
	public void setReportFolderName(String reportFolderName) {
		this.reportFolderName = reportFolderName;
	}

	/**
	 * @return the totalPassed
	 */
	public int getTotalPassed() {
		return totalPassed;
	}

	/**
	 * @param totalPassed
	 *            the totalPassed to set
	 */
	public void setTotalPassed(int totalPassed) {
		this.totalPassed = totalPassed;
	}

	/**
	 * @return the totalFailed
	 */
	public int getTotalFailed() {
		return totalFailed;
	}

	/**
	 * @param totalFailed
	 *            the totalFailed to set
	 */
	public void setTotalFailed(int totalFailed) {
		this.totalFailed = totalFailed;
	}
}
