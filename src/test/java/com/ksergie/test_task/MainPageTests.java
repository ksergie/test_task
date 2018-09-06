package com.ksergie.test_task;

import io.github.bonigarcia.SeleniumExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(SeleniumExtension.class)
@DisplayName("Main page tests")
public class MainPageTests {
    private MainPage mainPage;

    @Disabled
    @TestTemplate
    @DisplayName("Main page. Open the Main Page")
    void testOpenMainPage(WebDriver driver){
        mainPage = new MainPage(driver);
        mainPage.openMainPage();
    }

    @Disabled
    @TestTemplate
    @DisplayName("Main page. Check currency")
    void testCheckCurrency(WebDriver driver){
        mainPage = new MainPage(driver);
        mainPage.checkCurrency();
    }

    @Disabled
    @TestTemplate
    @DisplayName("Main page. Set USD currency")
    void testSetUSDCurrency(WebDriver driver){
        mainPage = new MainPage(driver);
        mainPage.setUSDcurrency();
    }

    @Disabled
    @TestTemplate
    @DisplayName("Main page. Search Dress")
    void testSearchDress(WebDriver driver){
        mainPage = new MainPage(driver);
        mainPage.searchDress();
    }

    @Disabled
    @TestTemplate
    @DisplayName("Main page. Test sorting of Products")
    void testSortProduct(WebDriver driver){
        mainPage = new MainPage(driver);
        mainPage.sortProductPriceHigh();
    }

    @TestTemplate
    @DisplayName("Main page. Check Product's discount")
    void testProductDiscount(WebDriver driver){
        mainPage = new MainPage(driver);
        mainPage.checkProductDiscount();
    }
}
