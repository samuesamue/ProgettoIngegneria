package it.unibo;
    // Il Soggetto Reale che fa il lavoro vero sul DB
    public class AutenticatoreReale  implements GestoreAutenticazione {
        //con tutti i dati apposto avvengono le delegazioni al database per l registrazione e il login
        @Override
        public Boolean registraUtente(Utente utente) throws Exception {
            // Delega diretta al DB
            return DatabaseUtente.registraUtente(utente);
        }

        @Override
        public Utente effettuaLogin(String mail, String password) {
            // Delega diretta al DB
            return DatabaseUtente.login(mail, password);
        }
    }

