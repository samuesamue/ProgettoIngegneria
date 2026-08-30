package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

@SuppressWarnings("serial")
public class GestoreProfiloImpl extends RemoteServiceServlet implements GestoreProfilo {

    @Override
    public Utente aggiornaProfilo(Utente utenteAggiornato) throws Exception {
        
        //Recuperiamo l'istanza del database MapDB
        DB db = DatabaseCore.getDB();
        
        //Apriamo (o creiamo) la mappa che contiene tutti gli utenti.
        HTreeMap<String, Utente> mappaUtenti = db.hashMap("utenti")
            .keySerializer(Serializer.STRING)
            .valueSerializer(Serializer.JAVA)
            .createOrOpen();

        //Salviamo l'utente con i nuovi dati
        mappaUtenti.put(utenteAggiornato.getUsername(), utenteAggiornato);

        //Salvare fisicamente le modifiche sul file del database
        DatabaseCore.commit();

        //Restituiamo l'utente aggiornato
        return utenteAggiornato;
    }
}