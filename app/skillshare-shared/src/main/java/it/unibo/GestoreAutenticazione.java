package it.unibo;
import com.google.gwt.user.client.rpc.RemoteService;//primo degli elemneti per attuare il pattern proxy
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("autenticazione")
public interface GestoreAutenticazione  extends RemoteService{
    Boolean registraUtente(Utente utente) throws Exception;
    Utente effettuaLogin(String mail, String password) throws IllegalArgumentException;
}