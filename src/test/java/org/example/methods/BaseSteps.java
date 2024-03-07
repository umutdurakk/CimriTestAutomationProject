package org.example.methods;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.example.driver.BaseTest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class BaseSteps {

    WebDriver driver;
    FluentWait<WebDriver> wait;
    JavascriptExecutor jsdriver;

    private static final Logger logger = Logger.getLogger(BaseSteps.class.getName());


    public BaseSteps(){
        driver= BaseTest.driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20), Duration.ofMillis(100)).ignoring(NoSuchElementException.class);
        jsdriver=(JavascriptExecutor) driver;
    }

    public WebElement findElement(String locatorKey){
        WebElement element = null;

        try {
            File folder =new File("src/test/java/org/example/locators");
            File[] listOfFiles= folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".json")) {
                        String jsonContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
                        JSONArray jsonArray = new JSONArray(jsonContent);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject locatorInfo = jsonArray.getJSONObject(i);

                            String key = locatorInfo.getString("key");
                            String type = locatorInfo.getString("type");
                            String value = locatorInfo.getString("value");

                            if (key.equals(locatorKey)) {
                                switch (type) {
                                    case "id":
                                        element = driver.findElement(By.id(value));
                                        break;
                                    case "css":
                                        element = driver.findElement(By.cssSelector(value));
                                        break;
                                    case "xpath":
                                        element = driver.findElement(By.xpath(value));
                                        break;
                                    case "class":
                                        element = driver.findElement(By.className(value));
                                        break;
                                    default:
                                        Assert.fail("Locator tipi Bulunamadı");
                                }
                            }
                            if (element != null) {
                                break;
                            }
                        }
                    }
                    if (element != null) {
                        break;
                    }
                }
            }

        } catch (JSONException | NoSuchElementException | IOException e) {
            Assert.fail("Bir Hata Bulundu");
        }
        if (element == null) {
            Assert.fail(locatorKey + " elementi bulunamadi!");
        }

        return element;

    }

    public By findElementInfoBy(String locatorKey) {
        By by = null;
        boolean elementFound = false;

        try {
            File folder = new File("src/test/java/org/example/locators");
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".json")) {
                        String jsonContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
                        JSONArray jsonArray = new JSONArray(jsonContent);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject locatorInfo = jsonArray.getJSONObject(i);

                            String key = locatorInfo.getString("key");
                            String type = locatorInfo.getString("type");
                            String value = locatorInfo.getString("value");

                            if (key.equals(locatorKey)) {
                                switch (type) {
                                    case "id":
                                        by = By.id(value);
                                        break;
                                    case "css":
                                        by = By.cssSelector(value);
                                        break;
                                    case "xpath":
                                        by = By.xpath(value);
                                        break;
                                    case "class":
                                        by = By.className(value);
                                        break;
                                    default:
                                        Assert.fail("Locator tipi Bulunamadı");
                                }
                                elementFound = true;
                                break;
                            }
                        }
                    }
                    if (elementFound) {
                        break;
                    }
                }
            }
            if (!elementFound) {
                Assert.fail("Element bulunamadi!");
            }
        } catch (JSONException | NoSuchElementException | IOException e) {
            Assert.fail("Bir hata olustu: " + e.getMessage());
        }

        return by;
    }

    public void waitBySecond(int seconds){
        try {
            Thread.sleep(seconds * 1000L);
            logger.info(seconds + " saniye beklendi");
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }


    }
    public List<WebElement> findsElements(String locatorKey){
        return driver.findElements(findElementInfoBy(locatorKey));
    }

    public void clickElement(String locatorKey){
        findElement(locatorKey).click();
        logger.info(locatorKey + " elementine tıklandı");

    }

    public void sendKeys(String locatorKey,String text){

        findElement(locatorKey).sendKeys(text);
        logger.info(locatorKey +" elementine " + text + " texti yazıldı");
    }
    public void sendKeyToElementENTER(String locatorKey) {
        findElement(locatorKey).sendKeys(Keys.ENTER);
        logger.info(locatorKey + " elementine Enter yollandı");
    }

    public void checkElementText(String locatorKey,String expectedText){
        WebElement element = findElement(locatorKey);
        Assert.assertEquals("Elementin Text içeriği farklı",expectedText,element.getText());
        logger.info(locatorKey + " elementinin text icerigi beklenen degere esit\n" +
                "elementText:" + element.getText() + "\t" +
                "expectedText:" + expectedText);
    }
    public void javascriptclicker(String locatorKey) {
        WebElement element= findElement(locatorKey);
        JavascriptExecutor executor = (JavascriptExecutor) jsdriver;
        executor.executeScript("arguments[0].click();", element);
    }
    public Boolean isElementVisible(String key){

        try {
            wait.until(ExpectedConditions.visibilityOf(findElement(key)));

        }catch (Exception e){
            logger.info("yanlış - " + key);
            return false;
        }
        logger.info("Doğru - " + key + ": görünür");
        return true;

    }

    public void scrollUntilElementIsVisible(String locatorKey) {
        Duration duration = Duration.ofSeconds(5);

        WebDriverWait wait = new WebDriverWait(driver, duration);
        while (true) {
            if (findElement(locatorKey).isDisplayed()) {
                break;
            }
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500)");
            wait.until(ExpectedConditions.visibilityOf(findElement(locatorKey)));
        }
    }

    public void clickWantedElement(int number,String locatorKey){
        List<WebElement> element = findsElements(locatorKey);
        element.get(number).click();
    }
    public void swipePage(){
            jsdriver.executeScript(String.format("window.scrollTo(0, %d)", 500));

    }

    public void printProductMoney(String locatorKey){
        WebElement element = findElement(locatorKey);
        logger.info("Ürünün fiyatı : " + element.getText());
    }

    public void sendKeysWithCsv(String locatorKey){
        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/csvFile.csv"))) {
            String product = br.readLine();
            findElement(locatorKey).sendKeys(product);
            logger.info("csv dosyasından çekilen "+product + " yazıldı");
        }catch (IOException e) {
            logger.info("Bir Hata oldu");

        }
    }




}
