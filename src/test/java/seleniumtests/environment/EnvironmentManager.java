package seleniumtests.environment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.io.IOException;

public class EnvironmentManager {
    public static class JavalinServer implements Runnable {
        public void run() {
            try {
                Process process = Runtime.getRuntime().exec("java -jar build/libs/cards-to-humanity-0.0.1.jar");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initialize() {
        System.setProperty("webdriver.gecko.driver", "./geckodriver");
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        WebDriver driver = new FirefoxDriver(firefoxOptions);
        RunEnvironment.setWebDriver(driver);

        Thread thread = new Thread(new JavalinServer());
        thread.start();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
        }
    }

    public static void stop() {
        RunEnvironment.getWebDriver().quit();
    }
}
