package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UpdateSeroExamPageObject extends BasePageObject {

    private final By hepatitisBSelect = By.id("hepatitisb-select");
    private final By hepatitisCSelect = By.id("hepatitisc-select");
    private final By chagasDiseaseSelect = By.id("chagasDisease-select");
    private final By syphilisSelect = By.id("syphilis-select");
    private final By aidsSelect = By.id("aids-select");
    private final By htlvSelect = By.id("htlv1_2-select");
    private final By observationsTextarea = By.id("observations-textarea");
    private final By rejectButton = By.id("reject-button");
    private final By approveButton = By.id("approve-button");

    public UpdateSeroExamPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/exams")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }


}
