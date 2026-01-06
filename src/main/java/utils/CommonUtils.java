package utils;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import base.BrowserSetup;

public class CommonUtils {

	

	private static WebDriver getDriver() {
		return BrowserSetup.getDriver(); // fetch ThreadLocal driver safely
	}

	

//========================================-- Log Methods Starts --======================================================================//
	// Logs information along with the class name of the caller.
	private static void logInfos(Object obj) {
		// Get the current call stack
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		// Default class name (if not enough stack frames)
		String className = "Unknown";

		// Check if there are enough stack frames to retrieve the caller's class name
		if (stackTrace.length >= 4) {
			// Retrieve the caller's class name
			className = stackTrace[3].getClassName();
		}

		// Log the class name and object's string representation
		ConfigReader.logger.info(className + " ===>  " + obj.toString());
	}




//========================================-- Web Table Methods Starts --=================================================================//

	public static void Webtable(List<WebElement> Rows, List<WebElement> Columns) {
		ReportUtils.info("Processing web table");

		int rowCount = Rows.size();
		ReportUtils.info("Number of rows : " + rowCount);

		int columnCount = Columns.size();
		ReportUtils.info("Number of columns : " + columnCount);

		WebElement cellAddress = getDriver().findElement(null);
		String value = cellAddress.getText();
		ReportUtils.info("Cell valu e: " + value);
	}






//-----------------------------------------------------------------------------------------------------------------------------------------//

	public static void Click_Data_In_CusNo_Column(String dataToClick, String cusNoHeaderText) {
		logInfos("Clicking data in CusNo column - Header: " + cusNoHeaderText + ", Data : " + dataToClick);

		WebElement cusNoHeader = getDriver().findElement(
				By.xpath("//table[@class=\"table cus-table-row-spacing mb-0\"]//th[text()='" + cusNoHeaderText + "']"));

		int cusNoColumnIndex = Get_Index_Of_Element(cusNoHeader);
		ReportUtils.info("Found CusNo column at index: " + cusNoColumnIndex);

		List<WebElement> rows = getDriver().findElements(
				By.xpath("//table[@class=\"table cus-table-row-spacing mb-0\"]/tbody/tr[@class=\"bg-white\"]"));

		for (WebElement row : rows) {
			WebElement cusNoCell = row.findElement(By.xpath("./td[" + cusNoColumnIndex + "]"));
			if (cusNoCell.getText().equals(dataToClick)) {
				cusNoCell.click();
				ReportUtils.info("Clicked on matching cell with data : " + dataToClick);
				break;
			}
		}
	}

//-----------------------------------------------------------------------------------------------------------------------------------------//

	private static int Get_Index_Of_Element(WebElement Element) {
		ReportUtils.info("Getting index of element");

		List<WebElement> siblings = Element.findElements(By.xpath("./preceding-sibling::*"));
		int index = siblings.size() + 1;
		ReportUtils.info("Element index: " + index);
		return index;
	}

//-----------------------------------------------------------------------------------------------------------------------------------------//

	public static String getElementText(WebElement element) {
		ReportUtils.info("Getting text from element using multiple methods");

		if (element == null) {
			ReportUtils.info("Element is null");
			return "Element is null";
		}

		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(element));

		String text = element.getText().trim();
		if (!text.isEmpty()) {
			ReportUtils.info("Found text using getText(): " + text);
			return text;
		}

		text = element.getAttribute("value");
		if (text != null && !text.trim().isEmpty()) {
			ReportUtils.info("Found text using value attribute: " + text);
			return text;
		}

		text = element.getAttribute("placeholder");
		if (text != null && !text.trim().isEmpty()) {
			ReportUtils.info("Found text using placeholder attribute: " + text);
			return text;
		}

		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		text = (String) js.executeScript("return arguments[0].textContent.trim();", element);
		if (text != null && !text.isEmpty()) {
			ReportUtils.info("Found text using JavaScript: " + text);
			return text;
		}

		ReportUtils.info("No text found in element");
		return "No text available";
	}

//-----------------------------------------------------------------------------------------------------------//

	public static String GetElementName(WebElement element) {
		if (element == null) {
			return "Null WebElement";
		}

		String elementName = "";

		try {
			// 1. Try getAccessibleName (if supported)
			elementName = element.getAccessibleName();

			// 2. Try useful attributes
			if (elementName == null || elementName.trim().isEmpty())
				elementName = element.getAttribute("aria-label");

			if (elementName == null || elementName.trim().isEmpty())
				elementName = element.getAttribute("placeholder");

			if (elementName == null || elementName.trim().isEmpty())
				elementName = element.getAttribute("name");

			if (elementName == null || elementName.trim().isEmpty())
				elementName = element.getAttribute("id");

			if (elementName == null || elementName.trim().isEmpty())
				elementName = element.getAttribute("title");

			if (elementName == null || elementName.trim().isEmpty())
				elementName = element.getAttribute("value");

			// 3. Try visible text
			if (elementName == null || elementName.trim().isEmpty())
				elementName = element.getText();

			// 4. Try using JS to fetch label text
			if ((elementName == null || elementName.trim().isEmpty()) && getDriver() instanceof JavascriptExecutor) {
				JavascriptExecutor js = (JavascriptExecutor) getDriver();
				elementName = (String) js.executeScript("var el = arguments[0];"
						+ "if (el.labels && el.labels.length > 0) return el.labels[0].innerText;"
						+ "if (el.getAttribute('aria-label')) return el.getAttribute('aria-label');"
						+ "if (el.getAttribute('title')) return el.getAttribute('title');"
						+ "return el.innerText || el.getAttribute('value') || el.getAttribute('name') || el.getAttribute('id');",
						element);
			}

			// 5. Final fallback
			if (elementName == null || elementName.trim().isEmpty())
				elementName = element.toString();

		} catch (Exception e) {
			elementName = "UnknownElement(" + element.toString() + ")";
		}

		return elementName.trim();
	}
//-----------------------------------------------------------------------------------------------------------//

	static void highlightElement(WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			js.executeScript("arguments[0].style.border='3px solid " + ConfigReader.prop.getProperty("color") + "'",
					element);
			// Optional: keep highlight for 200ms
			Thread.sleep(200);
			js.executeScript("arguments[0].style.border=''", element); // remove highlight
		} catch (Exception e) {

		}
	}
}