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
		new RegistrazioneGui().mostra();
		//new PersonalizzaProfiloGui().mostra(null);

		 
		//Creiamo un utente finto già loggato per poter testare la pagina
		Utente utenteFinto = new Utente("Ludovica", "Govoni");
		utenteFinto.setMail("ludovica.govoni@gmail.com");
		utenteFinto.setBio("Sto creando la mia prima interfaccia in GWT!");
		utenteFinto.setTagCompetenze("GWT, Java, Frontend");

		//Mostriamo la pagina passandole l'utente finto
		new PersonalizzaProfiloGui().mostra(utenteFinto);
	}
}
