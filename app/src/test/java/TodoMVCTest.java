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
import org.openqa.selenium.interactions.Actions;

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

    @BeforeEach
    void browseToHomepage() {
        driver.get("https://todomvc.com/");
    }

    @Test
    void testHomepageTitleContainerToDoMVC(){
        String pageTitle = driver.getTitle();
        assertTrue(pageTitle.contains("TodoMVC"));
    }

    @Test
    void navigateToDesiredFramework(){
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        assertEquals("TodoMVC: React", driver.getTitle());
    }

    @Test
    void addNewTodoOnSelectedFramework() throws Exception {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("Walk the dog.");
        inputField.sendKeys(Keys.ENTER);
        takeScreenshot(driver, "TestScreenshots/ToDoMVCTest/ItemAdded.png");

        WebElement listItem = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        String listItemText = listItem.getText();
        assertEquals("Walk the dog.", listItemText);
    }

    @Test
    void modifyTodoEntry() throws Exception {
        Actions act = new Actions(driver);
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("Walk the dog");
        inputField.sendKeys(Keys.ENTER);
        WebElement listItem = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        act.doubleClick(listItem).perform();
        Thread.sleep(2000);

        driver.findElement(By.cssSelector(".input-container:nth-child(1) > #todo-input")).sendKeys(" again");
        driver.findElement(By.cssSelector(".input-container:nth-child(1) > #todo-input")).sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        WebElement listItemText = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        String modifiedListItem = listItemText.getText();
        assertEquals("Walk the dog again", modifiedListItem);
    }



    @DisplayName("Test Adding Multiple todo Items")
    @ParameterizedTest( name = "Add {0} should return {1}")
    @CsvSource({
            "Walk the dog, Walk the dog",
    })

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

