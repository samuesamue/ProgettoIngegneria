package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GestoreRichiesteServiceAsync {

    void inviaRichiesta(RichiestaScambio richiesta, AsyncCallback<Boolean> callback);
}
