package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportUtils {

    private static ExtentReports report;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // ✅ ADDED: ThreadLocal test context (BEST PRACTICE)
    private static ThreadLocal<String> testContext = new ThreadLocal<>();

    private static final Logger logger = LogManager.getLogger(ReportUtils.class);

    private static String reportFolderPath;

    //-----------------------------------------------------------------------------------------------------------//

    @BeforeSuite(alwaysRun = true)
    public static void setUpReport() {
        try {
            String projectPath = System.getProperty("user.dir");
            String reportBaseFolder = projectPath + "/Reports";

            new File(reportBaseFolder).mkdirs();

            String timestamp = new SimpleDateFormat("dd-MM-yyyy_hh-mm-a").format(new Date());
            reportFolderPath = reportBaseFolder + "/ExtentReport_" + timestamp;
            new File(reportFolderPath).mkdirs();

            String reportPath = reportFolderPath + "/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("Test Execution Report");
            spark.config().setEncoding("UTF-8");
            spark.config().setTimelineEnabled(true);
            spark.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);
            spark.config().setCss(".test-name { font-weight:bold; }");
            spark.config().setJs("console.log('ExtentReport Loaded');");

            report = new ExtentReports();
            report.attachReporter(spark);

            report.setSystemInfo("OS", System.getProperty("os.name"));
            report.setSystemInfo("Java Version", System.getProperty("java.version"));
            report.setSystemInfo("Selenium Version", "4.12");
            report.setSystemInfo("Framework Version", "1.0.0");
            report.setSystemInfo("Browser", "Chrome 139");
            report.setSystemInfo("User", System.getProperty("user.name"));

            logger.info("Extent Report initialized at: {}", reportPath);

        } catch (Exception e) {
            logger.error("Error initializing report: {}", e.getMessage());
        }
    }

    //-----------------------------------------------------------------------------------------------------------//

    @Step("Start Test: {0}")
    public static void startTest(String testCaseName) {
        ExtentTest extentTest = report.createTest(testCaseName);
        test.set(extentTest);

        // ✅ SET TEST CONTEXT
        testContext.set(testCaseName);

        test.get().info("Test Started");
        Allure.step("Test Started: " + testCaseName);
        logger.info("[{}] Started Test", testCaseName);
    }

    //-----------------------------------------------------------------------------------------------------------//

    @Step("{0}")
    public static void passTest(String message) {
        logger.info("[{}] {}", testContext.get(), message);
        test.get().pass(message);
        Allure.step(message);
    }

    //-----------------------------------------------------------------------------------------------------------//

    @Step("{0}")
    public static void failTest(String message) {
        logger.error("[{}] {}", testContext.get(), message);
        try {
            String screenshotPath = Utilities.Capture_Screenshot();
            File screenshotFile = new File(screenshotPath);

            if (screenshotPath != null && screenshotFile.exists()) {
                test.get().fail(
                        message,
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
                );
                Allure.addAttachment("Screenshot", new FileInputStream(screenshotFile));
            } else {
                test.get().fail(message + " (Screenshot not captured)");
                Allure.step(message + " (Screenshot not captured)");
            }

        } catch (Exception e) {
            test.get().fail(message + " (Screenshot failed: " + e.getMessage() + ")");
            Allure.step(message + " (Screenshot failed: " + e.getMessage() + ")");
        }
    }

    //-----------------------------------------------------------------------------------------------------------//

    @Step("{0}")
    public static void failTestNoScreenshot(String message) {
        logger.error("[{}] {}", testContext.get(), message);
        test.get().fail(message);
        Allure.step(message);
    }

    //-----------------------------------------------------------------------------------------------------------//

    @Step("{0}")
    public static void info(String message) {
        logger.info("[{}] {}", testContext.get(), message);
        test.get().info(message);
        Allure.step(message);
    }

    //-----------------------------------------------------------------------------------------------------------//

    @Step("Log Web Table")
    public static void logWebTableToExtentReport(WebElement table) {
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            StringBuilder rowContent = new StringBuilder();

            for (WebElement cell : cells) {
                rowContent.append(cell.getText()).append("\t");
            }

            String content = rowContent.toString().trim();
            logger.info("[{}] {}", testContext.get(), content);
            test.get().info(content);
            Allure.step(content);
        }
    }

    //-----------------------------------------------------------------------------------------------------------//

    @Step("Log API Response")
    public static void logApiResponse(io.restassured.response.Response response) {
        try {
            if (response != null) {
                String info = "Status Code: " + response.getStatusCode()
                        + "\nResponse Body:\n" + response.getBody().asPrettyString();

                logger.info("[{}] API Response Logged", testContext.get());
                test.get().info(info);
                Allure.step(info);
            } else {
                test.get().warning("Response is null!");
                Allure.step("Response is null!");
            }
        } catch (Exception e) {
            logger.error("[{}] Error logging API response: {}", testContext.get(), e.getMessage());
            test.get().warning("Error logging API response: " + e.getMessage());
            Allure.step("Error logging API response: " + e.getMessage());
        }
    }

    //-----------------------------------------------------------------------------------------------------------//

    public static String getReportFolderPath() {
        return reportFolderPath;
    }

    //-----------------------------------------------------------------------------------------------------------//

    @AfterSuite(alwaysRun = true)
    public static void endReport() {
        if (report != null) {
            report.flush();
            logger.info("Extent Report flushed and saved successfully.");
        }

        // ✅ CLEANUP THREADLOCALS
        test.remove();
        testContext.remove();
    }
}
