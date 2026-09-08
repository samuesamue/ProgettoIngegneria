package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
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

    @Override
    public List<Annuncio> ottieniAnnunciUtente(String username) throws Exception {
        DB db = DatabaseCore.getDB();
        List<Annuncio> listaAnnunci = new ArrayList<>();
        
        try {
            HTreeMap<String, Annuncio> mappaAnnunci = db.hashMap("annunci")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
            
            // Filtriamo solo gli annunci dell'utente richiesto
            for (Annuncio annuncio : mappaAnnunci.values()) {
                if (annuncio.getAutoreUsername().equals(username)) {
                    listaAnnunci.add(annuncio);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        
        return listaAnnunci;
    }

    @Override
    public Boolean modificaAnnuncio(Annuncio annuncioModificato) throws Exception {
        DB db = DatabaseCore.getDB();
        try {
            HTreeMap<String, Annuncio> mappaAnnunci = db.hashMap("annunci")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
            
            if (!mappaAnnunci.containsKey(annuncioModificato.getId())) {
                return false;
            }
            
            Annuncio annuncioSicuro = mappaAnnunci.get(annuncioModificato.getId());
            
            // SICUREZZA: Blocchiamo la modifica se non sei l'autore originale
            if (!annuncioSicuro.getAutoreUsername().equals(annuncioModificato.getAutoreUsername())) {
                return false; 
            }
            
            // Aggiorniamo solo i campi modificabili, ID e Autore restano protetti
            annuncioSicuro.setTitolo(annuncioModificato.getTitolo());
            annuncioSicuro.setDescrizione(annuncioModificato.getDescrizione());
            annuncioSicuro.setCompetenzaOfferta(annuncioModificato.getCompetenzaOfferta());
            annuncioSicuro.setCompetenzaRichiesta(annuncioModificato.getCompetenzaRichiesta());
            
            mappaAnnunci.put(annuncioSicuro.getId(), annuncioSicuro);
            DatabaseCore.commit(); 
            return true;

        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

    @Override
    public Boolean eliminaAnnuncio(String id, String username) throws Exception {
        DB db = DatabaseCore.getDB();
        try {
            HTreeMap<String, Annuncio> mappaAnnunci = db.hashMap("annunci")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();
            
            if (!mappaAnnunci.containsKey(id)) {
                return false;
            }
            
            Annuncio annuncioSicuro = mappaAnnunci.get(id);
            
            // SICUREZZA: Blocchiamo l'eliminazione se non sei l'autore originale
            if (!annuncioSicuro.getAutoreUsername().equals(username)) {
                return false; 
            }
            
            mappaAnnunci.remove(id);
            DatabaseCore.commit(); 
            return true;
            
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }
}