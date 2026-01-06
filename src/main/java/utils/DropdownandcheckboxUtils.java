package utils;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BrowserSetup;

public class DropdownandcheckboxUtils {

	private static WebDriver getDriver() {
		return BrowserSetup.getDriver(); // fetch ThreadLocal driver safely
	}
	
	//========================================-- Dropdown Methods Starts --======================================================================//
			public static Object Select_By_Element(WebElement dropdownElement, String selectionType, String selectionValue) throws Exception {

				ReportUtils.info("Starting dropdown selection - Type : " + selectionType + ", Value : " + selectionValue);

				try {

					final Select select = new Select(dropdownElement); 

					RetryUtils.retryWithTryCatch(() -> {

						switch (selectionType.toLowerCase()) {

						case "text":
							select.selectByVisibleText(selectionValue);
							ReportUtils.info("Selected by visible text : " + selectionValue);
							break;

						case "value":
							select.selectByValue(selectionValue);
							ReportUtils.info("Selected by value : " + selectionValue);
							break;

						case "index":
							select.selectByIndex(Integer.parseInt(selectionValue));
							ReportUtils.info("Selected by index : " + selectionValue);
							break;

						default:
							String errorMsg = "Invalid selection type : " + selectionType;
							ReportUtils.info(errorMsg);
							throw new IllegalArgumentException(errorMsg);
						}

					}, "Dropdown Selection");

					return select;

				} catch (Exception e) {
					ReportUtils.failTest("Dropdown selection failed : " + e.getMessage());
					throw e;
				}
			}

			// -----------------------------------------------------------------------------------------------------------------------------------------//

			public static Object Select_Random_Option_From_Dropdown(WebElement dropdownElement) {
				ReportUtils.info("Starting random dropdown selection");

				Select dropdown = new Select(dropdownElement);
				List<WebElement> options = dropdown.getOptions();

				if (!options.isEmpty()) {
					int randomIndex = new Random().nextInt(options.size());
					dropdown.selectByIndex(randomIndex);
					String selectedOptionText = dropdown.getFirstSelectedOption().getText();
					ReportUtils.info("Selected Option ---> " + selectedOptionText);
					return selectedOptionText;
				} else {
					String errorMsg = "Dropdown has no options available";
					ReportUtils.info(errorMsg);
					throw new NoSuchElementException(errorMsg);
				}
			}

			// -----------------------------------------------------------------------------------------------------------------------------------------//

			public static void Select_Random_Option_From_Dropdown_Ignore_Index(WebElement dropdownElement, int ignoredCount) {
				ReportUtils.info("Starting random dropdown selection ignoring first " + ignoredCount + " options");

				Select dropdown = new Select(dropdownElement);
				List<WebElement> options = dropdown.getOptions();

				if (options.size() > ignoredCount) {
					int randomIndex;
					do {
						randomIndex = new Random().nextInt(options.size());
					} while (randomIndex < ignoredCount);

					dropdown.selectByIndex(randomIndex);
					ReportUtils.info("Selected option at index: " + randomIndex);
				} else {
					ReportUtils.info("Not enough options to select from after ignoring first " + ignoredCount + " options");
				}
			}

			// -----------------------------------------------------------------------------------------------------------------------------------------//

			public static void Select_Random_Option_From_Dropdown_Ignore_Indices(WebElement dropdownElement,
					List<Integer> ignoredIndices) {
				ReportUtils.info("Starting random dropdown selection with ignored indices : " + ignoredIndices);

				Select dropdown = new Select(dropdownElement);
				List<WebElement> options = dropdown.getOptions();
				Set<Integer> ignoredIndicesSet = new HashSet<>(ignoredIndices);

				if (!options.isEmpty()) {
					int randomIndex;
					do {
						randomIndex = new Random().nextInt(options.size());
					} while (ignoredIndicesSet.contains(randomIndex));

					dropdown.selectByIndex(randomIndex);
					ReportUtils.info("Selected option at index : " + randomIndex);
				} else {
					ReportUtils.info("No options available in dropdown");
				}
			}

			// -----------------------------------------------------------------------------------------------------------------------------------------//

			public static void Dynamic_Dropdown_Selection(List<WebElement> elements, String attribute, String value) {
				ReportUtils.info("Starting dynamic dropdown selection - Attribute : " + attribute + ", Value : " + value);

				for (WebElement element : elements) {
					if (element.getAttribute(attribute).equalsIgnoreCase(value)) {
						element.click();
						ReportUtils.info("Selected option with " + attribute + " = " + value);
						break;
					}
				}
			}

			// -----------------------------------------------------------------------------------------------------------------------------------------//

			public static Object Dropdown_And_Other_Selection_By_Text(List<WebElement> elements, String text) {
				ReportUtils.info("Starting selection by text : " + text);

				for (WebElement element : elements) {
					if (element.getText().equalsIgnoreCase(text)) {
						element.click();
						ReportUtils.info("Selected element with text : " + text);
						return text;
					}
				}
				return text;
			}

			// -----------------------------------------------------------------------------------------------------------------------------------------//

			public static Object Select_Random_Option_From_Dropdown(List<WebElement> options) {
				ReportUtils.info("Starting random selection from options list");

				String optionText = "";
				if (!options.isEmpty()) {
					int randomIndex = new Random().nextInt(options.size());
					WebElement selectedOption = options.get(randomIndex);
					optionText = selectedOption.getText().trim();
					selectedOption.click();
					ReportUtils.info("Selected option ---> " + optionText);
				} else {
					ReportUtils.info("No options available in the dropdown.");
				}
				return optionText;
			}

		//========================================-- Dropdown Methods Ends --=========================================================================//

	
	//========================================-- Radio Button Methods Starts --===================================================================//

		public static boolean Is_Radio_Button_Selected(WebElement RadioButton) {
			ReportUtils.info("Checking if radio button is selected");

			try {
				boolean isSelected = RadioButton.isSelected();
				ReportUtils.info("Radio button selected status : " + isSelected);
				return isSelected;
			} catch (NoSuchElementException e) {
				ReportUtils.failTest("Radio button not found : " + e.getMessage());
				return false;
			} catch (Exception e) {
				ReportUtils.failTest("Error checking radio button selection : " + e.getMessage());
				throw new RuntimeException("Error checking radio button selection : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Select_Radio_Button(WebElement RadioButton) {
			ReportUtils.info("Attempting to select radio button");
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
			try {
				RetryUtils.retryWithTryCatch(() -> {
					if (!Is_Radio_Button_Selected(RadioButton)) {
						wait.until(ExpectedConditions.elementToBeClickable(RadioButton));

						RadioButton.click();
						ReportUtils.info("Successfully selected the radio button");
					} else {
						ReportUtils.info("Radio button is already selected");
					}
				}, "Select_Radio_Button");
			} catch (TimeoutException e) {
				ReportUtils.failTest("Radio button not clickable within timeout : " + e.getMessage());
				throw new RuntimeException("Radio button not clickable within timeout : " + e.getMessage());
			} catch (NoSuchElementException e) {
				ReportUtils.failTest("Failed to select radio button - element not found : " + e.getMessage());
				throw new RuntimeException("Failed to select radio button - element not found : " + e.getMessage());
			} catch (Exception e) {
				ReportUtils.failTest("Error selecting radio button : " + e.getMessage());
				throw new RuntimeException("Error selecting radio button : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Unselect_Radio_Button(WebElement RadioButton) {
			ReportUtils.info("Attempting to unselect radio button");
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
			try {
				if (Is_Radio_Button_Selected(RadioButton)) {
					wait.until(ExpectedConditions.elementToBeClickable(RadioButton));

					RadioButton.click();
					ReportUtils.info("Successfully unselected the radio button");
				} else {
					ReportUtils.info("Radio button is already unselected");
				}
			} catch (TimeoutException e) {
				ReportUtils.failTest("Radio button not clickable within timeout : " + e.getMessage());
				throw new RuntimeException("Radio button not clickable within timeout : " + e.getMessage());
			} catch (NoSuchElementException e) {
				ReportUtils.failTest("Failed to unselect radio button - element not found : " + e.getMessage());
				throw new RuntimeException("Failed to unselect radio button - element not found : " + e.getMessage());
			} catch (Exception e) {
				ReportUtils.failTest("Error unselecting radio button : " + e.getMessage());
				throw new RuntimeException("Error unselecting radio button : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static String Get_Selected_Radiobtn_Value(List<WebElement> RadioButtons) {
			ReportUtils.info("Getting selected radio button value");

			try {
				for (WebElement radioButton : RadioButtons) {
					if (Is_Radio_Button_Selected(radioButton)) {
						String value = radioButton.getAttribute("value");
						ReportUtils.info("Found selected radio button with value : " + value);
						return value;
					}
				}
				ReportUtils.info("No selected radio button found");
				return null;
			} catch (NoSuchElementException e) {
				ReportUtils.failTest("Error getting selected radio button value - element not found : " + e.getMessage());
				return null;
			} catch (Exception e) {
				ReportUtils.failTest("Error getting selected radio button value : " + e.getMessage());
				throw new RuntimeException("Error getting selected radio button value : " + e.getMessage());
			}
		}

	//========================================-- Radio Button Methods Ends --=====================================================================//

	//========================================-- Check Box Methods Starts --======================================================================//	

		public static void Check_Checkbox(List<WebElement> Checkboxes, String Attribute, String Value) {
			ReportUtils.info("Attempting to check checkbox with " + Attribute + "=" + Value);

			try {
				for (WebElement checkbox : Checkboxes) {
					if (checkbox.getAttribute(Attribute).equalsIgnoreCase(Value) && !checkbox.isSelected()) {
						checkbox.click();
						ReportUtils.info("Checked checkbox with " + Attribute + "=" + Value);
						return;
					}
				}
				ReportUtils.info("No matching checkbox found or already checked.");
			} catch (NoSuchElementException e) {
				ReportUtils.failTest("Checkbox not found: " + e.getMessage());
				throw new RuntimeException("Checkbox not found : " + e.getMessage());
			} catch (Exception e) {
				ReportUtils.failTest("Error checking checkbox : " + e.getMessage());
				throw new RuntimeException("Error checking checkbox : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Uncheck_Checkbox(List<WebElement> Checkboxes, String Attribute, String Value) {
			ReportUtils.info("Attempting to uncheck checkbox with " + Attribute + "=" + Value);

			try {
				for (WebElement checkbox : Checkboxes) {
					if (checkbox.getAttribute(Attribute).equalsIgnoreCase(Value) && checkbox.isSelected()) {
						checkbox.click();
						ReportUtils.info("Unchecked checkbox with " + Attribute + "=" + Value);
						return;
					}
				}
				ReportUtils.info("No matching checkbox found or already unchecked.");
			} catch (NoSuchElementException e) {
				ReportUtils.failTest("Checkbox not found : " + e.getMessage());
				throw new RuntimeException("Checkbox not found : " + e.getMessage());
			} catch (Exception e) {
				ReportUtils.failTest("Error unchecking checkbox: " + e.getMessage());
				throw new RuntimeException("Error unchecking checkbox : " + e.getMessage());
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static void Check_Multiple_Checkboxes(List<WebElement> Checkboxes, String Attribute,
				String[] ValuesToSelect) {
			ReportUtils.info("Attempting to check multiple checkboxes");

			int checkedCount = 0;

			try {
				for (WebElement checkbox : Checkboxes) {
					String checkboxValue = checkbox.getAttribute(Attribute);
					for (String value : ValuesToSelect) {
						if (checkboxValue.equalsIgnoreCase(value) && !checkbox.isSelected()) {
							checkbox.click();
							checkedCount++;
							ReportUtils.info("Checked checkbox with value : " + value);
						}
					}
				}
				ReportUtils.info("Checked " + checkedCount + " checkboxes in total.");
			} catch (NoSuchElementException e) {
				ReportUtils.failTest("Some checkboxes not found: " + e.getMessage());
				throw new RuntimeException("Some checkboxes not found : " + e.getMessage());
			} catch (Exception e) {
				ReportUtils.failTest("Error checking multiple checkboxes : " + e.getMessage());
				throw new RuntimeException("Error checking multiple checkboxes : " + e.getMessage());
			}
		}

	//========================================-- Check Box Methods Ends --=======================================================================//	

}
