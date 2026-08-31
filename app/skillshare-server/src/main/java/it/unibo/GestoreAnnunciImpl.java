package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class GestoreAnnunciImpl extends RemoteServiceServlet implements GestoreAnnunci {

    @Override
    public Boolean pubblicaAnnuncio(Annuncio annuncio) throws Exception {
        // Recuperiamo l'istanza del database tramite la classe core
        DB db = DatabaseCore.getDB();
        
        try {
            // Apriamo (o creiamo) la mappa che contiene tutti gli annunci
            HTreeMap<String, Annuncio> mappaAnnunci = db.hashMap("annunci")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
            
            // Salviamo l'annuncio usando il suo ID univoco
            mappaAnnunci.put(annuncio.getId(), annuncio);
            
            // Salvare fisicamente le modifiche sul file del database
            DatabaseCore.commit();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Annuncio> ottieniTuttiGliAnnunci() throws Exception {
        DB db = DatabaseCore.getDB();
        List<Annuncio> listaAnnunci = new ArrayList<>();
        
        try {
            HTreeMap<String, Annuncio> mappaAnnunci = db.hashMap("annunci")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
            
            // Prendiamo tutti i valori dalla mappa e li mettiamo nella nostra lista
            listaAnnunci.addAll(mappaAnnunci.values());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaAnnunci;
    }
}