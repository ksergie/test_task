package com.ksergie.test_task;
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
    private WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    private By currency = By.xpath("//div[@id='_desktop_currency_selector']//span[2]");
    private By xpathPrices = By.xpath("//div[@class='product-price-and-shipping']/span[@itemprop='price' and @class='price']");
    private By xpathDiscount = By.xpath("//div[@class='product-price-and-shipping']//span[@class='discount-percentage']");
    private By dropdownCurrency = By.xpath("//div[@id='_desktop_currency_selector']//i");
    private By linkUSDcurr = By.xpath("(//div[@id='_desktop_currency_selector']//a[@class='dropdown-item'])[3]");
    private By fieldSearch = By.name("s");
    private By buttonSearch = By.xpath("//button[@type='submit']");
    private By totalProducts = By.xpath("//div[@id='js-product-list-top']//p");
    private By dropboxSort = By.xpath("//i[@class='material-icons pull-xs-right']");
    private By sortHi2Low = By.xpath("//div[@class='dropdown-menu']/a[@class='select-list js-search-link'][4]");

    private By test = By.xpath("//div[@class='product-price-and-shipping']//span[@class='price']");
    private By test1 = By.xpath("//div[@class='product-price-and-shipping']//span[@class='regular-price']");


    private static String url = "http://prestashop-automation.qatestlab.com.ua/ru/";
    private String titleMainPage ="prestashop-automation";
    List<String> prices = new ArrayList<>();
    List<Double> priceOfProduct = new ArrayList<>();
    List<Double> discountOfProduct = new ArrayList<>();

    public void openMainPage(){
        openPage(url, titleMainPage);
    }

    public void checkCurrency(){
        openMainPage();
        String siteCurrency = getCurrency();
        getProductPrice();
        Iterator iterator = prices.iterator();
        while(iterator.hasNext()){
            String price = (String) iterator.next();
            if(price.contains(siteCurrency)){
                System.out.println("The currencies are equal");
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

    }

    public void searchDress(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        openMainPage();
        setUSDcurrency();
        searchText("dress");
        getProductPrice();
        System.out.println("Dresses found - " + prices.size());
        Iterator iterator = prices.iterator();
        while(iterator.hasNext()){
            String price = (String) iterator.next();
            if(price.contains("$")){
                System.out.println("The currency is $");
            } else {
                System.out.println("The currency is not $");

            }
        }
        String numberProduct = trim(wait.until(ExpectedConditions.visibilityOfElementLocated(totalProducts)).getText());
        if(numberProduct.equals("Товаров: " + prices.size() + ".")){
            System.out.println("The amount is equal");
        } else {
            System.out.println("The amount is not equal");

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
        } else {
            System.out.println("The sorting is correct");
        }
        prices.clear();
    }

    public void checkProductDiscount(){
        openMainPage();
        setUSDcurrency();
        searchText("dress");
        getProductDiscount();
    }

    private int checkSort(){
        for(int i = 0; i < priceOfProduct.size(); i++){
            if (priceOfProduct.get(i) < priceOfProduct.get(i + 1)){
                return -1;
            }
        }
        return 0;
    }

    private void sortProductBy(By xpath){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(dropboxSort)).click();
//        driver.findElement(dropboxSort).click();
        wait.until(ExpectedConditions.elementToBeClickable(xpath)).click();
//        driver.findElement(xpath).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void searchText(String str){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(fieldSearch)).sendKeys(str);
        wait.until(ExpectedConditions.elementToBeClickable(buttonSearch)).click();
    }

    private void selectCurrency(By xpathCurrency){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(dropdownCurrency)).click();
        wait.until(ExpectedConditions.elementToBeClickable(xpathCurrency)).click();
    }

    private void getProductPrice(){
        WebDriverWait wait = new WebDriverWait(driver, 7);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(xpathPrices));
        List<WebElement>spans = driver.findElements(xpathPrices);
        for (WebElement span: spans) {
            prices.add(span.getText());
        }
        Iterator iterator = prices.iterator();
        while(iterator.hasNext()){
            String str = (String) iterator.next();
            str = trim(str.substring(0, str.indexOf('$')));
            str = str.replace(",", ".");
            priceOfProduct.add(Double.valueOf(str));
        }
        prices.clear();
        spans.clear();
    }

    private void getProductDiscount(){
        WebDriverWait wait = new WebDriverWait(driver, 7);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(xpathDiscount));
        List<WebElement>spans = driver.findElements(test);
        for(int i = 0; i < spans.size(); i++){
            System.out.println("Price 1");
            System.out.print(i + " ");
            System.out.println(spans.get(i).getText());
        }
        prices.clear();
        spans.clear();
        spans = driver.findElements(test1);
        for(int i = 0; i < spans.size(); i++){
            System.out.println("Price 2");
            System.out.print(i + " ");
            System.out.println(spans.get(i).getText());
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
