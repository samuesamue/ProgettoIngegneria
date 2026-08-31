package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("profilo")
public interface GestoreProfilo extends RemoteService {
    //Passiamo l'oggetto Utente aggiornato e ci facciamo restituire l'utente salvato
    Utente aggiornaProfilo(Utente utenteAggiornato) throws Exception;    
}
