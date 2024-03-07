package org.example.driver;


import org.openqa.selenium.WebDriver;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {

    public static WebDriver driver;


    @Before
    public void setUp(){

        System.setProperty("webdriver.chrome.driver",
                "src/test/resources/chromedriver.exe");

        driver=new ChromeDriver(getChromeOptions());
        driver.manage().window().maximize();

        driver.get("https://www.cimri.com");
    }

    public ChromeOptions getChromeOptions(){
        ChromeOptions chromeOptions=new ChromeOptions();
        chromeOptions.addArguments("--disable-notification");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-popup-blocking");
        return chromeOptions;
    }
    @After
    public void tearDown(){
        if (driver!=null){
            driver.close();
            driver.quit();
        }
    }

}
