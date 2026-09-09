package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import java.util.List;

public class BachecaProposteGUI {
    private final GestoreAnnunciAsync rpcService = GWT.create(GestoreAnnunci.class);

    private final GestoreRichiesteServiceAsync richiesteService = GWT.create(GestoreRichiesteService.class);

    public void mostra(Utente utenteLoggato) {
        VerticalPanel pannelloPrincipale = new VerticalPanel();
        pannelloPrincipale.setSpacing(15);

        pannelloPrincipale.add(new HTML("<h2 style='color: #006464; border-bottom: 2px solid #006464; padding-bottom: 5px; font-family: sans-serif;'>Lista Proposte Disponibili</h2>"));

        Label lblStato = new Label("Carimento delle proposte in corso...");
        pannelloPrincipale.add(lblStato);

        VerticalPanel pannelloAnnunci = new VerticalPanel();
        pannelloPrincipale.add(pannelloAnnunci);

        rpcService.ottieniTuttiGliAnnunci(new AsyncCallback<List<Annuncio>>() {
            
            @Override 
            public void onFailure(Throwable caught){
                lblStato.setText("Errore di comunicazione col server: " + caught.getMessage());
                lblStato.getElement().getStyle().setColor("red");
            }

            @Override 
            public void onSuccess(List<Annuncio> listaProposte) {
                lblStato.setText("");

                // Caso A: Database vuoto, nessun annuncio presente
                if (listaProposte == null || listaProposte.isEmpty()){
                    Label lblVuoto = new Label("Non ci sono proposte disponibili al momento.");
                    lblVuoto.getElement().setId("msg-nessuna-proposta");
                    pannelloAnnunci.add(lblVuoto);
                    return;
                }

                // Caso B: Database popolato, mostriamo gli annunci
                FlexTable tabellaAnnunci = new FlexTable();
                tabellaAnnunci.getElement().setId("tabella-proposte");
                tabellaAnnunci.setWidth("900px");
                tabellaAnnunci.setCellPadding(10);
                tabellaAnnunci.setCellSpacing(0); // Rimuove lo spazio tra le celle
                tabellaAnnunci.getElement().getStyle().setProperty("borderCollapse", "collapse");
                tabellaAnnunci.getElement().getStyle().setProperty("fontFamily", "sans-serif");

                // Intestazioni della tabella
                tabellaAnnunci.setHTML(0, 0, "<b>Titolo</b>");
                tabellaAnnunci.setHTML(0, 1, "<b>Autore</b>");
                tabellaAnnunci.setHTML(0, 2, "<b>Descrizione</b>");
                tabellaAnnunci.setHTML(0, 3, "<b>Competenza Offerta</b>");
                tabellaAnnunci.setHTML(0, 4, "<b>Competenza Richiesta</b>");
                tabellaAnnunci.setHTML(0,5, "<b>Azione</b>");

                // Stile per l'intestazione della tabella
                tabellaAnnunci.getRowFormatter().getElement(0).getStyle().setProperty("backgroundColor","#006464");
                tabellaAnnunci.getRowFormatter().getElement(0).getStyle().setProperty("color","white");
                tabellaAnnunci.getRowFormatter().getElement(0).getStyle().setProperty("textAlign","center");
                tabellaAnnunci.getRowFormatter().getElement(0).getStyle().setProperty("textTransform","uppercase");

                int riga = 1;
                for (Annuncio annuncio : listaProposte) {
                    tabellaAnnunci.setText(riga, 0, annuncio.getTitolo());
                    tabellaAnnunci.setText(riga, 1, annuncio.getAutoreUsername());
                    tabellaAnnunci.setText(riga, 2, annuncio.getDescrizione());
                    tabellaAnnunci.setText(riga, 3, annuncio.getCompetenzaOfferta());
                    tabellaAnnunci.setText(riga, 4, annuncio.getCompetenzaRichiesta());

                    Button btnRichiediScambio = new Button("Richiedi Scambio");

                    // Disabilita il pulsante se l'utente loggato è l'autore dell'annuncio
                    if (annuncio.getAutoreUsername().equals(utenteLoggato.getUsername())) {
                        btnRichiediScambio.setEnabled(false);
                        btnRichiediScambio.setTitle("Non puoi richiedere uno scambio con il tuo annuncio.");
                    }

                    btnRichiediScambio.addClickHandler(event ->{
                        // Creazione della richiesta di scambio
                        RichiestaScambio richiesta = new RichiestaScambio("",annuncio.getId(), utenteLoggato.getUsername(), annuncio.getAutoreUsername());

                        richiesteService.inviaRichiesta(richiesta, new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                Window.alert("Errore di rete: " + caught.getMessage());
                            }

                            @Override
                            public void onSuccess(Boolean successo) {
                                if (successo) {
                                    Window.alert("Richiesta di scambio inviata con successo a " + annuncio.getAutoreUsername() + " per l'annuncio '" + annuncio.getTitolo() + "'.");
                                } else {
                                    Window.alert("Errore: impossibile inviare la richiesta. Azione bloccata dal server.");
                                }
                            }
                        });
                    });

                    // Aggiunta del pulsante alla tabella
                    tabellaAnnunci.setWidget(riga, 5, btnRichiediScambio);

                    // Stile per le celle della tabella
                    tabellaAnnunci.getRowFormatter().getElement(riga).getStyle().setProperty("textAlign","center");

                    // Separazione tra le righe e alternanza dei colori
                    tabellaAnnunci.getRowFormatter().getElement(riga).getStyle().setProperty("borderBottom", "1px solid #dddddd");

                    if (riga % 2 == 0){
                        tabellaAnnunci.getRowFormatter().getElement(riga).getStyle().setProperty("backgroundColor", "#f9f9f9");
                    }

                    riga++;
                }
                    pannelloAnnunci.add(tabellaAnnunci);
            }
        });

        Button btnIndietro = new Button("Indietro");
        btnIndietro.getElement().getStyle().setProperty("marginTop", "20px");
        btnIndietro.addClickHandler(event -> {
            // Logica per tornare alla pagina precedente o alla home page
            new PubblicaAnnunciGui().mostra(utenteLoggato); // Passa l'utente loggato
        });
        pannelloPrincipale.add(btnIndietro);

        RootPanel.get().clear();
        RootPanel.get().add(pannelloPrincipale);
    }
}
