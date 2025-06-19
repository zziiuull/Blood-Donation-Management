package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.WebDriver;

public class BasePageObject {
    protected final WebDriver driver;

    public BasePageObject(WebDriver driver) {
        this.driver = driver;
    }

    public String pageTitle() {
        return driver.getTitle();
    }

    public String pageUrl() {
        return driver.getCurrentUrl();
    }
}
