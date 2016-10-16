package selenium;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by alex on 15.10.16.
 */
public class Selenium {

    private WebDriver driver;
    private WebDriverWait wait;
    private BrowserMobProxy proxy;
    private WebDriver commonDriver;

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
        System.setProperty("webdriver.chrome.driver", "/opt/chromedriver");
        // start the proxy
        proxy = new BrowserMobProxyServer();
        proxy.start(0);

        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();
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

        proxy.enableHarCaptureTypes(CaptureType.RESPONSE_HEADERS, CaptureType.RESPONSE_CONTENT);
    }

    @AfterGroups(groups = "admin")
    public void adminAfter() {
        driver.quit();
    }

    @AfterGroups(groups = "common")
    public void commonAfter() {
        commonDriver.quit();
    }
    @BeforeGroups(groups = "common")
    public void beforeCommon() {
        System.setProperty("webdriver.chrome.driver", "/opt/chromedriver");

        commonDriver = new ChromeDriver();
        commonDriver.manage().window().maximize();
    }

    @Test(groups = {"common"})
    public void loadTestThisWebsite() {
        commonDriver.get("http://localhost:8080/JavaSchool/");
        assertEquals("Login", commonDriver.getTitle());
    }

    @Test(groups = {"common"})
    public void login() {
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
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[1]/a")), By.xpath("//*[@id=\"customers_menu\"]"), true).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Customers", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[1]/a")), By.xpath("//*[@id=\"customers_menu\"]"), false);
    }

    @Test(groups = {"admin"})
    public void optionsTable() {
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[3]/a")), By.xpath("//*[@id=\"options_menu\"]"), true).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Options", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[3]/a")), By.xpath("//*[@id=\"options_menu\"]"), false);
    }

    @Test(groups = {"admin"})
    public void contractsTable() {
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[4]/a")), By.xpath("//*[@id=\"contracts_menu\"]"), true).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Contracts", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[4]/a")), By.xpath("//*[@id=\"contracts_menu\"]"), false);
    }

    @Test(groups = {"admin"})
    public void tariffsTable() {
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")), By.xpath("//*[@id=\"tariffs_menu\"]"), true).click();
        waitJquery();
        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() > 0);
        assertEquals("Tariffs", driver.findElement(By.xpath("//*[@id=\"piece_table\"]/div[1]/div/h1")).getText());

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")), By.xpath("//*[@id=\"tariffs_menu\"]"), false);
    }


    @Test(groups = {"admin"})
    public void addTariff() {
        proxy.newHar();

        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")), By.id("new_tariff_menu"), true).click();
        waitJquery();

        assertEquals("Add new tariff", driver.findElement(By.xpath("//*[@id=\"piece_new_tariff\"]/div/div/h1")).getText());

        int harsBeforeSend = proxy.getHar().getLog().getEntries().size();

        WebElement form = driver.findElement(By.id("add_tariff_form"));
        form.findElement(By.id("name")).sendKeys("Selenium");
        WebElement costInput = form.findElement(By.id("cost"));
        costInput.sendKeys("abc");
        form.submit();

        // Check no request
        assertEquals(harsBeforeSend, proxy.getHar().getLog().getEntries().size());

        costInput.clear();
        costInput.sendKeys("100");
        WebElement simpleOption = form.findElement(By.id("possibleOptions[][id]0"));
        assertFalse(simpleOption.isSelected());
        WebElement free10 = form.findElement(By.id("possibleOptions[][id]1"));

        Actions actions = new Actions(driver);
        actions.moveToElement(free10).click().perform();
        waitJquery();
        assertTrue(free10.isSelected());
        assertTrue(simpleOption.isSelected());
        actions.moveToElement(free10).click().perform();
        waitJquery();
        assertFalse(free10.isSelected());
        assertFalse(simpleOption.isSelected());

        form.submit();

        assertNotEquals(harsBeforeSend, proxy.getHar().getLog().getEntries().size());
        HarEntry har = catchHar();
        assertEquals(har.getResponse().getStatusText(), "Created");
        assertEquals(har.getResponse().getStatus(), 201);

        form.findElement(By.id("name")).sendKeys("Selenium");
        form.findElement(By.id("cost")).sendKeys("100");

        form.submit();

        HarEntry har2 = catchHar();
        assertEquals("Conflict", har2.getResponse().getStatusText());
        assertEquals(409, har2.getResponse().getStatus());

        deleteEntity(har);
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")), By.id("new_tariff_menu"), false);
    }

    @Test(groups = {"admin"})
    public void addOption() {
        proxy.newHar();
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[3]/a")), By.id("new_option_menu"), true).click();
        waitJquery();
        assertEquals("Add new option", driver.findElement(By.xpath("//*[@id=\"piece_new_option\"]/div/div/h1")).getText());

        int harsBeforeSend = proxy.getHar().getLog().getEntries().size();
        WebElement form = driver.findElement(By.id("add_option_form"));
        form.submit();

        // Check no request
        assertEquals(harsBeforeSend, proxy.getHar().getLog().getEntries().size());

        form.findElement(By.id("name")).sendKeys("Selenium");
        WebElement costInput = form.findElement(By.id("cost"));
        costInput.sendKeys("abc");
        form.submit();

        // Check no request
        assertEquals(harsBeforeSend, proxy.getHar().getLog().getEntries().size());

        costInput.clear();
        costInput.sendKeys("100");
        form.findElement(By.id("connect_cost")).sendKeys("100");

        WebElement forTariffs = driver.findElement(By.id("forTariffs"));
        assertTrue(forTariffs.findElements(By.xpath(".//*")).size() > 0);
        WebElement require = driver.findElement(By.id("requiredFrom"));
        assertTrue(require.findElements(By.xpath(".//*")).size() == 0);
        WebElement forbidden = driver.findElement(By.id("forbiddenWith"));
        assertTrue(forbidden.findElements(By.xpath(".//*")).size() == 0);

        Actions actions = new Actions(driver);

        WebElement smsUser = form.findElement(By.id("possibleTariffsOfOption[][id]1"));
        actions.moveToElement(smsUser).click().perform();
        waitJquery();
        assertTrue(require.findElements(By.xpath(".//*")).size() > 0);
        assertTrue(forbidden.findElements(By.xpath(".//*")).size() > 0);

        WebElement sms20 = form.findElement(By.id("requiredFrom[][id]2"));
        actions.moveToElement(sms20).click().perform();
        waitJquery();

        assertTrue(require.findElement(By.id("requiredFrom[][id]0")).isSelected());
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"requiredFrom\"]/div[2]")), "no", "checkbox-danger"));
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"requiredFrom\"]/div[4]")), "no", "checkbox-danger"));
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"forbiddenWith\"]/div[1]")), "no", "checkbox-danger"));
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"forbiddenWith\"]/div[3]")), "no", "checkbox-danger"));

        actions.moveToElement(sms20).click().perform();
        waitJquery();

        assertFalse(require.findElement(By.id("requiredFrom[][id]0")).isSelected());
        assert(hasAllClass(driver.findElement(By.xpath("//*[@id=\"requiredFrom\"]/div[2]")), "checkbox-primary"));
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"requiredFrom\"]/div[4]")), "checkbox-primary"));
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"forbiddenWith\"]/div[1]")), "checkbox-primary"));
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"forbiddenWith\"]/div[3]")), "checkbox-primary"));

        actions.moveToElement(form.findElement(By.id("possibleTariffsOfOption[][id]2"))).click().perform();
        waitJquery();

        assertTrue(require.findElements(By.xpath(".//*")).size() < forbidden.findElements(By.xpath(".//*")).size());

        form.submit();

        WebElement form2 = driver.findElement(By.id("add_option_form"));
        assertNotEquals(harsBeforeSend, proxy.getHar().getLog().getEntries().size());
        HarEntry har = catchHar();
        assertEquals(har.getResponse().getStatusText(), "Created");
        assertEquals(har.getResponse().getStatus(), 201);
        waitJquery();

        deleteEntity(har);
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[3]/a")), By.id("new_option_menu"), false);
    }


    @Test(groups = {"admin"})
    public void addCustomer() {
        proxy.newHar();
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[1]/a")), By.id("new_customer_menu"), true).click();
        waitJquery();
        assertEquals("Add new customer", driver.findElement(By.xpath("//*[@id=\"piece_new_customer\"]/div/div/h1")).getText());

        int harsBeforeSend = proxy.getHar().getLog().getEntries().size();

        WebElement form = driver.findElement(By.id("add_customer_form"));
        form.submit();

        // Check no request
        assertEquals(harsBeforeSend, proxy.getHar().getLog().getEntries().size());

        form.findElement(By.id("name")).sendKeys("Selenium");
        form.findElement(By.id("surname")).sendKeys("Selenium");
        form.findElement(By.id("passport_number")).sendKeys("ZZZZZZZZZ");
        form.findElement(By.id("birthday")).sendKeys("19191991");
        form.findElement(By.id("email")).sendKeys("selenium@selenium.ru");
        form.findElement(By.id("number")).sendKeys("5555555555");
        form.submit();
        HarEntry har = catchHar();
        assertEquals(409, har.getResponse().getStatus());
        assertTrue(har.getResponse().getContent().getText().contains("Resource with 'PassportNumber' == 'ZZZZZZZZZ' already exists."));

        form.findElement(By.id("passport_number")).sendKeys(RandomStringUtils.random(11, true, true));
        form.submit();

        HarEntry har2 = catchHar();
        assertEquals(har2.getResponse().getStatusText(), "Created");
        assertEquals(har2.getResponse().getStatus(), 201);
        deleteEntity(har2);
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[1]/a")), By.id("new_customer_menu"), false);
    }

    @Test(groups = {"admin"})
    public void testCustomer() {
        proxy.newHar();
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[1]/a")), By.xpath("//*[@id=\"customers_menu\"]"), true).click();
        waitJquery();
        driver.findElement(By.xpath("//*[@id=\"content_table_filter\"]/label/input")).sendKeys("Testname Testsurname");

        int harsBeforeSend = proxy.getHar().getLog().getEntries().size();

        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() == 1);

        WebElement row = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody/tr/td[contains(text(), 'Testsurname Testname')]"));
        row.findElement(By.xpath("//button[contains(.,'Show')]")).click();
        assertEquals("Testsurname Testname", driver.findElement(By.id("customerName")).getText());
        assertEquals("ZZZZZZZZZ", driver.findElement(By.id("customerPassportNumber")).getText());

        wv(By.linkText("+7 (000) 000-0000")).click();
        WebElement name = wv(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//h3"));
        assertEquals("Simple", name.getText());
        WebElement menu1 = wv(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//button"));
        menu1.click();
        wv(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//a/p[contains(text(),'Edit')]/..")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//a/p[contains(text(),'Unblock')]/.."))).click();
        waitJquery();
        assertEquals(catchHar().getResponse().getStatus(), 200);
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..")), "panel-default"));
        menu1.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//a/p[contains(text(),'Block')]/.."))).click();
        waitJquery();
        assertEquals(catchHar().getResponse().getStatus(), 200);
        assertTrue(hasAllClass(driver.findElement(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..")), "panel-red"));
    }

    @Test(groups = {"admin"})
    public void testCustomerContracts() {
        proxy.newHar();
        clickMenu(driver.findElement(By.xpath("//*[@id=\"side-menu\"]/li[1]/a")), By.xpath("//*[@id=\"customers_menu\"]"), true).click();
        waitJquery();
        driver.findElement(By.xpath("//*[@id=\"content_table_filter\"]/label/input")).sendKeys("Testname Testsurname");

        WebElement tableBody = driver.findElement(By.xpath("//*[@id=\"content_table\"]/tbody"));
        assertTrue(tableBody.findElements(By.tagName("tr")).size() == 1);

        WebElement row = wv(By.xpath("//*[@id=\"content_table\"]/tbody/tr/td[contains(text(), 'Testsurname Testname')]"));
        row.findElement(By.xpath("//button[contains(.,'Show')]")).click();
        waitJquery();
        WebElement name = wv(By.id("customerName"));
        assertEquals("Testsurname Testname", name.getText());
        assertEquals("ZZZZZZZZZ", driver.findElement(By.id("customerPassportNumber")).getText());

        WebElement accordion = wv(By.id("accordion"));
        Integer accordionSize = accordion.findElements(By.cssSelector("#accordion > div")).size();

        wv(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//button")).click();
        wv(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//a/p[contains(text(),'Delete')]")).click();
        waitJquery();

        assertTrue(accordion.findElements(By.cssSelector("#accordion > div")).size() == accordionSize-1);

        WebElement newContract = driver.findElement(By.className("panel-info"));
        newContract.findElement(By.linkText("Add new contract")).click();
        WebElement form = wv(By.id("add_contract_form"));
        WebElement number = wv(By.id("number"));
        number.sendKeys("1111111111");

        form.submit();
        HarEntry har = catchHar();
        assertEquals(har.getResponse().getStatus(), 409);
        assertTrue(har.getResponse().getContent().getText().contains("Resource with 'Number' == '+7 (111) 111-1111' already exists."));
        number.clear();
        number.sendKeys("0000000000");
        number.submit();
        waitJquery();
        WebElement newContractElement = wv(By.linkText("+7 (000) 000-0000"));
        newContractElement.click();
        wv(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"accordion\"]//a[contains(text(),\"00\")]/../../..//a/p[contains(text(),'Block')]/.."))).click();
        waitJquery();
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
                Boolean ajaxIsComplete = (Boolean)(((JavascriptExecutor)driver).executeScript("return jQuery.active == 0")) &&
                        (Boolean)(((JavascriptExecutor)driver).executeScript("return document.readyState").toString().equals("complete"));
                if (ajaxIsComplete) break;
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e) {
                    // nothing
                }
            }
        }
    }

    private HarEntry catchHar() {
        List<HarEntry> har = proxy.getHar().getLog().getEntries();
        while (true) {
            if (har.get(har.size()-1).getResponse().getBodySize() != -1){
                return har.get(har.size()-1);
            }
        }
    }

    private void deleteEntity(HarEntry har) {
        proxy.newHar("Delete");
        List<HarNameValuePair> headers = har.getResponse().getHeaders();
        String baseUrl = har.getRequest().getUrl();
        baseUrl = baseUrl.substring(0, baseUrl.indexOf("/rest"));
        for (HarNameValuePair header : headers) {
            if (header.getName().equals("Location")) {
                ((JavascriptExecutor)driver).executeScript("jQuery.ajax({type:'DELETE',url:'"+baseUrl+header.getValue()+"'})");
                assertEquals(200, catchHar().getResponse().getStatus());
            }
        }
    }

    private boolean hasAllClass(WebElement element, String... active) {
        for (String classActive : active){
            if (!element.getAttribute("class").contains(classActive)){
                return false;
            }
        }
        return true;
    }

    private WebElement wv(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private void wi(By by) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }
}
