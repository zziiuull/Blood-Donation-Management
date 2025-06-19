package br.ifsp.demo.ui.pageTest;

public class RegisterPageTest extends BaseSeleniumTest{

    @Override
    protected void setInitialPage(){
        String page = "http://localhost:3000/register";
        driver.get(page);
    }
}
