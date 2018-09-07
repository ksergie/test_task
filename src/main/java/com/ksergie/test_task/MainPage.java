/*
Using frameworks:
Junit5
selenium-jupiter (Junit5 extension) for testing the last versions Chrome, Edge and Firefox browsers
log4j2
maven-surefire-plugin

the testSortProduct for Edge does not stable enough

 */

package com.ksergie.test_task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.objects.NativeString.trim;

public class MainPage {
    private static final Logger log = LogManager.getLogger(MainPage.class.getName());
    private WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    private By currency = By.xpath("//div[@id='_desktop_currency_selector']//span[2]");
    private By xpathPrices = By.xpath("//div[@class='product-price-and-shipping']/span[@itemprop='price' and @class='price']");
    private By dropdownCurrency = By.xpath("//div[@id='_desktop_currency_selector']//i");
    private By linkUSDcurr = By.xpath("(//div[@id='_desktop_currency_selector']//a[@class='dropdown-item'])[3]");
    private By fieldSearch = By.name("s");
    private By buttonSearch = By.xpath("//button[@type='submit']");
    private By totalProducts = By.xpath("//div[@id='js-product-list-top']//p");
    private By dropboxSort = By.xpath("//i[@class='material-icons pull-xs-right']");
    private By sortHi2Low = By.xpath("//div[@class='dropdown-menu']/a[@class='select-list js-search-link'][4]");
    private By selectAmount = By.xpath("//div[@class='product-price-and-shipping']/span");


    private static String url = "http://prestashop-automation.qatestlab.com.ua/ru/";
    private String titleMainPage ="prestashop-automation";
    List<String> prices = new ArrayList<>();
    List<Double> priceOfProduct = new ArrayList<>();

    public void openMainPage(){
        openPage(url, titleMainPage);
        log.info("Open the " + url);
    }

    public void checkCurrency(){
        openMainPage();
        setUSDcurrency();
        if(getCurrency().equals("$")){
            System.out.println(getCurrency() + " selected");
            log.info("The currency " + getCurrency() + " is selected");
        } else {
            System.out.println("Error: wrong currency selected");
            log.error("The currency is not selected");

        }
        getProductPrice();
        Iterator iterator = prices.iterator();
        while(iterator.hasNext()){
            String price = (String) iterator.next();
            if(price.contains(getCurrency())){
                System.out.println("The currencies are equal");
                log.info("The currencies are equal");

            }
        }
        prices.clear();
    }

    public void setUSDcurrency(){
        openMainPage();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        selectCurrency(linkUSDcurr);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(linkUSDcurr));
        Assertions.assertEquals("USD $", driver.findElement(currency).getText());
        log.info("Set the currency");
    }

    public void searchDress(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        openMainPage();
        setUSDcurrency();
        searchText("dress");
        getProductPrice();
        System.out.println("Dresses found - " + prices.size());
        String numberProduct = trim(wait.until(ExpectedConditions.visibilityOfElementLocated(totalProducts)).getText());
        if(numberProduct.equals("Товаров: " + prices.size() + ".")){
            System.out.println("The amount is equal");
            log.info("The amount is equal");
        } else {
            System.out.println("The amount is not equal");
            log.error("The amount is equal");


        }
        prices.clear();
    }

    public void sortProductPriceHigh(){
        openMainPage();
        setUSDcurrency();
        searchText("dress");
        sortProductBy(sortHi2Low);
        getProductPrice();
        Iterator iterator = priceOfProduct.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        if(checkSort() < 0){
            System.out.println("Error: The sorting is not correct");
            log.error("Error: The sorting is not correct");
        } else {
            System.out.println("The sorting is correct");
            log.info("The sorting is correct");
        }
        prices.clear();
    }

    public void checkProductDiscount(){
        openMainPage();
        setUSDcurrency();
        searchText("dress");
        sortProductBy(sortHi2Low);
        getProductDiscount();
    }

    private int checkSort(){
        // Previous element of array must be bigger than next one
        for(int i = 0; i < priceOfProduct.size() - 1; i++){
            if (priceOfProduct.get(i) < priceOfProduct.get(i + 1)){
                return -1;
            }
        }
        return 0;
    }

    private void sortProductBy(By xpath){

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(dropboxSort)).click();
        wait.until(ExpectedConditions.elementToBeClickable(xpath)).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void searchText(String str){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(fieldSearch)).sendKeys(str);
        log.info("Search the text: " + str);
        wait.until(ExpectedConditions.elementToBeClickable(buttonSearch)).click();
        log.info("Click the Search button");
    }

    private void selectCurrency(By xpathCurrency){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(dropdownCurrency)).click();
        log.info("Click the Currencies dropdown");
        wait.until(ExpectedConditions.elementToBeClickable(xpathCurrency)).click();
    }

    private void getProductPrice(){
//        WebDriverWait wait = new WebDriverWait(driver, 7);
//        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(xpathPrices));
        List<WebElement>spans = driver.findElements(xpathPrices);
        for (WebElement span: spans) {
            prices.add(span.getText());
        }
        Iterator iterator = prices.iterator();
        while(iterator.hasNext()){
            String str = (String) iterator.next();
            priceOfProduct.add(str2Double(str));
        }
        spans.clear();
    }

    private double str2Double(String str){
        double i = 0;
        if(str.contains("$")){
            str = trim(str.substring(0, str.indexOf('$')));
            str = str.replace(",", ".");
            i = Double.valueOf(str);
        }
        if(str.contains("%")){
            str = trim(str.substring(1, str.indexOf('%')));
            i = Double.valueOf(str);
        }
        return i;
    }

    private void getProductDiscount(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        WebDriverWait wait = new WebDriverWait(driver, 7);
//        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(selectAmount));
        List<WebElement>spans = driver.findElements(selectAmount);
        for(int i = 0; i < spans.size(); i++){
            if(spans.get(i).getAttribute("class").equals("regular-price")){
                double oldPrice = str2Double(spans.get(i).getText());
                double discount = str2Double(spans.get(i + 1).getText());
                double newPrice = str2Double(spans.get(i + 2).getText());
                if(Math.round(100.0 - newPrice / oldPrice * 100) != discount){
                    System.out.println("Discount is wrong");
                    log.error("Discount is wrong");
                    System.out.println(newPrice + " / " + oldPrice + " * 100 != " + discount);
                    log.info(newPrice + " / " + oldPrice + " * 100 != " + discount);
                } else {
                    System.out.println("Discount is right");
                    log.info("Discount is right");
                    System.out.println(newPrice + " / " + oldPrice + " * 100 = " + discount);
                    log.info(newPrice + " / " + oldPrice + " * 100 = " + discount);

                }
            }
        }
        prices.clear();
        spans.clear();
    }

    private String getCurrency(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        String str = wait.until(ExpectedConditions.visibilityOfElementLocated(currency)).getText();
        String[] subStr = str.split(" ");
        return subStr[1];
    }

    private void openPage(String url, String titlePage){
        driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(url);
        Assertions.assertEquals(driver.getTitle(), titlePage);
    }
}
