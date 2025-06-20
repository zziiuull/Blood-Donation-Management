package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.WebDriver;

public class DonationPageObject extends BasePageObject {
    public DonationPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/donation")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }
}
