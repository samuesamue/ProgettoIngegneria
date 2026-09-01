package it.unibo;

public class AutenticatoreReale implements GestoreAutenticazione {

    private final GestorePassword passwordManager;

    // Iniezione del gestore password
    public AutenticatoreReale(GestorePassword passwordManager) {
        this.passwordManager = passwordManager;
    }

    @Override
    public Boolean registraUtente(Utente utente) throws Exception {
        // Registra nel database
        return DatabaseUtente.registraUtente(utente);
    }

    @Override
    public Utente effettuaLogin(String mail, String password) {
        // Delega il controllo al PasswordManager
        return passwordManager.effettuaLogin(mail, password);
    }
}