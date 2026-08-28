package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;

public interface Registrazione extends RemoteService {
    boolean registra(Utente u);
}
