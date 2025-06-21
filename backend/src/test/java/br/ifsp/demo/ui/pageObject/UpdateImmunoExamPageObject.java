package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UpdateImmunoExamPageObject extends BasePageObject{

    private final By bloodTypeSelect = By.id("blood-type-select");
    private final By irregularAntibodiesSelect = By.id("irregular-antibodies-select");
    private final By observationsTextarea = By.id("observations-textarea");
    private final By rejectButton = By.id("reject-button");
    private final By approveButton = By.id("approve-button");

    public UpdateImmunoExamPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/exams")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }


}
