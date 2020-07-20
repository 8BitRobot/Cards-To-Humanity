package seleniumtests.environment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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
        System.setProperty("webdriver.gecko.driver", "/opt/geckodriver");
        WebDriver driver = new FirefoxDriver();
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
