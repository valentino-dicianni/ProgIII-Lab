package progettoEmail;


import java.util.Observable;


public class ClientEmailModel extends Observable {

	private String nomeAcClient, emailClient;

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
}