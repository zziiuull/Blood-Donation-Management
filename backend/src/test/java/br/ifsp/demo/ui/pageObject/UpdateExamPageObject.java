package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.WebDriver;

public class UpdateExamPageObject extends BasePageObject{
    public UpdateExamPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/donation")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }
}
