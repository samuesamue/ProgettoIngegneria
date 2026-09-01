package it.unibo;
//classe proxy che si occupa del controllo dei valori inseriti
public class AutenticatoreProxy implements GestoreAutenticazione{
    private final GestoreAutenticazione autenticatoreReale;
    public AutenticatoreProxy(GestoreAutenticazione autenticatoreReale) {
        this.autenticatoreReale = autenticatoreReale;
    }

    @Override
    public Boolean registraUtente(Utente utente) throws Exception {
        // operazione di controllo dei valori dell'utente Mediante FieldVerifier
        //I criteri di controllo sono stati scelti arbitrariamente e potrebbero essere modificati in seguito

        if(!FieldVerifier.isValidName(utente.getNome()))
            throw new IllegalArgumentException("Nome non valido, inserisci almeno 3 caratteri alfabetici");

        if(!FieldVerifier.isValidUsername(utente.getUsername()))
            throw new IllegalArgumentException("Username non valido, inserisci almeno 3 caratteri alfabetici. Non inserire più di 25 caratteri");

        if(!FieldVerifier.isValidEmail(utente.getMail())) {
            System.out.println("mail fallace: "+ utente.getMail());
            throw new IllegalArgumentException("mail non valida. Assicurati di avere scritto @");
        }

        if(!FieldVerifier.isValidPassword(utente.getPassword()))
            throw new IllegalArgumentException("Password non valida. Assicurati di reimpostarla per potere accedere");

        if(!FieldVerifier.isValidDataNascita(utente.getData()))
            throw new IllegalArgumentException("Data di nascita non valida");

        
        // Se tutto va bene, il Proxy lo passa al Autenticatore Reale
        return autenticatoreReale.registraUtente(utente);
    }
//TO DO
    @Override
    public Utente effettuaLogin(String mail, String password) throws IllegalArgumentException {
        if(!FieldVerifier.isValidEmail(mail)){
            throw new IllegalArgumentException("mail non valida. Assicurati di avere scritto @");
        }
        return autenticatoreReale.effettuaLogin(mail, password);
    }
}
