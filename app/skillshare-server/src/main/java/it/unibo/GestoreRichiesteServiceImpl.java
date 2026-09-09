package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.UUID;

@SuppressWarnings("unchecked")
public class GestoreRichiesteServiceImpl extends RemoteServiceServlet implements GestoreRichiesteService {

    @Override
    public Boolean inviaRichiesta(RichiestaScambio richiesta) throws Exception {
        
        // Blocco auto-scambio
        if (richiesta.getRichiedente().equals(richiesta.getRicevente())){
            return false; // Fallisce perché l'utente non può inviare una richiesta a se stesso
        }

        // Generere un ID univoco per la richiesta
        if (richiesta.getIdRichiesta() == null || richiesta.getIdRichiesta().isEmpty()) {
            richiesta.setIdRichiesta(UUID.randomUUID().toString());
        }
        
        DB db = DatabaseCore.getDB();

        try{
            
            // Apriamo (o creiamo) la mappa che contiene tutte le richieste
            HTreeMap<String, RichiestaScambio> mappaRichieste = db.hashMap("richieste")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
            
            // Salviamo la richiesta usando il suo ID univoco
            mappaRichieste.put(richiesta.getIdRichiesta(), richiesta);
            
            // Salvare fisicamente le modifiche sul file del database
            DatabaseCore.commit();

            // Trucco di DEBUG: stampiamo a console tutte le richieste salvate nel database
            System.out.println("\n----------------- IL SERVER HA AGGIORNATO IL DATABASE ------------------");
            System.out.println("Numero totale di richieste salvate: " + mappaRichieste.size());
            
            for (RichiestaScambio r : mappaRichieste.values()) {
                System.out.println("👤 ID: " + r.getIdRichiesta() 
                        + " | Mittente: " + r.getRichiedente() 
                        + " | Destinatario: " + r.getRicevente() 
                        + " | Stato: " + r.getStato());
            }
            System.out.println("------------------------------------------------------------------------\n");
            
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Fallisce in caso di errore
        }
    }
}
