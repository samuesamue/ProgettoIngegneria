package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.AbstractRemoteServiceServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//TEST DELLA SERVLET ANNUNCI
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GestoreAnnunciTest {

    // Configurazione standard per testare una GWT Servlet
    @Mock private ServletConfig servletConfig;
    @Mock private ServletContext servletContext;
    @Mock private HttpServletRequest request;

    private GestoreAnnunciImpl gestore;
    private Annuncio annuncioValido;

    @BeforeAll
    static void testmode() {
        // Attiviamo il database finto per i test
        DatabaseCore.enableTestMode();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Inizializzazione finta del Web Server
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getServerInfo()).thenReturn("MockServer/1.0");
        when(request.getHeader("User-Agent")).thenReturn("MockBrowser/1.0");

        gestore = new GestoreAnnunciImpl();
        gestore.init(servletConfig);

        // Iniezione della request finta tramite reflection
        ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();
        threadLocal.set(request);

        Field field = AbstractRemoteServiceServlet.class.getDeclaredField("perThreadRequest");
        field.setAccessible(true);
        field.set(gestore, threadLocal);

        // Prepariamo l'annuncio per il test
        annuncioValido = new Annuncio(
            "Scambio Lezioni di Chitarra",
            "Offro lezioni base di chitarra in cambio di ripetizioni di Java.",
            "Chitarra",
            "Programmazione Java",
            "utente_test_01"
        );
    }

    @AfterAll
    static void testmodeoff() {
        // Spegniamo il database di test
        DatabaseCore.disableTestMode();
    }

    // TEST DI ACCETTAZIONE
    @Test
    void testPubblicazioneERecuperoAnnuncio() {
        try {
            //Verifichiamo che il salvataggio vada a buon fine tornando true
            assertTrue(gestore.pubblicaAnnuncio(annuncioValido));

            //Recuperiamo gli annunci e verifichiamo che i dati siano intatti
            List<Annuncio> tuttiGliAnnunci = gestore.ottieniTuttiGliAnnunci();
            assertNotNull(tuttiGliAnnunci);

            boolean trovato = false;
            for (Annuncio a : tuttiGliAnnunci) {
                if (a.getId().equals(annuncioValido.getId())) {
                    trovato = true;
                    assertEquals("Scambio lezioni di chitarra", a.getTitolo());
                    assertEquals("utente_test_01", a.getAutoreUsername());
                    break;
                }
            }
            assertTrue(trovato, "L'annuncio appena creato deve essere nel database");

        } catch (Exception e) {
            fail("Il test ha lanciato un'eccezione imprevista: " + e.getMessage());
        }
    }
}