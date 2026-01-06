package utils;

public class RetryUtils {

	static void retryWithTryCatch(Runnable action, String actionName) throws InterruptedException {
		int attempt = 0;
		String retryValue = ConfigReader.prop.getProperty("retry.count");
		int maxRetry = Integer.parseInt(retryValue);

		while (attempt < maxRetry) {
			try {
				action.run();
				return;
			} catch (Exception e) {
				attempt++;

				System.out.println("[RETRY] " + actionName + " | Attempt " + attempt + " | Reason: "
						+ e.getClass().getSimpleName() + " - " + e.getMessage());

				if (attempt == maxRetry) {
					throw e; // let outer try–catch handle final failure
				}
				WaitUtils.Hard_Wait(500);
			}
		}
	}

}
