package utils;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class DataUtils {
	
	private static final Faker faker = new Faker(Locale.forLanguageTag("en-IN"));
	private static final Random random = new Random();

	//========================================-- Random Methods Starts --======================================================================//

		public static String Random_String(int length) {
			ReportUtils.info("Generating random string of length : " + length);

			String randomString = RandomStringUtils.randomAlphabetic(length);
			ReportUtils.info("Generated random string : " + randomString);
			return randomString;
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static int Random_Number(int digit) {
			ReportUtils.info("Generating random number with " + digit + " digits");

			String randomNumberString = faker.number().digits(digit);
			int randomNumber = Integer.parseInt(randomNumberString);

			ReportUtils.info("Generated random number : " + randomNumber);
			return randomNumber;
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static Object Random_Number(int digit, boolean asString) {
			ReportUtils.info("Generating random number with " + digit + " digits, return as string : " + asString);

			String randomNumberString = faker.number().digits(digit);

			if (asString) {
				ReportUtils.info("Generated random number string : " + randomNumberString);
				return randomNumberString;
			} else {
				int randomNumber = Integer.parseInt(randomNumberString);
				ReportUtils.info("Generated random number : " + randomNumber);
				return randomNumber;
			}
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static int Random_Number_Between(int start, int end) {
			ReportUtils.info("Generating random number between " + start + " and " + end);

			int randomNumber = faker.number().numberBetween(start, end);
			ReportUtils.info("Generated random number : " + randomNumber);
			return randomNumber;
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static String Random_Email() {
			ReportUtils.info("Generating random email address");

			String randomEmail = faker.internet().emailAddress();
			ReportUtils.info("Generated random email : " + randomEmail);
			return randomEmail;
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static String Random_Name(String nameType) {
			ReportUtils.info("Generating random name of type : " + nameType);

			String generatedName;
			switch (nameType.toLowerCase()) {
			case "firstname":
				generatedName = faker.name().firstName();
				break;
			case "lastname":
				generatedName = faker.name().lastName();
				break;
			case "fullname":
				generatedName = faker.name().fullName();
				break;
			case "username":
				generatedName = faker.name().username();
				break;
			default:
				ReportUtils.info("Invalid name type specified : " + nameType);
				return "";
			}

			ReportUtils.info("Generated random name : " + generatedName);
			return generatedName;
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static String Random_Address() {
			ReportUtils.info("Generating random address");

			String randomAddress = faker.address().streetAddress();
			ReportUtils.info("Generated random address: " + randomAddress);
			return randomAddress;
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static String Random_LoremIpsum(int paragraphs) {
			ReportUtils.info("Generating " + paragraphs + " paragraphs of Lorem Ipsum");

			String randomLoremIpsum = faker.lorem().paragraph(paragraphs);
			ReportUtils.info("Generated Lorem Ipsum text");
			return randomLoremIpsum;
		}

		// -----------------------------------------------------------------------------------------------------------------------------------------//

		public static String Get_Random_Value(List<String> values) {
			if (values == null || values.isEmpty()) {
				ReportUtils.info("Provided list is null or empty.");
				return "";
			}

			ReportUtils.info("Getting random value from list of size: " + values.size());

			int randomIndex = random.nextInt(values.size());
			String randomValue = values.get(randomIndex);
			ReportUtils.info("Selected random value: " + randomValue);
			return randomValue;
		}

	//========================================-- Random Methods Ends --=======================================================================//

	
	
}
