package selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * Created by alex on 15.10.16.
 */
public class Selenium {

    private WebDriver driver;
    private WebDriver commonDriver;
    private WebDriverWait wait;

    @BeforeClass
    public static void before() {
    }

    @BeforeMethod
    public void beforeMethod() {
    }

    @AfterMethod
    public void afterMethod() {
    }

    @BeforeSuite
    public void beforeSuite() {
    }

    @BeforeGroups(groups = "admin")
    public void adminTests() {
        System.out.println("beforeAdmin");
        System.setProperty("webdriver.chrome.driver", "/opt/chromedriver");

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);

        driver.get("http://localhost:8080/JavaSchool/");
        WebElement form = driver.findElement(By.id("loginForm"));
        form.findElement(By.name("username")).sendKeys("admin@ts.ru");
        form.findElement(By.name("password")).sendKeys("adminadmin");
        form.submit();
        waitJquery();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customers_menu")));
        WebElement menu = driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a"));
        clickMenu(menu, By.id("tariffs_menu"), true);
        clickMenu(menu, By.id("tariffs_menu"), false);
    }

    @AfterGroups(groups = "admin")
    public void adminAfter() {
        System.out.println("AfterAll");
        driver.quit();
    }

    @AfterGroups(groups = "common")
    public void commonAfter() {
        System.out.println("AfterCommon");
        commonDriver.quit();
    }
    @BeforeGroups(groups = "common")
    public void beforeCommon() {
        System.out.println("BeforeCommon");
        System.setProperty("webdriver.chrome.driver", "/opt/chromedriver");

        commonDriver = new ChromeDriver();
    }

    @Test(groups = {"common"})
    public void loadTestThisWebsite() {
        System.out.println("CommonLoad");
        commonDriver.get("http://localhost:8080/JavaSchool/");
        assertEquals("Login", commonDriver.getTitle());
    }

    @Test(groups = {"common"})
    public void login() {
        System.out.println("CommonLogin");
        commonDriver.get("http://localhost:8080/JavaSchool/");
        WebElement form = commonDriver.findElement(By.id("loginForm"));
        form.findElement(By.name("username")).sendKeys("admin@ts.ru");
        form.findElement(By.name("password")).sendKeys("adminadmin");
        form.submit();
        waitJquery();
        assertEquals("eCare", commonDriver.getTitle());
    }

    @Test(groups = {"admin"})
    public void customerTable() {
        System.out.println("AdmonCustomerTable");
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[1]/a")), By.xpath("//*[@id=\"customers_menu\"]"), true).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Customers", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[1]/a")), By.xpath("//*[@id=\"customers_menu\"]"), false);
    }

    @Test(groups = {"admin"})
    public void optionsTable() {
        System.out.println("AdmonOptionTable");
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[3]/a")), By.xpath("//*[@id=\"options_menu\"]"), true).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Options", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[3]/a")), By.xpath("//*[@id=\"options_menu\"]"), false);
    }

    @Test(groups = {"admin"})
    public void contractsTable() {
        System.out.println("AdmonContractTable");
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[4]/a")), By.xpath("//*[@id=\"contracts_menu\"]"), true).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Contracts", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[4]/a")), By.xpath("//*[@id=\"contracts_menu\"]"), false);
    }

    @Test(groups = {"admin"})
    public void tariffsTable() {
        System.out.println("AdmonTariffTable");
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")), By.xpath("//*[@id=\"tariffs_menu\"]"), true).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Tariffs", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")), By.xpath("//*[@id=\"tariffs_menu\"]"), false);
    }


    private WebElement clickMenu(WebElement menuGroup, By selector, Boolean visibility) {
        WebElement webElement = null;
        while(true) {
            menuGroup.click();
            if (visibility) {
                try {
                    webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
                } catch (TimeoutException e) {
                    continue;
                }
            } else {
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(selector));
                } catch (TimeoutException e) {
                    continue;
                }
            }
            break;
        }
        return webElement;
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
