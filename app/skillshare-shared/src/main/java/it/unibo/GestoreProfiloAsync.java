package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GestoreProfiloAsync{
    void aggiornaProfilo(Utente utenteAggiornato, AsyncCallback<Utente> callback);
}
