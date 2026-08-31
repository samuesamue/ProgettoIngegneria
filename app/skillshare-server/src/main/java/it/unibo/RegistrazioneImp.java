package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

public class RegistrazioneImp extends RemoteServiceServlet implements GestoreAutenticazione {

    // Istanziamo il Proxy perchè la Servlet non parla mai direttamente col DB.
    //Il proxy si occuperà dei controlli
    private final GestoreAutenticazione proxy = new AutenticatoreProxy();

    @Override
    public Boolean registraUtente(Utente utente) throws Exception {
        // La Servlet riceve la chiamata dal Web e la passa al Proxy per i controlli
        return proxy.registraUtente(utente);
    }

    @Override
    public Utente effettuaLogin(String mail, String password) throws IllegalArgumentException {
        // Stessa cosa per il login
        return proxy.effettuaLogin(mail, password);
    }
}