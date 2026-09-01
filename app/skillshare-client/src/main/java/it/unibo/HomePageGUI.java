package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DatePicker;

import java.util.Date;

public class HomePageGUI {

    private final GestoreAutenticazioneAsync rpcService = GWT.create(GestoreAutenticazione.class);
    private static final String COLORE_ERRORE = "2px solid #e53935";

    private void evidenzia(Widget w, boolean valido) {
        if (valido) {
            w.getElement().getStyle().clearProperty("border");
        } else {
            w.getElement().getStyle().setProperty("border", COLORE_ERRORE);
        }
    }

    private void mostraErrore(Label lbl, String testo) {
        lbl.getElement().getStyle().setColor("red");
        lbl.setText(testo);
    }

    private void mostraSuccesso(Label lbl, String testo) {
        lbl.getElement().getStyle().setColor("green");
        lbl.setText(testo);
    }

    public void mostra() {
        VerticalPanel panel = new VerticalPanel();
        panel.setSpacing(10);

        panel.add(new HTML("<h1 style=\"background-color:rgb(0,100,100);color:rgb(0,0,255);\"> <em>Benvenuto in SkillShare!   </em></h1> <p>Compila i campi sotto per registrarti</p>\n"));

        // ==================== REGISTRAZIONE ====================

        // NOME
        TextBox txtNome = new TextBox();
        txtNome.getElement().setPropertyString("placeholder", "Nome");
        txtNome.getElement().setId("reg-nome");
        Label errNome = new Label();
        errNome.getElement().getStyle().setColor("red");
        HorizontalPanel rowNome = new HorizontalPanel();
        rowNome.setSpacing(5);
        rowNome.add(txtNome);
        rowNome.add(errNome);

        // COGNOME
        TextBox txtCognome = new TextBox();
        txtCognome.getElement().setPropertyString("placeholder", "Cognome");
        txtCognome.getElement().setId("reg-cognome");
        Label errCognome = new Label();
        HorizontalPanel rowCognome = new HorizontalPanel();
        rowCognome.setSpacing(5);
        rowCognome.add(txtCognome);

        // USERNAME
        TextBox txtUser = new TextBox();
        txtUser.getElement().setPropertyString("placeholder", "Username");
        txtUser.getElement().setId("reg-username");
        Label errUser = new Label();
        errUser.getElement().getStyle().setColor("red");
        HorizontalPanel rowUser = new HorizontalPanel();
        rowUser.setSpacing(5);
        rowUser.add(txtUser);
        rowUser.add(errUser);

        // EMAIL
        TextBox txtMail = new TextBox();
        txtMail.getElement().setPropertyString("placeholder", "Email");
        txtMail.getElement().setId("reg-mail");
        Label errMail = new Label();
        errMail.getElement().getStyle().setColor("red");
        HorizontalPanel rowMail = new HorizontalPanel();
        rowMail.setSpacing(5);
        rowMail.add(txtMail);
        rowMail.add(errMail);

        // DATA DI NASCITA
        DatePicker datePicker = new DatePicker();
        datePicker.setYearArrowsVisible(true);
        DateBox dateNascitaBox = new DateBox(
                datePicker,
                null,
                new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy"))
        );
        dateNascitaBox.getElement().setPropertyString("placeholder", "Data di nascita (gg/mm/aaaa)");
        dateNascitaBox.getElement().setId("reg-data");
        Label errData = new Label();
        errData.getElement().getStyle().setColor("red");
        HorizontalPanel rowData = new HorizontalPanel();
        rowData.setSpacing(5);
        rowData.add(dateNascitaBox);
        rowData.add(errData);

        // PASSWORD
        PasswordTextBox txtPass = new PasswordTextBox();
        txtPass.getElement().setPropertyString("placeholder", "Password");
        txtPass.getElement().setId("reg-password");
        Label errPass = new Label();
        errPass.getElement().getStyle().setColor("red");
        HorizontalPanel rowPass = new HorizontalPanel();
        rowPass.setSpacing(5);
        rowPass.add(txtPass);
        rowPass.add(errPass);

        Button btnRegistrati = new Button("Registrati");
        btnRegistrati.getElement().setId("reg-submit");

        Label lblMessaggio = new Label();
        lblMessaggio.getElement().setId("reg-message");

        btnRegistrati.addClickHandler(event -> {
            // Reset dei messaggi di errore
            errNome.setText("");
            errCognome.setText("");
            errUser.setText("");
            errMail.setText("");
            errData.setText("");
            errPass.setText("");
            lblMessaggio.setText("");

            // Validazione lato client
            boolean nomeOk = FieldVerifier.isValidName(txtNome.getText());
            boolean cognomeOk=FieldVerifier.isValidName(txtCognome.getText());
            boolean userOk = FieldVerifier.isValidUsername(txtUser.getText());
            boolean mailOk = FieldVerifier.isValidEmail(txtMail.getText());
            boolean passOk = FieldVerifier.isValidPassword(txtPass.getText());
            Date dataNascita = dateNascitaBox.getValue();
            boolean dataOk = FieldVerifier.isValidDataNascita(dataNascita);

            evidenzia(txtNome, nomeOk);
            if (!nomeOk) errNome.setText("Nome non valido");

            evidenzia(txtCognome,cognomeOk);
            if(!cognomeOk) errCognome.setText("Cognome non valido");

            evidenzia(txtUser, userOk);
            if (!userOk) errUser.setText("Username non valido");

            evidenzia(txtMail, mailOk);
            if (!mailOk) errMail.setText("Email non valida");

            evidenzia(dateNascitaBox, dataOk);
            if (!dataOk) errData.setText("Data non valida");

            evidenzia(txtPass, passOk);
            if (!passOk) errPass.setText("Password non valida");

            if (!nomeOk || !cognomeOk || !userOk || !mailOk || !dataOk || !passOk) {
                mostraErrore(lblMessaggio, "Controlla gli errori segnalati accanto ai campi e riprova.");
                return;
            }

            // Creazione utente e chiamata al server
            Utente u = new Utente();
            u.setNome(txtNome.getText());
            u.setCognome(txtCognome.getText());
            u.setUsername(txtUser.getText());
            u.setMail(txtMail.getText());
            u.setPassword(txtPass.getText());
            u.setData(dataNascita);

            rpcService.registraUtente(u, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    String msg = caught.getMessage() == null ? "" : caught.getMessage();

                    // il vincolo di unicità ora è sull'email
                    if (msg.contains("già registrato")) {
                        evidenzia(txtMail, false);
                        errMail.setText("Email già registrata, effettua il login.");
                        mostraErrore(lblMessaggio, "Errore: questa email è già registrata.");
                        return;
                    }

                    if (caught instanceof IllegalArgumentException) {
                        // Smista l'errore del server nei rispettivi campi
                        boolean isNomeErr = msg.contains("Nome");
                        boolean isCognomeERr = msg.contains("Cognome");
                        boolean isUserErr = msg.contains("Username");
                        boolean isMailErr = msg.contains("mail");
                        boolean isDataErr = msg.contains("Data di nascita");
                        boolean isPassErr = msg.contains("Password");

                        evidenzia(txtNome, !isNomeErr);
                        if (isNomeErr) errNome.setText("Rifiutato dal server");

                        evidenzia(txtCognome, !isCognomeERr);
                        if (isCognomeERr) errCognome.setText("Rifiutato dal server");   // era isNomeErr, corretto

                        evidenzia(txtUser, !isUserErr);
                        if (isUserErr) errUser.setText("Rifiutato dal server");

                        evidenzia(txtMail, !isMailErr);
                        if (isMailErr) errMail.setText("Rifiutato dal server");

                        evidenzia(dateNascitaBox, !isDataErr);
                        if (isDataErr) errData.setText("Rifiutata dal server");

                        evidenzia(txtPass, !isPassErr);
                        if (isPassErr) errPass.setText("Rifiutata dal server");

                        mostraErrore(lblMessaggio, "Errore di validazione dal server. " + msg);
                        return;
                    }

                    mostraErrore(lblMessaggio, "Errore di comunicazione col server: " + msg);
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (Boolean.TRUE.equals(result)) {
                        mostraSuccesso(lblMessaggio, "Registrazione completata con successo!");

                        // Svuota il contenuto
                        txtNome.setText("");
                        txtCognome.setText("");
                        txtUser.setText("");
                        txtMail.setText("");
                        dateNascitaBox.setValue(null);
                        txtPass.setText("");

                        // I bordi tornano normali
                        evidenzia(txtNome, true);
                        evidenzia(txtCognome, true);
                        evidenzia(txtUser, true);
                        evidenzia(txtMail, true);
                        evidenzia(dateNascitaBox, true);
                        evidenzia(txtPass, true);

                        // Pulisce i messaggi di errore
                        errNome.setText("");
                        errUser.setText("");
                        errMail.setText("");
                        errData.setText("");
                        errPass.setText("");
                    }
                }
            });
        });

        // Aggiunta righe al pannello
        panel.add(rowNome);
        panel.add(rowCognome);
        panel.add(rowUser);
        panel.add(rowMail);
        panel.add(rowData);
        panel.add(rowPass);
        panel.add(btnRegistrati);
        panel.add(lblMessaggio);

        // ==================== LOGIN ====================
        panel.add(new HTML("<h3>Accedi</h3>"));

        TextBox txtLoginMail = new TextBox();
        txtLoginMail.getElement().setPropertyString("placeholder", "Email");
        txtLoginMail.getElement().setId("login-mail");
        Label errLoginMail = new Label();
        errLoginMail.getElement().getStyle().setColor("red");
        errLoginMail.getElement().setId("login-mail-error");
        HorizontalPanel rowLoginMail = new HorizontalPanel();
        rowLoginMail.setSpacing(5);
        rowLoginMail.add(txtLoginMail);
        rowLoginMail.add(errLoginMail);

        PasswordTextBox txtLoginPassword = new PasswordTextBox();
        txtLoginPassword.getElement().setPropertyString("placeholder", "Password");
        txtLoginPassword.getElement().setId("login-password");
        Label errLoginPassword = new Label();
        errLoginPassword.getElement().getStyle().setColor("red");
        errLoginPassword.getElement().setId("login-password-error");
        HorizontalPanel rowLoginPassword = new HorizontalPanel();
        rowLoginPassword.setSpacing(5);
        rowLoginPassword.add(txtLoginPassword);
        rowLoginPassword.add(errLoginPassword);

        Button btnLogin = new Button("Accedi");
        btnLogin.getElement().setId("login-submit");

        Label lblLoginMessaggio = new Label();
        lblLoginMessaggio.getElement().setId("login-message");

        btnLogin.addClickHandler(event -> {
            errLoginMail.setText("");
            errLoginPassword.setText("");
            lblLoginMessaggio.setText("");

            boolean mailOk = FieldVerifier.isValidEmail(txtLoginMail.getText());
            boolean passOk = !txtLoginPassword.getText().isEmpty();

            evidenzia(txtLoginMail, mailOk);
            if (!mailOk) errLoginMail.setText("Email non valida");

            evidenzia(txtLoginPassword, passOk);
            if (!passOk) errLoginPassword.setText("Password obbligatoria");

            if (!mailOk || !passOk) {
                mostraErrore(lblLoginMessaggio, "Controlla gli errori segnalati accanto ai campi e riprova.");
                return;
            }

            rpcService.effettuaLogin(txtLoginMail.getText(), txtLoginPassword.getText(), new AsyncCallback<Utente>() {
                @Override
                public void onFailure(Throwable caught) {
                    if (caught instanceof IllegalArgumentException) {
                        evidenzia(txtLoginMail, false);
                        errLoginMail.setText("Rifiutata dal server");
                        mostraErrore(lblLoginMessaggio, "Errore: " + caught.getMessage());
                        return;
                    }
                    mostraErrore(lblLoginMessaggio, "Errore di comunicazione col server: " + caught.getMessage());
                }

                @Override
                public void onSuccess(Utente utente) {
                    if (utente != null) {
                        mostraSuccesso(lblLoginMessaggio, "Benvenuto/a, " + utente.getNome() + "!");
                    } else {
                        evidenzia(txtLoginMail, false);
                        evidenzia(txtLoginPassword, false);
                        mostraErrore(lblLoginMessaggio, "Credenziali errate, riprova.");
                    }
                }
            });
        });

        panel.add(rowLoginMail);
        panel.add(rowLoginPassword);
        panel.add(btnLogin);
        panel.add(lblLoginMessaggio);

        // Stampo a schermo
        RootPanel.get().clear();
        RootPanel.get().add(panel);
    }
}