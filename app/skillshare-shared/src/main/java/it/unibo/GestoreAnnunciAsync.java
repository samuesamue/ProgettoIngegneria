package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface GestoreAnnunciAsync {
    
    void pubblicaAnnuncio(Annuncio annuncio, AsyncCallback<Boolean> callback);
    
    void ottieniTuttiGliAnnunci(AsyncCallback<List<Annuncio>> callback);
}