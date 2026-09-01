package it.unibo;

import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

//Soggetto reale che si occupa di salvare effettivamente il profilo sul DB
public class GestoreProfiloReale implements GestoreProfilo {

    @Override
    public Utente aggiornaProfilo(Utente utenteAggiornato) throws Exception {
        DB db = DatabaseCore.getDB();
        
        HTreeMap<String, Utente> mappaUtenti = db.hashMap("utenti")
            .keySerializer(Serializer.STRING)
            .valueSerializer(Serializer.JAVA)
            .createOrOpen();

        // Salvataggio basato sulla mail come chiave
        mappaUtenti.put(utenteAggiornato.getMail(), utenteAggiornato);
        DatabaseCore.commit();

        return utenteAggiornato;
    }
}