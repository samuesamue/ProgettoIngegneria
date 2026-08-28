package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RegistrazioneImp extends RemoteServiceServlet implements GestoreAutenticazione {

    // Istanziamo il Proxy perchè la Servlet non parla mai direttamente col DB.
    //il proxy si occuperà dei controlli
    private final GestoreAutenticazione proxy = new AutenticatoreProxy();

    @Override
    public Boolean registraUtente(Utente utente) throws Exception {
        // La Servlet riceve la chiamata dal Web e la passa al Proxy per i controlli
        return proxy.registraUtente(utente);
    }

    @Override
    public Utente effettuaLogin(String username, String password) throws IllegalArgumentException {
        // Stessa cosa per il login
        return proxy.effettuaLogin(username, password);
    }
}