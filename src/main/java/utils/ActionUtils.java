package utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BrowserSetup;

public class ActionUtils {
	
	private static WebDriver getDriver() {
		return BrowserSetup.getDriver(); // fetch ThreadLocal driver safely
	}
	
	//========================================-- All Click Methods Starts --=====================================================================//

		public static void Click(WebElement Element) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

			try {
				RetryUtils.retryWithTryCatch(() -> {

					wait.until(ExpectedConditions.elementToBeClickable(Element));

					String elementName = CommonUtils.GetElementName(Element);

					CommonUtils.highlightElement(Element);

					ReportUtils.info("Attempting to click element : " + elementName);

					Element.click();

					ReportUtils.info("Successfully clicked element : " + elementName);
				}, "Click");
			} catch (TimeoutException e) {
				ReportUtils.failTest("Timeout waiting for element to be clickable : " + Element);
				throw new AssertionError("Timeout waiting for element to be clickable : " + Element);
			} catch (Exception e) {
				ReportUtils.failTest("Error clicking element : " + e.getMessage());
				throw new RuntimeException("Error clicking element : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Click_Using_JavaScript_Executor(WebElement Element) {
			try {
				RetryUtils.retryWithTryCatch(() -> {
					if (!Element.isDisplayed()) {
						throw new AssertionError("Element is not visible, JavaScript click might fail : " + Element);
					}

					CommonUtils.highlightElement(Element);

					String elementName = CommonUtils.GetElementName(Element);
					ReportUtils.info("Attempting to click element using JavaScript : " + elementName);

					JavascriptExecutor js = (JavascriptExecutor) getDriver();
					js.executeScript("arguments[0].click();", Element);

					ReportUtils.info("Successfully clicked element using JavaScript : " + elementName);
				}, "Click_Using_JavaScript_Executor");
			} catch (Exception e) {
				ReportUtils.failTest("Error clicking element using JavaScript : " + e.getMessage());
				throw new RuntimeException("Error clicking element using JavaScript : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Click_All_Elements(List<WebElement> elements) {
			int count = 0;

			try {
				if (elements.isEmpty()) {
					throw new AssertionError("Element list is empty, nothing to click.");
				}

				ReportUtils.info("Attempting to click all elements in list");

				for (WebElement element : elements) {
					if (!element.isDisplayed() || !element.isEnabled()) {
						throw new AssertionError("Element " + (count + 1) + " is not visible or enabled.");
					}

					// Highlight element before click
					CommonUtils.highlightElement(element);

					element.click();
					count++;
					ReportUtils.info("Clicked element " + count + " of " + elements.size());
				}

				ReportUtils.info("Successfully clicked all " + elements.size() + " elements.");
			} catch (Exception e) {
				ReportUtils.failTest("Error clicking all elements : " + e.getMessage());
				throw new RuntimeException("Error clicking all elements : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Double_Click(WebElement element) {
			Actions actions = new Actions(getDriver());

			try {
				RetryUtils.retryWithTryCatch(() -> {
					if (!element.isDisplayed() || !element.isEnabled()) {
						throw new AssertionError("Element is not visible or enabled for double click : " + element);
					}

					// Highlight element before double click
					CommonUtils.highlightElement(element);

					String elementName = CommonUtils.GetElementName(element);
					ReportUtils.info("Attempting double click on element : " + elementName);

					actions.doubleClick(element).perform();

					ReportUtils.info("Successfully performed double click on element: " + elementName);
				}, "Double_Click");
			} catch (Exception e) {
				ReportUtils.failTest("Error performing double click : " + e.getMessage());
				throw new RuntimeException("Error performing double click : " + e.getMessage());
			}
		}

	//========================================-- All Click Methods Ends --========================================================================//

	//========================================-- Send Keys Methods Starts --======================================================================//	

		public static void Sendkeys(WebElement Element, Object Keys) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
			try {
				RetryUtils.	retryWithTryCatch(() -> {
					wait.until(ExpectedConditions.visibilityOf(Element)); // Ensure visibility

					if (!Element.isDisplayed() || !Element.isEnabled()) {
						throw new AssertionError("Element is not visible or enabled : " + Element);
					}

					String elementName = CommonUtils.GetElementName(Element);
					CommonUtils.highlightElement(Element);

					ReportUtils.info("Sending keys to element : " + elementName);

					if (Keys instanceof String) {
						Element.sendKeys((String) Keys);
					} else if (Keys instanceof Integer) {
						Element.sendKeys(String.valueOf(Keys));
					} else {
						throw new IllegalArgumentException(
								"Unsupported data type for Sendkeys : " + Keys.getClass().getSimpleName());
					}

					ReportUtils.info("Successfully sent keys: " + Keys + " to element : " + elementName);
				}, "Sendkeys");
			} catch (TimeoutException e) {
				ReportUtils.failTest("Timeout waiting for element : " + Element);
				throw new AssertionError("Timeout waiting for element : " + Element);
			} catch (Exception e) {
				ReportUtils.failTest("Error sending keys to element : " + e.getMessage());
				throw new RuntimeException("Error sending keys to element : " + e.getMessage());
			}
		}

	//-----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Sendkeys_And_Click(WebElement Element, String Keys) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
			try {
				RetryUtils.retryWithTryCatch(() -> {
					wait.until(ExpectedConditions.visibilityOf(Element)); // Wait for visibility

					if (!Element.isDisplayed() || !Element.isEnabled()) {
						throw new AssertionError("Element is not visible or enabled : " + Element);
					}

					ReportUtils.info("Attempting to send keys and click element");

					Element.sendKeys(Keys);
					Element.click();

					ReportUtils.info("Successfully sent keys : " + Keys + " and clicked element");
				}, "Sendkeys_And_Click");
			} catch (TimeoutException e) {
				ReportUtils.failTest("Timeout waiting for element: " + Element);
				throw new AssertionError("Timeout waiting for element : " + Element);
			} catch (Exception e) {
				ReportUtils.failTest("Error in Sendkeys_And_Click: " + e.getMessage());
				throw new RuntimeException("Error in Sendkeys_And_Click : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Clear_And_SendKeys(WebElement Element, Object Keys) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
			try {
				RetryUtils.retryWithTryCatch(() -> {
					wait.until(ExpectedConditions.visibilityOf(Element)); // Wait for visibility

					if (!Element.isDisplayed() || !Element.isEnabled()) {
						throw new AssertionError("Element is not visible or enabled : " + Element);
					}

					// Highlight element before clearing and sending keys
					CommonUtils.highlightElement(Element);

					ReportUtils.info("Attempting to clear and send keys to element");

					Element.clear();
					ReportUtils.info("Cleared element content");

					String elementName = CommonUtils.GetElementName(Element);
					if (elementName == null || elementName.isEmpty()) {
						elementName = Element.toString();
					}

					if (Keys instanceof String) {
						Element.sendKeys((String) Keys);
					} else if (Keys instanceof Integer) {
						Element.sendKeys(String.valueOf(Keys));
					} else {
						throw new IllegalArgumentException(
								"Unsupported data type for SendKeys : " + Keys.getClass().getSimpleName());
					}

					ReportUtils.info("Successfully sent keys : " + Keys + " to element : " + elementName);
				}, "Clear_And_SendKeys");
			} catch (TimeoutException e) {
				ReportUtils.failTest("Timeout waiting for element : " + Element);
				throw new AssertionError("Timeout waiting for element : " + Element);
			} catch (Exception e) {
				ReportUtils.failTest("Error in Clear_And_SendKeys : " + e.getMessage());
				throw new RuntimeException("Error in Clear_And_SendKeys : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Clear_Data(WebElement Element) {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
			try {
				RetryUtils.	retryWithTryCatch(() -> {
					wait.until(ExpectedConditions.visibilityOf(Element)); // Wait for visibility

					if (!Element.isDisplayed() || !Element.isEnabled()) {
						throw new AssertionError("Element is not visible or enabled : " + Element);
					}

					// Highlight element before clearing
					CommonUtils.highlightElement(Element);

					String elementName = CommonUtils.GetElementName(Element);

					ReportUtils.info("Attempting to clear element data : " + elementName);

					Element.clear();

					ReportUtils.info("Successfully cleared element data : " + elementName);
				}, "Clear_Data");
			} catch (TimeoutException e) {
				ReportUtils.failTest("Timeout waiting for element : " + Element);
				throw new AssertionError("Timeout waiting for element : " + Element);
			} catch (Exception e) {
				ReportUtils.failTest("Error in Clear_Data : " + e.getMessage());
				throw new RuntimeException("Error in Clear_Data : " + e.getMessage());
			}
		}

	//========================================-- Send Keys Methods Ends --========================================================================//

		//========================================-- Actions Methods Starts --=======================================================================//	

		public static void Move_To_Element(WebElement Element) {
			ReportUtils.info("Attempting to move to element");

			try {
				RetryUtils.retryWithTryCatch(() -> {
					String elementName = CommonUtils.GetElementName(Element);
					Actions actions = new Actions(getDriver());
					actions.moveToElement(Element).perform();
					ReportUtils.info("Successfully moved to element :" + elementName);
				}, "Move_To_Element");
			} catch (Exception e) {
				ReportUtils.failTest("Failed to move to element : " + e.getMessage());
				throw new RuntimeException("Error moving to element : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Move_To_Element_And_Click(WebElement Element) {
			ReportUtils.info("Attempting to move to element and click");

			try {
				RetryUtils.retryWithTryCatch(() -> {
					String elementName = CommonUtils.GetElementName(Element);
					Actions actions = new Actions(getDriver());
					actions.moveToElement(Element).click().perform();
					ReportUtils.info("Successfully moved to element and clicked :" + elementName);
				}, "Move_To_Element_And_Click");
			} catch (Exception e) {
				ReportUtils.failTest("Failed to move and click element : " + e.getMessage());
				throw new RuntimeException("Error moving and clicking element : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Drag_And_Drop(WebElement Source, WebElement Target) {
			ReportUtils.info("Attempting drag and drop operation");

			try {
				RetryUtils.	retryWithTryCatch(() -> {
					String elementName = CommonUtils.GetElementName(Source);
					String elementName1 = CommonUtils.GetElementName(Target);
					Actions actions = new Actions(getDriver());
					actions.dragAndDrop(Source, Target).perform();
					ReportUtils.info("Successfully completed drag and drop from source " + elementName + "to target "
							+ elementName1);
				}, "Drag_And_Drop");
			} catch (Exception e) {
				ReportUtils.failTest("Drag and drop failed : " + e.getMessage());
				throw new RuntimeException("Error performing drag and drop : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Drag_And_Drop_By_Axis(WebElement Source, int Xaxis, int Yaxis) {
			ReportUtils.info("Attempting drag and drop by axis - X: " + Xaxis + ", Y: " + Yaxis);

			try {
				RetryUtils.retryWithTryCatch(() -> {
					Actions actions = new Actions(getDriver());
					actions.dragAndDropBy(Source, Xaxis, Yaxis).perform();
					ReportUtils.info("Successfully completed drag and drop by axis");
				}, "Drag_And_Drop_By_Axis");
			} catch (Exception e) {
				ReportUtils.failTest("Drag and drop by axis failed: " + e.getMessage());
				throw new RuntimeException("Error performing drag and drop by axis: " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Right_Click(WebElement Element) {
			ReportUtils.info("Attempting right click on element");

			try {
				RetryUtils.retryWithTryCatch(() -> {
					String elementName = CommonUtils.GetElementName(Element);
					Actions actions = new Actions(getDriver());
					actions.contextClick(Element).perform();
					ReportUtils.info("Successfully performed right click :" + elementName);
				}, "Right_Click");
			} catch (Exception e) {
				ReportUtils.failTest("Right-click failed : " + e.getMessage());
				throw new RuntimeException("Error performing right-click : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Click_And_Hold(WebElement Element) {
			ReportUtils.info("Attempting click and hold on element");

			try {
				RetryUtils.retryWithTryCatch(() -> {
					String elementName = CommonUtils.GetElementName(Element);
					Actions actions = new Actions(getDriver());
					actions.clickAndHold(Element).perform();
					ReportUtils.info("Successfully performed click and hold :" + elementName);
				}, "Click_And_Hold");
			} catch (Exception e) {
				ReportUtils.failTest("Click and hold failed : " + e.getMessage());
				throw new RuntimeException("Error performing click and hold : " + e.getMessage());
			}
		}

	//========================================-- Actions Methods Ends --=========================================================================//

}
