package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

@RemoteServiceRelativePath("annunci")
public interface GestoreAnnunci extends RemoteService {
    
    // Metodo per salvare un nuovo annuncio
    Boolean pubblicaAnnuncio(Annuncio annuncio) throws Exception;
    
    // Metodo per recuperare tutti gli annunci pubblicati sulla piattaforma
    List<Annuncio> ottieniTuttiGliAnnunci() throws Exception;
}