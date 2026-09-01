package it.unibo.acceptance;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PersonalizzaProfiloPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "prof-bio")
    private WebElement campoBio;

    @FindBy(id = "prof-foto")
    private WebElement campoFoto;

    @FindBy(id = "prof-tag")
    private WebElement campoTag;

    @FindBy(id = "prof-submit")
    private WebElement bottoneSalva;

    @FindBy(id = "prof-message")
    private WebElement messaggio;

    public PersonalizzaProfiloPage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        PageFactory.initElements(driver, this);
    }

    public PersonalizzaProfiloPage apri(String baseUrl) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("prof-submit")));
        return this;
    }

    public PersonalizzaProfiloPage compila(String bio, String foto, String tag) {
        campoBio.clear();
        campoBio.sendKeys(bio);
        campoFoto.clear();
        campoFoto.sendKeys(foto);
        campoTag.clear();
        campoTag.sendKeys(tag);
        return this;
    }

    public PersonalizzaProfiloPage salva() {
        bottoneSalva.click();
        wait.until(d -> !messaggio.getText().isEmpty());
        return this;
    }

    public String testoMessaggio() {
        return messaggio.getText();
    }

    public boolean campoInErrore(WebElement campo) {
        String bordo = campo.getCssValue("border-top-color");
        // Verifica il colore rosso di errore (es. #e53935 in rgb)
        return bordo.replace(" ", "").contains("229,57,53");
    }

    public WebElement campoBio() { return campoBio; }
    public WebElement campoFoto() { return campoFoto; }
    public WebElement campoTag() { return campoTag; }
}