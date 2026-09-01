package it.unibo.acceptance;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GestoreAnnunciPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Mappatura degli elementi basata sugli ID che hai inserito nella GUI
    @FindBy(id = "annuncio-titolo")    private WebElement campoTitolo;
    @FindBy(id = "annuncio-desc")      private WebElement campoDescrizione;
    @FindBy(id = "annuncio-offerta")   private WebElement campoOfferta;
    @FindBy(id = "annuncio-richiesta") private WebElement campoRichiesta;
    @FindBy(id = "annuncio-submit")    private WebElement bottonePubblica;
    @FindBy(id = "annuncio-message")   private WebElement messaggio;

    public GestoreAnnunciPage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        PageFactory.initElements(driver, this);
    }

    public GestoreAnnunciPage apri(String baseUrl) {
        driver.get(baseUrl);
        // Attende che il bottone di pubblicazione sia cliccabile
        wait.until(ExpectedConditions.elementToBeClickable(By.id("annuncio-submit")));
        return this;
    }

    // Metodo per compilare tutto il form in un colpo solo
    public GestoreAnnunciPage compila(String titolo, String descrizione, String offerta, String richiesta) {
        campoTitolo.clear();
        campoTitolo.sendKeys(titolo);
        
        campoDescrizione.clear();
        campoDescrizione.sendKeys(descrizione);
        
        campoOfferta.clear();
        campoOfferta.sendKeys(offerta);
        
        campoRichiesta.clear();
        campoRichiesta.sendKeys(richiesta);
        
        return this;
    }

    public GestoreAnnunciPage pubblica() {
        bottonePubblica.click();
        // Attende che il messaggio a schermo non sia vuoto
        wait.until(d -> !messaggio.getText().isEmpty());
        return this;
    }

    public String testoMessaggio() {
        return messaggio.getText();
    }

    // Verifica la presenza del bordo rosso (#e53935)
    public boolean campoInErrore(WebElement campo) {
        String bordo = campo.getCssValue("border-top-color");
        return bordo.replace(" ", "").contains("229,57,53");
    }

    // Getter
    public WebElement campoTitolo() { return campoTitolo; }
    public WebElement campoDescrizione() { return campoDescrizione; }
    public WebElement campoOfferta() { return campoOfferta; }
    public WebElement campoRichiesta() { return campoRichiesta; }
}