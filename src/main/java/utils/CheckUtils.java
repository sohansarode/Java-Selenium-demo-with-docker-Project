package utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BrowserSetup;

public class CheckUtils {
	private static WebDriver getDriver() {
		return BrowserSetup.getDriver(); // fetch ThreadLocal driver safely
	}
	
	//========================================-- Boolean Methods Starts --=========================================================================//	

		public static boolean Is_Displayed(WebElement Element) {
			String elementName = CommonUtils.GetElementName(Element);
			ReportUtils.info("Checking if element is displayed : " + elementName);

			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
				wait.until(ExpectedConditions.visibilityOf(Element));

				boolean isDisplayed = Element.isDisplayed();
				ReportUtils.info("Element displayed status : " + isDisplayed);
				return isDisplayed;
			} catch (Exception e) {
				ReportUtils.failTest("Element not displayed : " + e.getMessage());
				return false;
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static boolean Is_Clickable(WebElement Element) {
			String elementName = CommonUtils.GetElementName(Element);
			ReportUtils.info("Checking if element is clickable : " + elementName);

			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
				wait.until(ExpectedConditions.elementToBeClickable(Element));

				boolean isClickable = Element.isEnabled();
				ReportUtils.info("Element clickable status : " + isClickable);
				return isClickable;
			} catch (Exception e) {
				ReportUtils.failTest("Element not clickable : " + e.getMessage());
				return false;
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static boolean Is_Enabled(WebElement Element) {
			String elementName = CommonUtils.GetElementName(Element);
			ReportUtils.info("Checking if element is enabled : " + elementName);

			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
				wait.until(ExpectedConditions.visibilityOf(Element));

				boolean isEnabled = Element.isEnabled();
				ReportUtils.info("Element enabled status : " + isEnabled);
				return isEnabled;
			} catch (Exception e) {
				ReportUtils.failTest("Element not enabled : " + e.getMessage());
				return false;
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static boolean Is_Selected(WebElement Element) {
			String elementName = CommonUtils.GetElementName(Element);
			ReportUtils.info("Checking if element is selected : " + elementName);

			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
				wait.until(ExpectedConditions.visibilityOf(Element));

				boolean isSelected = Element.isSelected();
				ReportUtils.info("Element selected status : " + isSelected);
				return isSelected;
			} catch (Exception e) {
				ReportUtils.failTest("Element not selected : " + e.getMessage());
				return false;
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static boolean Is_Present(WebElement Element) {
			String elementName = CommonUtils.GetElementName(Element);
			ReportUtils.info("Checking if element is present : " + elementName);

			try {
				boolean isPresent = Element != null;
				ReportUtils.info("Element present status : " + isPresent);
				return isPresent;
			} catch (Exception e) {
				ReportUtils.failTest("Element presence check failed : " + e.getMessage());
				return false;
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static boolean Contains_Text(WebElement Element, String text) {
			String elementName = CommonUtils.GetElementName(Element);
			ReportUtils.info("Checking if element contains text : '" + text + "' in " + elementName);

			try {
				JavascriptExecutor js = (JavascriptExecutor) getDriver();
				boolean containsText = (Boolean) js.executeScript("return arguments[0].textContent.includes(arguments[1]);",
						Element, text);

				ReportUtils.info("Element contains text status : " + containsText);
				return containsText;
			} catch (Exception e) {
				ReportUtils.failTest("Error checking text in element : " + e.getMessage());
				return false;
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

	//========================================-- Boolean Methods Ends --=======================================================================//

		
		//========================================-- Utility Methods Starts --================================================================//

		public static List<String> Get_Text_From_WebElement_List(List<WebElement> elementList) {
			ReportUtils.info("Getting text from WebElement list of size : " + elementList.size());

			List<String> textList = new ArrayList<>();
			for (WebElement element : elementList) {
				textList.add(element.getText().trim()); // Trim to remove leading/trailing spaces
			}

			ReportUtils.info("Retrieved " + textList.size() + " text elements");
			return textList;
		}

	//-----------------------------------------------------------------------------------------------------------------------------------------//

		public static boolean Compare_WebElement_Text_Contains(List<WebElement> List1, List<WebElement> List2) {
			ReportUtils.info("Comparing text between two WebElement lists");

			if (List1.size() != List2.size()) {
				ReportUtils.info("Lists have different sizes : " + List1.size() + " vs " + List2.size());
				return false;
			}

			boolean result = IntStream.range(0, List1.size())
					.allMatch(i -> List1.get(i).getText().contains(List2.get(i).getText())
							|| List2.get(i).getText().contains(List1.get(i).getText()));

			ReportUtils.info("Text comparison result : " + result);
			return result;
		}

	//-----------------------------------------------------------------------------------------------------------------------------------------//

		public static boolean compareListTextContains(List<String> cellTexts, List<WebElement> list2) {
			ReportUtils.info("Comparing text between String list and WebElement list");

			if (cellTexts.size() != list2.size()) {
				ReportUtils.info("Lists have different sizes : " + cellTexts.size() + " vs " + list2.size());
				return false;
			}

			for (int i = 0; i < cellTexts.size(); i++) {
				String text1 = cellTexts.get(i);
				String text2 = list2.get(i).getText();

				if (!(text1.contains(text2) || text2.contains(text1))) {
					ReportUtils.info("Text mismatch at index " + i);
					return false;
				}
			}

			ReportUtils.info("All texts match");
			return true;
		}

	//-----------------------------------------------------------------------------------------------------------------------------------------//

		public static boolean Compare_String_List_Ignoring_Icons(List<String> list1, List<String> list2) {
			ReportUtils.info("Comparing string lists ignoring icons");

			if (list1.size() != list2.size()) {
				ReportUtils.info("Lists have different sizes : " + list1.size() + " vs " + list2.size());
				return false;
			}

			boolean result = IntStream.range(0, list1.size()).allMatch(i -> {
				String text1 = list1.get(i).replaceAll("[↓↑]", "").trim();
				String text2 = list2.get(i).replaceAll("[↓↑]", "").trim();
				return text1.contains(text2) || text2.contains(text1);
			});

			ReportUtils.info("String comparison result : " + result);
			return result;
		}

	//-----------------------------------------------------------------------------------------------------------------------------------------//

		public List<String> Get_Text_Of_Checked_Options(List<WebElement> List1) {
			ReportUtils.info("Getting text of checked options");

			List<String> checkedOptionsText = new ArrayList<>();

			for (WebElement option : List1) {
				WebElement checkbox = option.findElement(By.xpath("preceding-sibling::input[@type='checkbox']"));
				if (checkbox.isSelected()) {
					checkedOptionsText.add(option.getText());
					ReportUtils.info("Found checked option : " + option.getText());
				}
			}

			ReportUtils.info("Found " + checkedOptionsText.size() + " checked options");
			return checkedOptionsText;
		}

}
