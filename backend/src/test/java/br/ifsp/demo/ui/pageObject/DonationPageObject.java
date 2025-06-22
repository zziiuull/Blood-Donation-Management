package br.ifsp.demo.ui.pageObject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DonationPageObject extends BasePageObject {

    private final By donationTitle = By.id("donation-title");
    private final By registerTabButton = By.id("register-tab");
    private final By registerDonationStepOneText = By.xpath("//h3[contains(text(), 'Select a donor')]");
    private final By updateTabButton = By.id("update-tab");
    private final By donationAutocompleteSelectButton = By.id("donation-autocomplete");
    private final By viewTabButton = By.id("view-tab");
    private final By activeUpdateTabLocator = By.xpath("//button[@id='update-tab' and @aria-selected='true']");
    private final By activeViewTabLocator = By.xpath("//button[@id='view-tab' and @aria-selected='true']");
    private final By donorSelect = By.id("donor-autocomplete");
    private final By appointmentSelect = By.id("appointment-autocomplete");
    private final By immunohematologyCheckbox = By.xpath("//*[@id=\"immunohematologyexam-checkbox-\"]");
    private final By serologicalCheckbox = By.xpath("//*[@id=\"serologicalscreeningexam-checkbox-\"]");
    private final By updateImmunoExamLink = By.xpath("//h3[text()='Immunohematology exam']/following-sibling::div//a[text()='Update']");
    private final By updateSeroExamLink = By.xpath("//h3[text()='Serological screening exam']/following-sibling::div//a[text()='Update']");
    private final By bloodTypeInTableLocator = By.xpath("//h3[text()='Immunohematology exam']/following-sibling::div//td[4]");
    private final By antibodiesInTableLocator = By.xpath("//h3[text()='Immunohematology exam']/following-sibling::div//td[5]");
    private final By hepatitisBStatusInTable = By.xpath("//h3[text()='Serological screening exam']/following-sibling::div//td[4]");
    private final By hepatitisCStatusInTable = By.xpath("//h3[text()='Serological screening exam']/following-sibling::div//td[5]");
    private final By chagasDiseaseStatusInTable = By.xpath("//h3[text()='Serological screening exam']/following-sibling::div//td[6]");
    private final By syphilisStatusInTable = By.xpath("//h3[text()='Serological screening exam']/following-sibling::div//td[7]");
    private final By aidsStatusInTable = By.xpath("//h3[text()='Serological screening exam']/following-sibling::div//td[8]");
    private final By htlvStatusInTable = By.xpath("//h3[text()='Serological screening exam']/following-sibling::div//td[9]");


    public DonationPageObject(WebDriver driver) {
        super(driver);
        if (!currentUrl().contains("/donation")) throw new IllegalStateException("Wrong page url: " + driver.getCurrentUrl());
    }

    public AuthenticationPageObject logout() {
        WebElement logoutButton = driver.findElement(By.id("logout-desktop-button"));
        logoutButton.click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains("/login"));
        return new AuthenticationPageObject(driver);
    }

    public DonationPageObject clickOnBDMButton() {
        WebElement button = driver.findElement(By.xpath("//*[@id=\"navbar-link-home\"]/p"));
        button.click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains("/donation"));
        return new DonationPageObject(driver);
    }

    public boolean isDonationTitleVisible() {
        return driver.findElement(donationTitle).isDisplayed();
    }

    public void clickOnRegisterTabButton() {
        driver.findElement(registerTabButton).click();
    }

    public boolean isRegisterDonationStepOneTextVisible() {
        List<WebElement> elements = driver.findElements(registerDonationStepOneText);

        return !elements.isEmpty();
    }

    private void selectADonorToRegister(String donorName) {
        WebElement donorSelectElement = driver.findElement(donorSelect);
        donorSelectElement.sendKeys(donorName);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.textToBePresentInElementValue(donorSelectElement, donorName)
        );
        donorSelectElement.sendKeys(Keys.ENTER);
    }

    private void selectFirstAppointmentToRegister() {
        WebElement appointmentSelectElement = driver.findElement(appointmentSelect);
        appointmentSelectElement.click();

        WebElement firstAppointment = driver.findElement(By.xpath("//span[contains(text(), 'Banco de Doação')]"));
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(firstAppointment));

        firstAppointment.click();
    }

    private void selectImmunohematologyCheckboxToRegister() {
        driver.findElement(immunohematologyCheckbox).click();
    }

    private void selectSerologicalCheckboxToRegister() {
        driver.findElement(serologicalCheckbox).click();
    }

    public void clickOnUpdateTabButton() {
        driver.findElement(updateTabButton).click();
    }

    public void clickOnViewTabButton() {
        driver.findElement(viewTabButton).click();
    }

    public void scrollAndClickOnViewTabButton() {
        By tabListLocator = By.cssSelector("[data-slot='tabList']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement tabListElement = wait.until(ExpectedConditions.visibilityOfElementLocated(tabListLocator));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", tabListElement);

        if (!isInViewport(tabListElement)) {
            throw new IllegalStateException("The tabList element is not fully visible inside the viewport after scrolling.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(viewTabButton)).click();
    }

    private boolean isInViewport(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object result = js.executeScript(
                "var rect = arguments[0].getBoundingClientRect();" +
                        "return (" +
                        "rect.top >= 0 && " +
                        "rect.left >= 0 && " +
                        "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "rect.right <= (window.innerWidth || document.documentElement.clientWidth)" +
                        ");",
                element
        );

        return Boolean.TRUE.equals(result);
    }

    public boolean isUpdateTabActive() {
        return !driver.findElements(activeUpdateTabLocator).isEmpty();
    }

    public boolean isViewTabActive() {
        return !driver.findElements(activeViewTabLocator).isEmpty();
    }

    public void registerDonationWithImmunohematologyExam(String donorName) {
        clickOnRegisterTabButton();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(donorSelect)));
        selectADonorToRegister(donorName);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(appointmentSelect)));
        selectFirstAppointmentToRegister();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(immunohematologyCheckbox)));
        selectImmunohematologyCheckboxToRegister();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.attributeContains(driver.findElement(immunohematologyCheckbox), "data-selected", "true"));
        driver.findElement(By.id("register-donation-button")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.invisibilityOf(driver.findElement(appointmentSelect)));
    }

    public void registerDonationWithSerologicalExam(String donorName) {
        clickOnRegisterTabButton();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(donorSelect)));
        selectADonorToRegister(donorName);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(appointmentSelect)));
        selectFirstAppointmentToRegister();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(serologicalCheckbox)));
        selectSerologicalCheckboxToRegister();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.attributeContains(driver.findElement(serologicalCheckbox), "data-selected", "true"));
        driver.findElement(By.id("register-donation-button")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.invisibilityOf(driver.findElement(appointmentSelect)));
    }

    public void registerDonationWithAllExams(String donorName) {
        clickOnRegisterTabButton();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(donorSelect)));
        selectADonorToRegister(donorName);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(appointmentSelect)));
        selectFirstAppointmentToRegister();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(immunohematologyCheckbox)));
        selectImmunohematologyCheckboxToRegister();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.attributeContains(driver.findElement(immunohematologyCheckbox), "data-selected", "true"));
        selectSerologicalCheckboxToRegister();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.attributeContains(driver.findElement(serologicalCheckbox), "data-selected", "true"));
        driver.findElement(By.id("register-donation-button")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.invisibilityOf(driver.findElement(appointmentSelect)));
    }

    public String getDonorOfView(String donorName) {
        By donorNameElementBy = By.xpath("//p[text()='" + donorName + "']");
        WebElement donorNameElement = driver.findElement(donorNameElementBy);
        return donorNameElement.getText();
    }

    public int getQuantityOfExamUnderAnalysisOfView() {
        By underAnalysis = By.xpath("//p[text()='UNDER ANALYSIS']");
        List<WebElement> elements = driver.findElements(underAnalysis);
        return elements.size();
    }

    public String getColumnTextOfView(String columnName) {
        final By locator = RelativeLocator.with(By.tagName("p"))
                .below(By.xpath("//th[text()='" + columnName + "']"));
        WebElement element = driver.findElement(locator);
        return element.getText();
    }


    public void viewDonationRegistered(String donorName) {
        clickOnViewTabButton();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(donationAutocompleteSelectButton)));
        driver.findElement(donationAutocompleteSelectButton).sendKeys(donorName);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.textToBePresentInElementValue(donationAutocompleteSelectButton, donorName));
        driver.findElement(donationAutocompleteSelectButton).sendKeys(Keys.ENTER);
        By donorNameElementBy = By.xpath("//p[text()='" + donorName + "']");
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(donorNameElementBy)));
    }

    public void selectDonationInList(String donorName) {
        WebElement selectDonation = driver.findElement(donationAutocompleteSelectButton);
        selectDonation.sendKeys(donorName);

        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.textToBePresentInElementValue(selectDonation, donorName)
        );

        selectDonation.sendKeys(Keys.ENTER);
    }

    public UpdateImmunoExamPageObject clickUpdateForImmunohematologyExam() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(updateImmunoExamLink));
        link.click();

        wait.until(ExpectedConditions.urlContains("/exams/immunohematology/"));

        return new UpdateImmunoExamPageObject(driver);
    }

    public UpdateSeroExamPageObject clickUpdateForSerologicalExam() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(updateSeroExamLink));
        link.click();

        wait.until(ExpectedConditions.urlContains("/exams/serological-screening/"));

        return new UpdateSeroExamPageObject(driver);
    }

    public String donationRequestMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOf(driver
            .findElement(By.xpath("//*[text()='Donation requested']")))).getText();
    }

    public String immunohematologyExamRequestMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOf(driver
                .findElement(By.xpath("//*[text()='Immunohematology exam requested']")))).getText();
    }

    public String serologicalExamRequestMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOf(driver
                .findElement(By.xpath("//*[text()='Serological screening exam requested']")))).getText();
    }

    public String getUpdatedBloodTypeTextFromTable() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(bloodTypeInTableLocator, "N/A")));

        return driver.findElement(bloodTypeInTableLocator).getText();
    }

    public String getUpdatedAntibodiesTextFromTable() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(antibodiesInTableLocator, "N/A")));

        return driver.findElement(antibodiesInTableLocator).getText();
    }

    private String getTextFromElementWithScroll(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

        wait.until(ExpectedConditions.visibilityOf(element));

        return element.getText();
    }

    public String getUpdatedHepatitisBStatus() {
        return getTextFromElementWithScroll(hepatitisBStatusInTable);
    }

    public String getUpdatedHepatitisCStatus() {
        return getTextFromElementWithScroll(hepatitisCStatusInTable);
    }

    public String getUpdatedChagasDiseaseStatus() {
        return getTextFromElementWithScroll(chagasDiseaseStatusInTable);
    }

    public String getUpdatedSyphilisStatus() {
        return getTextFromElementWithScroll(syphilisStatusInTable);
    }

    public String getUpdatedAidsStatus() {
        return getTextFromElementWithScroll(aidsStatusInTable);
    }

    public String getUpdatedHtlvStatus() {
        return getTextFromElementWithScroll(htlvStatusInTable);
    }

    public void updateExams(String donorName, String bloodType, String immunoObservations, String seroObservations) {
        String negative = "Negative";

        registerDonationWithAllExams(donorName);
        clickOnUpdateTabButton();
        selectDonationInList(donorName);

        UpdateImmunoExamPageObject immunoPage = clickUpdateForImmunohematologyExam();

        immunoPage.selectBloodType(bloodType);
        immunoPage.selectIrregularAntibodies(negative);
        immunoPage.fillObservations(immunoObservations);

        immunoPage.clickApproveButtonAndExpectSuccess();

        clickOnUpdateTabButton();
        selectDonationInList(donorName);

        UpdateSeroExamPageObject seroPage = clickUpdateForSerologicalExam();

        seroPage.selectDiseaseStatus("hepatitisB", negative);
        seroPage.selectDiseaseStatus("hepatitisC", negative);
        seroPage.selectDiseaseStatus("chagasDisease", negative);
        seroPage.selectDiseaseStatus("syphilis", negative);
        seroPage.selectDiseaseStatus("aids", negative);
        seroPage.selectDiseaseStatus("htlv1_2", negative);
        seroPage.fillObservations(seroObservations);

        seroPage.clickApproveButtonAndExpectSuccess();
    }

    public void updateRejectExams(String donorName, String bloodType, String immunoObservations, String seroObservations) {
        String positive = "Positive";
        String negative = "Negative";

        registerDonationWithAllExams(donorName);
        clickOnUpdateTabButton();
        selectDonationInList(donorName);

        UpdateImmunoExamPageObject immunoPage = clickUpdateForImmunohematologyExam();

        immunoPage.selectBloodType(bloodType);
        immunoPage.selectIrregularAntibodies(positive);
        immunoPage.fillObservations(immunoObservations);

        immunoPage.clickRejectButtonAndExpectSuccess();

        clickOnUpdateTabButton();
        selectDonationInList(donorName);

        UpdateSeroExamPageObject seroPage = clickUpdateForSerologicalExam();

        seroPage.selectDiseaseStatus("hepatitisB", positive);
        seroPage.selectDiseaseStatus("hepatitisC", positive);
        seroPage.selectDiseaseStatus("chagasDisease", negative);
        seroPage.selectDiseaseStatus("syphilis", positive);
        seroPage.selectDiseaseStatus("aids", negative);
        seroPage.selectDiseaseStatus("htlv1_2", positive);
        seroPage.fillObservations(seroObservations);

        seroPage.clickRejectButtonAndExpectSucceess();
    }

    public ViewImmunohematologyObject cickOnViewImmunohematologyButton() {
        By immunohematologyButton = By.xpath("//a[starts-with(@href, '/exams/immunohematology/')]");
        WebElement button = driver.findElement(immunohematologyButton);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(
                button
        ));
        button.click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(
                "/exams/immunohematology/"
        ));
        return new ViewImmunohematologyObject(driver);
    }

    public ViewSerologicalObject cickOnViewSerologicalButton() {
        By immunohematologyButton = By.xpath("//a[starts-with(@href, '/exams/serological-screening/')]");
        WebElement button = driver.findElement(immunohematologyButton);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(
                button
        ));
        button.click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(
                "/exams/serological-screening/"
        ));
        return new ViewSerologicalObject(driver);
    }
}
