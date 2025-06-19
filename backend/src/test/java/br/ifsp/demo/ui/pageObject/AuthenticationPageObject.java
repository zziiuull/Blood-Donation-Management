package br.ifsp.demo.ui.pageObject;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AuthenticationPageObject extends BasePageObject {
    private static final Faker faker = new Faker();
    private static final String PAGE_TITLE = "Login";

    public AuthenticationPageObject(WebDriver driver) {
        super(driver);
        if (!PAGE_TITLE.equals(pageTitle())) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }
}
