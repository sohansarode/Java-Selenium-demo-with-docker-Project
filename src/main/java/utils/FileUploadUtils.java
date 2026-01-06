package utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.BrowserSetup;

public class FileUploadUtils {

	
	private static WebDriver getDriver() {
		return BrowserSetup.getDriver(); // fetch ThreadLocal driver safely
	}
	//========================================-- Upload files Methods Starts --===============================================================//

		public static void Upload_File_By_JS(WebElement inputElement, String filePath) {
			ReportUtils.info("Uploading file using JavaScript : " + filePath);

			File file = new File(filePath);
			if (!file.exists()) {
				ReportUtils.info("File not found: " + filePath);
				throw new IllegalArgumentException("File not found: " + filePath);
			}

			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			js.executeScript("arguments[0].value = arguments[1];", inputElement, filePath);
			ReportUtils.info("File uploaded successfully via JavaScript");
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Upload_File_By_SendKeys(WebElement inputElement, String filePath) {
			ReportUtils.info("Uploading file using sendKeys : " + filePath);

			File file = new File(filePath);
			if (!file.exists()) {
				ReportUtils.info("File not found : " + filePath);
				throw new IllegalArgumentException("File not found : " + filePath);
			}

			inputElement.sendKeys(filePath);
			ReportUtils.info("File uploaded successfully via sendKeys");
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Upload_File_By_Robot(WebElement inputElement, String filePath) {
			ReportUtils.info("Uploading file using Robot class : " + filePath);

			File file = new File(filePath);
			if (!file.exists()) {
				ReportUtils.info("File not found : " + filePath);
				throw new IllegalArgumentException("File not found : " + filePath);
			}

			try {
				inputElement.click();

				Robot robot = new Robot();
				StringSelection stringSelection = new StringSelection(filePath);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				ReportUtils.info("File uploaded successfully via Robot class");
			} catch (AWTException e) {
				ReportUtils.info("Failed to upload file using Robot class : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Upload_File_From_Local(String filePath) {
			ReportUtils.info("Uploading local file : " + filePath);

			File file = new File(filePath);
			if (!file.exists()) {
				ReportUtils.info("File not found : " + filePath);
				throw new IllegalArgumentException("File not found : " + filePath);
			}

			try {
				StringSelection ss = new StringSelection(filePath);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				Thread.sleep(1000);

				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				ReportUtils.info("Local file uploaded successfully");
			} catch (AWTException | InterruptedException e) {
				ReportUtils.info("Failed to upload local file : " + e.getMessage());
			}
		}
	//========================================-- Upload files Methods Ends --===============================================================//

}
