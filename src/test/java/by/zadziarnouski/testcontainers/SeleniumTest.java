package by.zadziarnouski.testcontainers;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.VncRecordingContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class SeleniumTest {

    @Container
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withCapabilities(new FirefoxOptions())
            .withRecordingMode(RECORD_ALL, new File("/home/taras/"), VncRecordingContainer.VncRecordingFormat.MP4);

    @Test
    void testMe() {
        doSimpleExplore(chrome);
    }

    protected static void doSimpleExplore(BrowserWebDriverContainer<?> rule) {
        RemoteWebDriver driver = setupDriverFromRule(rule);
        System.out.println("Selenium remote URL is: " + rule.getSeleniumAddress());
        System.out.println("VNC URL is: " + rule.getVncAddress());

        driver.get("https://ankiweb.net/decks/");
        System.out.println(driver.getPageSource());
        WebElement title = driver.findElement(By.tagName("h1"));
        System.out.println(title);
    }

    private static RemoteWebDriver setupDriverFromRule(BrowserWebDriverContainer<?> rule) {
        RemoteWebDriver driver = rule.getWebDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return driver;
    }

}

