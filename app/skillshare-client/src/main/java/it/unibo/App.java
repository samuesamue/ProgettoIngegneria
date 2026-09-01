package it.unibo;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint {
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		//CAMPI DI PROVA
		Utente utenteFinto = new Utente("Ludovica", "Govoni");
		utenteFinto.setMail("ludovica.govoni@gmail.com");
		utenteFinto.setUsername("govonsx");
		utenteFinto.setBio("Sto creando la mia prima interfaccia in GWT!");
		utenteFinto.setTagCompetenze("GWT, Java, Frontend");


		//new RegistrazioneGui().mostra();
		//new PersonalizzaProfiloGui().mostra(utenteFinto);
		new PubblicaAnnunciGui().mostra(utenteFinto);

	}
}
