import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.time.Duration;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TodoMVCTest {
    private static ChromeDriver driver;

    @BeforeAll
    static void launchBrowser() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://todomvc.com/");
        takeScreenshot(driver, "TestScreenshots/ToDoMVCTest/homepage.png");
    }

    @Test
    void testHomepageTitleContainerToDoMVC(){
        String pageTitle = driver.getTitle();
        assertTrue(pageTitle.contains("TodoMVC"));
    }

    @BeforeEach
    void browseToSelectedFramework() {
        driver.get("https://todomvc.com/examples/react/dist/");
    }

    @Test
    void addNewTodoOnSelectedFramework() throws Exception {
        driver.findElement(By.id("todo-input")).click();
        driver.findElement(By.id("todo-input")).sendKeys("Walk the dog.");
        driver.findElement(By.id("todo-input")).sendKeys(Keys.ENTER);
        takeScreenshot(driver, "TestScreenshots/ToDoMVCTest/ItemAdded.png");

        WebElement listItem = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        String listItemText = listItem.getText();
        System.out.println(listItemText);

        assertEquals("Walk the dog.", listItemText);
    }

    @AfterAll
    static void closeBrowser() {
        driver.quit();}



    public static void takeScreenshot(WebDriver webdriver,String desiredPath) throws Exception{
        TakesScreenshot screenshot = ((TakesScreenshot)webdriver);
        File screenshotFile = screenshot.getScreenshotAs(OutputType.FILE);
        File targetFile = new File(desiredPath);
        FileUtils.copyFile(screenshotFile, targetFile);
    }
}

