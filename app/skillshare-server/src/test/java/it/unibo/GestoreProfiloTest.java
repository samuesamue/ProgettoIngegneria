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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// TEST DELLA SERVLET
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GestoreProfiloTest{

    // Configurazione standard per testare una GWT Servlet 
    @Mock private ServletConfig servletConfig;
    @Mock private ServletContext servletContext;
    @Mock private HttpServletRequest request;

    private GestoreProfiloImpl gestoreProfilo;
    private Utente utenteDiTest;

    @BeforeAll
    static void testmode(){
        DatabaseCore.enableTestMode();
    }

    @BeforeEach
    public void setUp() throws Exception{
        // Inizializzazione finta del web server
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getServerInfo()).thenReturn("MockServer/1.0");
        when(request.getHeader("User-Agent")).thenReturn("MockBrowser/1.0");

        gestoreProfilo = new GestoreProfiloImpl();
        gestoreProfilo.init(servletConfig);

        ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();
        threadLocal.set(request);

        Field field = AbstractRemoteServiceServlet.class.getDeclaredField("perThreadRequest");
        field.setAccessible(true);
        field.set(gestoreProfilo, threadLocal);

        //Preparazione dell'utente di test 
        utenteDiTest = new Utente("Ludovica", "Govoni");
        utenteDiTest.setMail("ludovica.govoni@gmail.com");
        utenteDiTest.setUsername("govonsx");
        utenteDiTest.setBio("Nel tempo libero creo capi all'uncinetto");
        utenteDiTest.setTagCompetenze("Uncinetto, Maglia, Ricamo");
    }

    @AfterAll
    static void testmodeoff() {
        DatabaseCore.disableTestMode();
    }

    // TEST DI ACCETTAZIONE 
    @Test
    void aggiornaProfilo_ConDatiCorretti_DeveTornareUtenteAggiornato() {
        try {
            Utente utenteSalvato = gestoreProfilo.aggiornaProfilo(utenteDiTest);
            
            assertNotNull(utenteSalvato, "L'utente salvato non deve essere nullo");
            assertEquals("Nel tempo libero creo capi all'uncinetto", utenteSalvato.getBio());
            assertEquals("Uncinetto, Maglia, Ricamo", utenteSalvato.getTagCompetenze());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
