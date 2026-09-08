package it.unibo.acceptance;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BachecaPropostePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Mappiamo gli elementi della pagina usando gli ID definiti nella GUI
    @FindBy(id = "tabella-proposte")
    private WebElement tabellaProposte;

    @FindBy(id = "msg-nessuna-proposta")
    private WebElement messaggioVuoto;

    public BachecaPropostePage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        PageFactory.initElements(driver, this);
    }

    // Controlla se la tabella delle proposte è visibile
    public boolean isTabellaVisibile() {
        try {
            wait.until(ExpectedConditions.visibilityOf(tabellaProposte));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMessaggioVuotoVisibile() {
        try {
            wait.until(ExpectedConditions.visibilityOf(messaggioVuoto));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
