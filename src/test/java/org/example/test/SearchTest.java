package org.example.test;

import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.example.driver.BaseTest;
import org.example.page.HomePage;
import org.example.page.ProductPage;
import org.junit.*;

public class SearchTest extends BaseTest {


    @Story("Urun Arayip Onun Satisina gitmek")
    @Test
    public void deneme(){
        HomePage homePage = new HomePage();
        ProductPage productPage = new ProductPage();
        homePage.searchProductAndGoList();
        productPage.productPageApplications();
    }
}
