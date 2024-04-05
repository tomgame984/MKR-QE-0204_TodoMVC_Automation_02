import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.time.Duration;
import java.util.List;


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
        driver.findElement(By.cssSelector(".input-container:nth-child(1) > #todo-input")).sendKeys(" again");
        driver.findElement(By.cssSelector(".input-container:nth-child(1) > #todo-input")).sendKeys(Keys.ENTER);
        WebElement listItemText = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        String modifiedListItem = listItemText.getText();
        assertEquals("Walk the dog again", modifiedListItem);
    }

    @Test
    void deleteAnActiveItem() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("Walk");
        inputField.sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector(".view > label")).click();
        driver.findElement(By.cssSelector(".destroy")).click();
        List<WebElement> todoItemElements = driver.findElements(By.cssSelector("[data-testid='todo-item']"));
        assertEquals(todoItemElements.size(),0);

    }
    @Test
    void changeStatesToCompleted() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("Walk the dog");
        inputField.sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector(".toggle")).click();
        List<WebElement> completedItem = driver.findElements(By.cssSelector(".completed"));
        System.out.println(completedItem.size());
        assertEquals(completedItem.size(),1);
    }

    @Test
    void changeStatesToActive() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("Walk the dog");
        inputField.sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector(".toggle")).click();
        driver.findElement(By.cssSelector(".toggle")).click();
        List<WebElement> activeItem = driver.findElements(By.cssSelector(".view"));
        assertEquals(activeItem.size(),1);
    }

    @Test
    void changeAllTodosToCompleted() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("Walk the dog");
        inputField.sendKeys(Keys.ENTER);
        inputField.sendKeys("Watch the movie");
        inputField.sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector(".toggle-all")).click();
        List<WebElement> completedItem = driver.findElements(By.cssSelector(".completed"));
        System.out.println(completedItem.size());
        assertEquals(completedItem.size(),2);
    }

    @Test
    void changeAllTodosToActive() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("Walk the dog");
        inputField.sendKeys(Keys.ENTER);
        inputField.sendKeys("Watch the movie");
        inputField.sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector(".toggle-all")).click();
        driver.findElement(By.cssSelector(".toggle-all")).click();
        List<WebElement> activeItem = driver.findElements(By.cssSelector(".view"));
        assertEquals(activeItem.size(),2);
    }

    @Test
    void todoItemLessThanTwoCharacter(){
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("A");
        inputField.sendKeys(Keys.ENTER);
        List<WebElement> activeItem = driver.findElements(By.cssSelector(".view"));
        assertEquals(activeItem.size(),0);
    }

    @Test
    void checkMinimumCharacterLimitShouldBeMoreThanTwo(){
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("AB");
        inputField.sendKeys(Keys.ENTER);
        List<WebElement> activeItem = driver.findElements(By.cssSelector(".view"));
        assertEquals(activeItem.size(),1);
    }

    @Test
    void checkFiveHundredCharacterAccepted() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        String input = "1 3 5 7 9 12 15 18 21 24 27 30 33 36 39 42 45 48 51 54 57 60 63 66 69 72 75 78 81 84 87 90 93 96 99 103 107 111 115 119 123 127 131 135 139 143 147 151 155 159 163 167 171 175 179 183 187 191 195 199 203 207 211 215 219 223 227 231 235 239 243 247 251 255 259 263 267 271 275 279 283 287 291 295 299 303 307 311 315 319 323 327 331 335 339 343 347 351 355 359 363 367 371 375 379 383 387 391 395 399 403 407 411 415 419 423 427 431 435 439 443 447 451 455 459 463 467 471 475 479 483 487 491 495 499 501";
        inputField.sendKeys(input);
        inputField.sendKeys(Keys.ENTER);
        List<WebElement> activeItem = driver.findElements(By.cssSelector(".view"));
        assertEquals(activeItem.size(),1);
    }

    @Test
    void checkIntAccepted() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("12");
        inputField.sendKeys(Keys.ENTER);
        List<WebElement> activeItem = driver.findElements(By.cssSelector(".view"));
        assertEquals(activeItem.size(),1);
    }

    @Test
        //This is the test we expect to fail as it is a bug. '&' will change to '1&amp;' due to some issue. The dev team will need to have a look.
    void checkSpecialCharAccepted() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href='examples/react/dist/']")).click();
        WebElement inputField = driver.findElement(By.id("todo-input"));
        inputField.click();
        inputField.sendKeys("1&");
        inputField.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        WebElement listItem = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        String listItemText = listItem.getText();
        assertEquals("1&", listItemText);
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

