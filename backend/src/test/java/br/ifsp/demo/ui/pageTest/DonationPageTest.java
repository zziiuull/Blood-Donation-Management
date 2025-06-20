package br.ifsp.demo.ui.pageTest;

import br.ifsp.demo.ui.pageObject.DonationPageObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class DonationPageTest extends BaseSeleniumTest {
    @Override
    protected void setInitialPage() {
        String page = "http://localhost:3000/donation";
        driver.get(page);
    }

    @Test
    @Tag("UiTest")
    @DisplayName("Should navigate to login if user is not logged in")
    void shouldNavigateToLoginIfUserIsNotLoggedIn() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/login"));
        assertThat(driver.getCurrentUrl()).contains("/login");
    }
}
