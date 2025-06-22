package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ViewImmunohematologyObject extends BasePageObject {
    public ViewImmunohematologyObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/exams/immunohematology/")
                && !currentUrl().contains("/view")
        ) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }

    public String status() {
        WebElement status = driver.findElement(By.id("exam-status"));
        return status.getText();
    }

    public String bloodType() {
        WebElement bloodType = driver.findElement(By.id("blood-type"));
        return bloodType.getText();
    }

    public String irregularAntibodies() {
        WebElement irregularAntibodies = driver.findElement(By.id("irregular-antibodies"));
        return irregularAntibodies.getText();
    }

    public String observations() {
        WebElement observations = driver.findElement(By.id("observations"));
        return observations.getText();
    }
}
