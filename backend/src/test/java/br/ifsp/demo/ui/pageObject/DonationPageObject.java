package br.ifsp.demo.ui.pageObject;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Slf4j
public class DonationPageObject extends BasePageObject {

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

    public boolean isViewingDonation(String donorName) {
        By donorNameElementBy = By.xpath("//p[text()='" + donorName + "']");
        WebElement donorNameElement = driver.findElement(donorNameElementBy);
        return donorNameElement.getText().equals(donorName);
    }

    public void viewDonationRegistered(String donorName) throws InterruptedException {
        clickOnViewTabButton();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(donationAutocompleteSelectButton)));
        driver.findElement(donationAutocompleteSelectButton).sendKeys(donorName);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.textToBePresentInElementValue(donationAutocompleteSelectButton, donorName));
        driver.findElement(donationAutocompleteSelectButton).sendKeys(Keys.ENTER);
        By donorNameElementBy = By.xpath("//p[text()='" + donorName + "']");
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(driver.findElement(donorNameElementBy)));
    }

    public void clickOnDonationAutocompleteSelectButton() {
        driver.findElement(donationAutocompleteSelectButton).click();
    }

    public void selectFirstDonationInList() {
        driver.findElement(donationAutocompleteSelectButton).click();

        By firstOptionLocator = By.xpath("(//div[@role='listbox']//li[@role='option'])[1]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement firstOption = wait.until(ExpectedConditions.elementToBeClickable(firstOptionLocator));

        firstOption.click();
    }

    public UpdateImmunoExamPageObject clickUpdateForImmunohematologyExam() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(updateImmunoExamLink));
        link.click();

        return new UpdateImmunoExamPageObject(driver);
    }

    public UpdateSeroExamPageObject clickUpdateForSerologicalExam() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(updateSeroExamLink));
        link.click();

        return new UpdateSeroExamPageObject(driver);
    }
}
