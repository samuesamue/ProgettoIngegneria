package it.unibo.acceptance;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class GestoreAnnunciIT {

    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080/");
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private static WebDriver driver;
    private GestoreAnnunciPage pagina;

    @BeforeAll
    static void avviaBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (!"false".equalsIgnoreCase(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterAll
    static void chiudiBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void vaiAllaPagina() {
        pagina = new GestoreAnnunciPage(driver, TIMEOUT).apri(BASE_URL);
    }

    @Nested
    @DisplayName("Quando il form è vuoto")
    class QuandoIlFormEVuoto {

        @Test
        @DisplayName("evidenzia in rosso tutti i campi obbligatori")
        void formVuoto_evidenziaTuttiICampi() {
            // Act
            pagina.pubblica();

            // Assert: tutti i campi devono avere il bordo rosso
            assertTrue(pagina.campoInErrore(pagina.campoTitolo()));
            assertTrue(pagina.campoInErrore(pagina.campoDescrizione()));
            assertTrue(pagina.campoInErrore(pagina.campoOfferta()));
            assertTrue(pagina.campoInErrore(pagina.campoRichiesta()));

            assertTrue(pagina.testoMessaggio().toLowerCase().contains("controlla gli errori"),
                    "Messaggio ottenuto: " + pagina.testoMessaggio());
        }
    }

    @Nested
    @DisplayName("Quando un solo campo è vuoto")
    class QuandoUnSoloCampoEVuoto {

        @Test
        @DisplayName("manca il titolo, evidenzia solo il campo titolo")
        void mancaTitolo_evidenziaSoloTitolo() {
            // Arrange + Act: Compilo tutto tranne il titolo
            pagina.compila("", "Una bellissima descrizione", "Java", "Python");
            pagina.pubblica();

            // Assert: solo il titolo è in errore
            assertTrue(pagina.campoInErrore(pagina.campoTitolo()));
            assertFalse(pagina.campoInErrore(pagina.campoDescrizione()));
            assertFalse(pagina.campoInErrore(pagina.campoOfferta()));
            assertFalse(pagina.campoInErrore(pagina.campoRichiesta()));
        }
    }

    @Nested
    @DisplayName("Quando i dati sono tutti validi")
    class QuandoIDatiSonoValidi {

        @Test
        @DisplayName("la pubblicazione va a buon fine e il form viene svuotato")
        void datiValidi_pubblicazioneCompletataConSuccesso() {
            // Arrange
            String titoloTest = "Lezione GWT " + System.currentTimeMillis();

            // Act
            pagina.compila(titoloTest, "Insegno GWT base", "GWT", "Spring Boot");
            pagina.pubblica();

            // Assert
            assertTrue(pagina.testoMessaggio().toLowerCase().contains("successo"),
                    "Messaggio ottenuto: " + pagina.testoMessaggio());
            
            // Verifica che il form sia stato pulito
            assertEquals("", pagina.campoTitolo().getAttribute("value"));
        }
    }
}