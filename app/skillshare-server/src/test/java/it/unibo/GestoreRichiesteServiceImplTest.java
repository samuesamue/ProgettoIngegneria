package it.unibo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

public class GestoreRichiesteServiceImplTest {

    // Accensione database finto
    @BeforeAll
    public static void testmode() {
        DatabaseCore.enableTestMode();
    }

    // Spegnimento database finto
    @AfterAll 
    public static void testmodeoff() {
        DatabaseCore.disableTestMode();
    }

    @Test
    @DisplayName("Invia Richiesta: Scenario di successo")
    public void testInviaRichiesta() throws Exception {

        // Preparazione dei dati di test
        GestoreRichiesteServiceImpl servizio = new GestoreRichiesteServiceImpl();
        RichiestaScambio richiesta = new RichiestaScambio("", "annuncio123", "Mario", "Luigi");

        // Invio della richiesta
        Boolean risultato = servizio.inviaRichiesta(richiesta);

        // Controlliamo che il risultato sia true e che l'ID sia stato generato
        assertTrue(risultato,"La richiesta valida dovrebbe restituire true");
        assertNotNull(richiesta.getIdRichiesta(), "Il server avrebbe dovuto generare un ID per la richiesta");
    }

    @Test
    @DisplayName("Invia Richiesta: Blocco auto-scambio")
    public void testBloccoAutoScambio() throws Exception {

        // Preparazione dei dati di test
        GestoreRichiesteServiceImpl servizio = new GestoreRichiesteServiceImpl();
        RichiestaScambio richiesta = new RichiestaScambio("", "annuncio456", "Mario", "Mario");

        // Invio della richiesta
        Boolean risultato = servizio.inviaRichiesta(richiesta);

        // Il server DEVE aver bloccato l'operazione restituendo false
        assertFalse(risultato, "La richiesta di auto-scambio dovrebbe restituire false");
    }
}
