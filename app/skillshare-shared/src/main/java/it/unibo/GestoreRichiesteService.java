package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

// Indirizzo relativo per il servizio remoto
@RemoteServiceRelativePath("richieste")
public interface GestoreRichiesteService extends RemoteService {

    // Metodo per inviare una richiesta di scambio
    Boolean inviaRichiesta(RichiestaScambio richiesta) throws Exception;
}
