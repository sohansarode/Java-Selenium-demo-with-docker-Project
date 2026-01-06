package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DropdownUtils {

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

	
}
