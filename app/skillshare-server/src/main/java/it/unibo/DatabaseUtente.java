package it.unibo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.Serializer;
import org.mapdb.DB;//Database contenente tutti gli utente registrati alla piattaforma
public class DatabaseUtente {
    private static final DB db = DatabaseCore.getDB();
    private static final ConcurrentMap<String, Integer> exampleCollection =
            db.hashMap(
                    "ricetta",        // Nome della collezione
                    Serializer.STRING,   // Tipo della chiave
                    Serializer.INTEGER    // Tipo del valore
            ).createOrOpen();
}
