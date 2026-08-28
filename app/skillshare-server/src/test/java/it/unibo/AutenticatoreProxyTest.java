package it.unibo;

import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class AutenticatoreProxyTest {

    private AutenticatoreProxy proxy;
    private Utente utenteValido;
    private Utente utenteNonValido;

    @BeforeAll
    public static void testmode() {
        DatabaseCore.enableTestMode();
    }

    @BeforeEach
    public void setUp() {
        proxy = new AutenticatoreProxy();

        // 2. Prepariamo l'utente valido
        utenteValido = new Utente("Mario", "Rossi");
        utenteValido.setUsername("mario123");
        utenteValido.setMail("mario@mail.com");
        utenteValido.setPassword("passwordSicura123");
        utenteValido.setData(new Date());

        // 3. Prepariamo l'utente non valido
        utenteNonValido = new Utente("L", "Ver");
        utenteNonValido.setUsername("");
        utenteNonValido.setMail("mariomailcom");
        utenteNonValido.setPassword("pas23");
        utenteNonValido.setData(new GregorianCalendar(1800, Calendar.DECEMBER, 31).getTime());
    }



    @Test
    public void registraUtente_datiValidi_deveTornareTrue() throws Exception {
        // Eseguiamo la registrazione (salverà nella RAM)
        Boolean risultato = proxy.registraUtente(utenteValido);
        assertTrue(risultato);
    }

    @Test
    public void registraUtente_nomeTroppoCorto_deveLanciareEccezione() {
        utenteNonValido.setNome("Ma"); // Non valido!
        IllegalArgumentException eccezione = assertThrows(IllegalArgumentException.class,
                () -> proxy.registraUtente(utenteNonValido));
        assertTrue(eccezione.getMessage().contains("Nome non valido"));
    }

    @Test
    public void registraUtente_passwordMancante_deveLanciareEccezione() {
        utenteNonValido.setPassword("");
        assertThrows(IllegalArgumentException.class,
                () -> proxy.registraUtente(utenteNonValido));
    }


    @AfterAll
    public static void testmodeoff() {
        DatabaseCore.disableTestMode();
        //DatabaseUtente.resetPerTest();
    }
}