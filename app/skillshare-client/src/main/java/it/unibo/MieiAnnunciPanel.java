package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;

public class MieiAnnunciPanel extends Composite {
    
    // Pannelli principali per strutturare la grafica della pagina
    private final VerticalPanel mainPanel = new VerticalPanel();
    private final VerticalPanel listaContainer = new VerticalPanel();
    
    // Memorizziamo l'username dell'utente attualmente loggato
    private final String usernameCorrente;

    // Interfaccia asincrona RPC generata da GWT per comunicare con il backend
    private final GestoreAnnunciAsync gestoreAnnunci = GWT.create(GestoreAnnunci.class);

    // Azione da eseguire per tornare indietro
    private final Runnable onTornaIndietro;

    // Costruttore: riceve l'username dell'utente che ha fatto il login
    public MieiAnnunciPanel(String usernameCorrente, Runnable onTornaIndietro) {
        this.usernameCorrente = usernameCorrente;
        this.onTornaIndietro = onTornaIndietro;

        mainPanel.setSpacing(10);

        // --- PULSANTE TORNA INDIETRO ---
        Button btnIndietro = new Button("← Torna alla Bacheca");
        btnIndietro.addClickHandler(event -> {
            if (this.onTornaIndietro != null) {
                this.onTornaIndietro.run(); // Esegue il ritorno alla schermata precedente
            }
        });
        mainPanel.add(btnIndietro);

        mainPanel.add(new HTML("<h2>I Miei Annunci</h2>"));
        mainPanel.add(new Label("Da qui puoi visualizzare e gestire esclusivamente i post che hai pubblicato."));
        mainPanel.add(listaContainer);

        // Collega il pannello principale al widget GWT
        initWidget(mainPanel);

        // Appena apri la schermata, scarica la lista dei tuoi annunci dal server
        caricaMieiAnnunci();
    }

    // Metodo per chiamare il backend e recuperare solo gli annunci dell'utente
    private void caricaMieiAnnunci() {
        listaContainer.clear();
        listaContainer.add(new Label("Caricamento dei tuoi annunci in corso..."));

        // Chiamata asincrona al metodo che hai creato nel backend
        gestoreAnnunci.ottieniAnnunciUtente(usernameCorrente, new AsyncCallback<List<Annuncio>>() {
            @Override
            public void onFailure(Throwable caught) {
                listaContainer.clear();
                listaContainer.add(new Label("Errore di comunicazione con il server: " + caught.getMessage()));
            }

            @Override
            public void onSuccess(List<Annuncio> annunci) {
                listaContainer.clear();
                
                if (annunci == null || annunci.isEmpty()) {
                    listaContainer.add(new Label("Non hai ancora pubblicato nessun annuncio."));
                    return;
                }

                // Per ogni annuncio trovato, creiamo una "card" grafica con i bottoni
                for (final Annuncio annuncio : annunci) {
                    VerticalPanel card = creaCardAnnuncio(annuncio);
                    listaContainer.add(card);
                }
            }
        });
    }

    // Disegna visivamente la scheda (card) per ogni singolo annuncio
    private VerticalPanel creaCardAnnuncio(final Annuncio annuncio) {
        VerticalPanel card = new VerticalPanel();
        card.setStyleName("annuncio-card");
        card.setSpacing(5);

        card.add(new HTML("<b>Titolo:</b> " + annuncio.getTitolo()));
        card.add(new HTML("<b>Descrizione:</b> " + annuncio.getDescrizione()));
        card.add(new HTML("<b>Offre:</b> " + annuncio.getCompetenzaOfferta() + " | <b>Richiede:</b> " + annuncio.getCompetenzaRichiesta()));

        HorizontalPanel btnPanel = new HorizontalPanel();
        btnPanel.setSpacing(5);

        // Bottone Modifica con ID univoco basato sull'ID dell'annuncio
        Button btnModifica = new Button("Modifica", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                mostraDialogoModifica(annuncio);
            }
        });
        btnModifica.getElement().setId("btn-modifica-" + annuncio.getId());

        // Pulsante Elimina con ID univoco basato sull'ID dell'annuncio
        Button btnElimina = new Button("Elimina");
        btnElimina.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Prima di eliminare, apriamo il popup di conferma "Sei sicuro?"
                mostraDialogoConfermaEliminazione(annuncio.getId());
            }
        });
        btnElimina.getElement().setId("btn-elimina-" + annuncio.getId());

        btnPanel.add(btnModifica);
        btnPanel.add(btnElimina);

        card.add(btnPanel);
        card.add(new HTML("<hr/>"));

        return card;
    }

    // Apre un popup modale (DialogBox) pulito e nativo GWT con i bottoni Sì / No
    private void mostraDialogoConfermaEliminazione(final String idAnnuncio) {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Conferma operazione");
        dialogBox.setAnimationEnabled(true);
        dialogBox.setGlassEnabled(true); // Scura lo sfondo dietro la finestra

        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setSpacing(15);
        dialogContents.add(new Label("Sei sicuro di voler eliminare questo annuncio?"));

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(10);

        // Bottone Sì: procede con l'eliminazione
        Button btnSi = new Button("Sì", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                eseguiEliminazioneServer(idAnnuncio);
            }
        });
        // Assegniamo un ID chiaro al bottone "Sì" della modale di conferma
        btnSi.getElement().setId("btn-conferma-elimina-si");

        // Bottone No: chiude semplicemente il popup senza fare nulla
        Button btnNo = new Button("No", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });

        buttonPanel.add(btnSi);
        buttonPanel.add(btnNo);
        dialogContents.add(buttonPanel);
        
        dialogBox.setWidget(dialogContents);
        dialogBox.center(); // Centra perfettamente il popup nella schermata del browser
    }

    // Invia la richiesta di eliminazione al server passando l'ID e l'utente richiedente (Controllo Zero Trust)
    private void eseguiEliminazioneServer(String idAnnuncio) {
        gestoreAnnunci.eliminaAnnuncio(idAnnuncio, usernameCorrente, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Errore di rete durante l'eliminazione.");
            }

            @Override
            public void onSuccess(Boolean risultato) {
                if (risultato != null && risultato) {
                    Window.alert("Annuncio eliminato con successo!");
                    // Ricarica la lista per aggiornare subito la schermata rimuovendo il post
                    caricaMieiAnnunci();
                } else {
                    Window.alert("Operazione negata: non sei il proprietario di questo annuncio.");
                }
            }
        });
    }

    private void mostraDialogoModifica(final Annuncio annuncio) {
        // Creazione di una DialogBox nativa GWT per contenere il form di modifica
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Modifica Annuncio");
        dialogBox.setAnimationEnabled(true);
        dialogBox.setGlassEnabled(true); // Scura lo sfondo della pagina per dare effetto modale

        // Pannello verticale principale dentro il popup
        VerticalPanel panel = new VerticalPanel();
        panel.setSpacing(10);

        // Campo di testo per il titolo, precompilato con il valore attuale dell'annuncio
        final TextBox txtTitolo = new TextBox();
        txtTitolo.setText(annuncio.getTitolo());
        
        // Area di testo per la descrizione, precompilata con il valore attuale
        final TextArea txtDescrizione = new TextArea();
        txtDescrizione.setText(annuncio.getDescrizione());
        txtDescrizione.setVisibleLines(3);

        // Campo di testo per la competenza offerta, precompilato
        final TextBox txtOfferta = new TextBox();
        txtOfferta.setText(annuncio.getCompetenzaOfferta());

        // Campo di testo per la competenza richiesta, precompilato
        final TextBox txtRichiesta = new TextBox();
        txtRichiesta.setText(annuncio.getCompetenzaRichiesta());

        // Aggiungiamo etichette e campi di input al pannello del form
        panel.add(new Label("Titolo:"));
        panel.add(txtTitolo);
        panel.add(new Label("Descrizione:"));
        panel.add(txtDescrizione);
        panel.add(new Label("Offre:"));
        panel.add(txtOfferta);
        panel.add(new Label("Richiede:"));
        panel.add(txtRichiesta);

        // Pannello orizzontale per i pulsanti di azione in basso
        HorizontalPanel btnPanel = new HorizontalPanel();
        btnPanel.setSpacing(10);

        // Pulsante per confermare e salvare le modifiche apportate
        Button btnSalva = new Button("Salva Modifiche", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Aggiorniamo l'oggetto annuncio con i nuovi testi scritti dall'utente
                annuncio.setTitolo(txtTitolo.getText());
                annuncio.setDescrizione(txtDescrizione.getText());
                annuncio.setCompetenzaOfferta(txtOfferta.getText());
                annuncio.setCompetenzaRichiesta(txtRichiesta.getText());

                // Chiudiamo il popup di modifica e avviamo la chiamata al server per salvare i dati modificati
                dialogBox.hide();
                eseguiModificaServer(annuncio);
            }
        });

        // Pulsante per annullare l'operazione e chiudere il popup senza salvare
        Button btnAnnulla = new Button("Annulla", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });

        // Inseriamo i bottoni nel loro sotto-pannello e il sotto-pannello nel form principale
        btnPanel.add(btnSalva);
        btnPanel.add(btnAnnulla);
        panel.add(btnPanel);

        // Assegniamo gli ID ai campi del popup per renderli facilmente testabili con Selenium
        txtTitolo.getElement().setId("modifica-titolo");
        txtDescrizione.getElement().setId("modifica-desc");
        txtOfferta.getElement().setId("modifica-offerta");
        txtRichiesta.getElement().setId("modifica-richiesta");
        btnSalva.getElement().setId("modifica-submit");

        // Impostiamo il pannello come contenuto del DialogBox e lo centriamo a schermo
        dialogBox.setWidget(panel);
        dialogBox.center();
    }

    // Metodo per inviare l'annuncio modificato al backend tramite RPC asincrona
    private void eseguiModificaServer(Annuncio annuncioAggiornato) {
        gestoreAnnunci.modificaAnnuncio(annuncioAggiornato, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                // Gestione dell'errore di rete o di comunicazione
                Window.alert("Errore di rete durante la modifica.");
            }

            @Override
            public void onSuccess(Boolean risultato) {
                // Controllo se il server ha completato con successo l'operazione
                if (risultato != null && risultato) {
                    Window.alert("Annuncio modificato con successo!");
                    // Ricarichiamo la lista dei nostri annunci per mostrare i dati aggiornati a schermo
                    caricaMieiAnnunci();
                } else {
                    Window.alert("Modifica non riuscita.");
                }
            }
        });
    }
}
