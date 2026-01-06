package utils;

import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;

import base.BrowserSetup;

public class BrowserUtils {

	private static WebDriver getDriver() {
		return BrowserSetup.getDriver(); // fetch ThreadLocal driver safely
	}

	// ========================================-- Alerts Methods Starts
	// --=========================================================================//

	public static void Handle_Alerts(String Action) {
		ReportUtils.info("Attempting to handle alert with action : " + Action);

		try {
			Alert alert = getDriver().switchTo().alert();
			if ("accept".equalsIgnoreCase(Action)) {
				alert.accept();
				ReportUtils.info("Alert accepted");
			} else if ("dismiss".equalsIgnoreCase(Action)) {
				alert.dismiss();
				ReportUtils.info("Alert dismissed");
			} else {
				ReportUtils.info("Invalid alert action --> " + Action);
			}
		} catch (NoAlertPresentException e) {
			ReportUtils.info("Alert is not present");
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static String Switch_To_Alert_And_Get_Text() {
		ReportUtils.info("Attempting to switch to alert and get text");

		try {
			Alert alert = getDriver().switchTo().alert();
			String alertText = alert.getText();
			ReportUtils.info("Retrieved alert text : " + alertText);
			return alertText;
		} catch (NoAlertPresentException e) {
			ReportUtils.info("No alert present");
			return null;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static void Send_Keys_To_Alert(String text) {
		ReportUtils.info("Attempting to send keys to alert : " + text);

		try {
			Alert alert = getDriver().switchTo().alert();
			alert.sendKeys(text);
			ReportUtils.info("Successfully sent keys to alert");
		} catch (NoAlertPresentException e) {
			ReportUtils.info("No alert present");
		}
	}

	// ========================================-- Alerts Methods Ends
	// --=========================================================================//

	// ========================================-- Windows Methods Starts
	// --======================================================================//

	public static void Switch_To_Different_Browser_Window() {
		ReportUtils.info("Attempting to switch to different browser window");

		String currentWindowHandle = getDriver().getWindowHandle();
		Set<String> windowHandles = getDriver().getWindowHandles();

		for (String nextWindowHandle : windowHandles) {
			if (!currentWindowHandle.equalsIgnoreCase(nextWindowHandle)) {
				getDriver().switchTo().window(nextWindowHandle);
				ReportUtils.info("Switched to window : " + nextWindowHandle);
				break;
			}
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static void Switch_To_Specific_Browser_Window(String windowHandleToSwitch) {
		ReportUtils.info("Attempting to switch to specific browser window : " + windowHandleToSwitch);

		String currentWindowHandle = getDriver().getWindowHandle();
		Set<String> windowHandles = getDriver().getWindowHandles();

		for (String nextWindowHandle : windowHandles) {
			if (!currentWindowHandle.equalsIgnoreCase(nextWindowHandle)) {
				if ("second".equalsIgnoreCase(windowHandleToSwitch)) {
					getDriver().switchTo().window(nextWindowHandle);
					ReportUtils.info("Switched to second window");
					break;
				} else if ("first".equalsIgnoreCase(windowHandleToSwitch)) {
					getDriver().switchTo().window(currentWindowHandle);
					ReportUtils.info("Switched back to first window");
					break;
				}
			}
		}
	}

	// ========================================-- Windows Methods Ends
	// --=========================================================================//

	// ========================================-- Frame Methods Starts
	// --=======================================================================//

	public static void Switch_To_Frame_By_Index(int index) {
		ReportUtils.info("Switching to frame by index : " + index);
		try {
			getDriver().switchTo().frame(index);
			ReportUtils.info("Successfully switched to frame at index : " + index);
		} catch (NoSuchFrameException e) {
			ReportUtils.info("Frame not found at index : " + index);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static void Switch_Out_Of_Frame() {
		ReportUtils.info("Switching out of all frames to default content");
		getDriver().switchTo().defaultContent();
		ReportUtils.info("Successfully switched to default content");
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static void Switch_To_Frame_By_Name_Or_Id(String frameNameOrId) {
		ReportUtils.info("Switching to frame by name/id : " + frameNameOrId);
		try {
			getDriver().switchTo().frame(frameNameOrId);
			ReportUtils.info("Successfully switched to frame : " + frameNameOrId);
		} catch (NoSuchFrameException e) {
			ReportUtils.info("Frame not found with name/id : " + frameNameOrId);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static void Switch_To_Parent_Frame() {
		ReportUtils.info("Switching to parent frame");
		getDriver().switchTo().parentFrame();
		ReportUtils.info("Successfully switched to parent frame");
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static int Get_Number_Of_Frames() {
		ReportUtils.info("Getting the total number of frames on the page");
		int numberOfFrames = getDriver().findElements(By.tagName("iframe")).size();
		ReportUtils.info("Total number of frames found: " + numberOfFrames);
		return numberOfFrames;
	}

	// ========================================-- Frame Methods Ends
	// --=======================================================================//
	// ========================================-- Navigation Methods Starts
	// --====================================================================//

	public static void Navigate_Back() {
		ReportUtils.info("Navigating back");

		getDriver().navigate().back();
		ReportUtils.info("Navigated back to previous page");
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static void Navigate_Forward() {
		ReportUtils.info("Navigating forward");

		getDriver().navigate().forward();
		ReportUtils.info("Navigated forward to next page");
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	public static void Refresh_Page() {
		ReportUtils.info("Refreshing page");

		getDriver().navigate().refresh();
		ReportUtils.info("Page refreshed");
	}

	// ========================================-- Navigation Methods Ends
	// --=====================================================================//

}
