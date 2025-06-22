package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ViewSerologicalObject extends BasePageObject {
    public ViewSerologicalObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/exams/serological-screening/")
                && !currentUrl().contains("/view")
        ) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }

    public String status() {
        WebElement status = driver.findElement(By.id("exam-status"));
        return status.getText();
    }

    public String hepatitisB() {
        WebElement hepatitisB = driver.findElement(By.id("hepatitis-b"));
        return hepatitisB.getText();
    }

    public String hepatitisC() {
        WebElement hepatitisC = driver.findElement(By.id("hepatitis-c"));
        return hepatitisC.getText();
    }

    public String chagasDisease() {
        WebElement chagasDisease = driver.findElement(By.id("chagas-disease"));
        return chagasDisease.getText();
    }

    public String syphilis() {
        WebElement syphilis = driver.findElement(By.id("syphilis"));
        return syphilis.getText();
    }

    public String aids() {
        WebElement aids = driver.findElement(By.id("aids"));
        return aids.getText();
    }

    public String htlv() {
        WebElement htlv = driver.findElement(By.id("htlv1-2"));
        return htlv.getText();
    }

    public String observations() {
        WebElement observations = driver.findElement(By.id("observations"));
        return observations.getText();
    }
}
