package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.WebDriver;

public class UpdateSeroExamPageObject extends BasePageObject {
    public UpdateSeroExamPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/exams")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }
}
