package it.unibo;

// Classe proxy che si occupa del controllo dei valori del profilo prima di passarli al Reale
public class GestoreProfiloProxy implements GestoreProfilo {
    private final GestoreProfilo gestoreReale = new GestoreProfiloReale();

    @Override
    public Utente aggiornaProfilo(Utente utenteAggiornato) throws Exception {
        if (utenteAggiornato == null || utenteAggiornato.getMail() == null || utenteAggiornato.getMail().isEmpty()) {
            throw new IllegalArgumentException("Errore: email mancante o utente non valido.");
        }

        if(!FieldVerifier.isValidBio(utenteAggiornato.getBio()))
            throw new IllegalArgumentException("Limite di caratteri superato.");

        if(!FieldVerifier.isValidTag(utenteAggiornato.getTagCompetenze()))
            throw new IllegalArgumentException("Inserisci i tag.");

        // Se i controlli passano, delega al gestore reale
        return gestoreReale.aggiornaProfilo(utenteAggiornato);
    }
}