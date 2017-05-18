package client;


import javax.swing.*;

import java.util.Observable;

public class ClientEmailModel extends Observable {

	private String nomeAcClient, emailClient;
	private DefaultListModel list = new DefaultListModel();

	public ClientEmailModel(String nomeAcClient, String emailClient) {
		this.nomeAcClient = nomeAcClient;
		this.emailClient = emailClient;
	}

	public String getNomeAcClient() {
		return nomeAcClient;
	}

	public String getEmailClient() {
		return emailClient;
	}



	public String toString() {

		return ("Modello della Mail di " + nomeAcClient);

	}



	/* metodo per set email come letta, notifica agli observers */

	public void openEmail(Email selectedEmail) {

		selectedEmail.setRead(true);

		setChanged();
		notifyObservers(selectedEmail);
	}

	/* metodo per notificare richiesta di caricamento form di creazione email */

	public void showNewEmailForm(){
		setChanged();
		notifyObservers("newEmailForm");
	}

	public DefaultListModel getList() {
		return list;
	}

	public void showMail(){

		for (int i = 0; i < 15; i++) {
			list.addElement(new Email("Mittente "+i, "Destinatario "+i, "Oggetto "+i, "Testo email"+i, 1, null,false));
		}
		setChanged();
		notifyObservers("updateMailList");
	}
/* TODO: aggiungere theread che in maniera pediodica va a fare la pool dal server centrale e se ci sono nuove mail le aggiunge alla lista e notifica gli osservatori */

}