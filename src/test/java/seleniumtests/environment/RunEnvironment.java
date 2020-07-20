package seleniumtests.environment;

import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class RunEnvironment {
    private static WebDriver webDriver;

    public static WebDriver getWebDriver() {
        return webDriver;
    }

    static void setWebDriver(WebDriver webDriver) {
        RunEnvironment.webDriver = webDriver;
    }
}
