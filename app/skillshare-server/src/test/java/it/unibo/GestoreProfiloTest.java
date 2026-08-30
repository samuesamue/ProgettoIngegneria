package it.unibo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestoreProfiloTest{

    private GestoreProfiloImpl gestoreProfilo;

    @BeforeEach
    public void setUp(){
        //Inizializziamo il nostro servizio prima di ogni test
        gestoreProfilo = new GestoreProfiloImpl();
    }

    @Test
    public void testAggiornamentoProfiloRiuscito(){
        //GIVEN: creiamo un utente di base
        Utente utente = new Utente("Ludovica", "Govoni");
        utente.setUsername("govonsx");

        //Simuliamo inserimento dati dal frontend
        utente.setBio("Appassionata di uncinetto.");
        utente.setTagCompetenze("Uncinetto, Ricamo, Maglia");

        try{
            //WHEN: chiamiamo metodo per salvare
            Utente utenteSalvato = gestoreProfilo.aggiornaProfilo(utente);

            //THEN: verifichiamo che i dati siano stati salvati e restituiti correttamente
            assertNotNull(utenteSalvato, "L'utente salvato non dovrebbe essere null");
            assertEquals("Appassionata di uncinetto.", utenteSalvato.getBio());
            assertEquals("Uncinetto, Ricamo, Maglia", utenteSalvato.getTagCompetenze());
        }catch (Exception e){
            fail("L'aggiornamento ha lanciato un'eccezione: "+e.getMessage());
        }
    }
}
