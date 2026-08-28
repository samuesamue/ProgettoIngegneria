package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GestoreAutenticazioneAsync {

    void registraUtente(Utente utente, AsyncCallback<Boolean> callback);

    void effettuaLogin(String username, String password, AsyncCallback<Utente> callback);

}