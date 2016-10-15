package selenium;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 15.10.16.
 */
public class Selenium {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public static void before() {
        System.setProperty("webdriver.chrome.driver", "/opt/chromedriver");
    }

    @Before
    public void beforeMethod() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void afterMethod() {
        driver.quit();
    }

    @Test
    public void loadTestThisWebsite() {
        driver.get("http://localhost:8080/JavaSchool/");
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void login() {
        driver.get("http://localhost:8080/JavaSchool/");
        WebElement form = driver.findElement(By.id("loginForm"));
        form.findElement(By.name("username")).sendKeys("admin@ts.ru");
        form.findElement(By.name("password")).sendKeys("adminadmin");
        form.submit();
        waitJquery();
        assertEquals("eCare", driver.getTitle());
    }

    @Test
    public void customerTable() {
        driver.get("http://localhost:8080/JavaSchool/");
        WebElement form = driver.findElement(By.id("loginForm"));
        form.findElement(By.name("username")).sendKeys("admin@ts.ru");
        form.findElement(By.name("password")).sendKeys("adminadmin");
        form.submit();
        waitJquery();

        driver.findElement(By.xpath("//*[@id=\"customers_menu\"]")).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Customers", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());
    }

    @Test
    public void optionsTable() {
        driver.get("http://localhost:8080/JavaSchool/");
        WebElement form = driver.findElement(By.id("loginForm"));
        form.findElement(By.name("username")).sendKeys("admin@ts.ru");
        form.findElement(By.name("password")).sendKeys("adminadmin");
        form.submit();
        waitJquery();

        driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[3]/a")).click();
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"options_menu\"]")));
        menu.click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Options", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());
    }

    @Test
    public void contractsTable() {
        driver.get("http://localhost:8080/JavaSchool/");
        WebElement form = driver.findElement(By.id("loginForm"));
        form.findElement(By.name("username")).sendKeys("admin@ts.ru");
        form.findElement(By.name("password")).sendKeys("adminadmin");
        form.submit();
        waitJquery();

        driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[4]/a")).click();
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"contracts_menu\"]")));
        menu.click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Contracts", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());
    }

    @Test
    public void tariffsTable() {
        driver.get("http://localhost:8080/JavaSchool/");
        WebElement form = driver.findElement(By.id("loginForm"));
        form.findElement(By.name("username")).sendKeys("admin@ts.ru");
        form.findElement(By.name("password")).sendKeys("adminadmin");
        form.submit();
        waitJquery();

        driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")).click();
        WebElement menu = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tariffs_menu\"]")));
        menu.click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Tariffs", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());
    }



    private void waitJquery() {
        Boolean isJqueryUsed = (Boolean)((JavascriptExecutor)driver).executeScript("return (typeof(jQuery) != 'undefined')");
        if(isJqueryUsed){
            while (true){
                // JavaScript test to verify jQuery is active or not
                Boolean ajaxIsComplete = (Boolean)(((JavascriptExecutor)driver).executeScript("return jQuery.active == 0"));
                if (ajaxIsComplete) break;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e) {
                    // nothing
                }
            }
        }
    }
}
