package client;

import server.LogInterface;
import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Observable;


public class ClientEmailModel extends Observable {

	private LogInterface server;
	private String nomeAcClient, emailClient;
	private DefaultListModel list = new DefaultListModel();
	private String ipServer;

	public ClientEmailModel(String nomeAcClient, String emailClient) {
		this.nomeAcClient = nomeAcClient;
		this.emailClient = emailClient;
        this.ipServer = JOptionPane.showInputDialog(null,"Inserire indirizzo IP locale del server","192.168.0.x");

        try {
            server = (LogInterface) Naming.lookup("rmi://"+ipServer+":2000/server");
            System.out.print("Client connesso al server");
            server.appendToLog("Client connesso");

        }
        catch(Exception e) {
            System.out.println("Failed to find distributor" + e.getMessage());
        }
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


	/*
	* Metodo che inizializza la casella mail all'apertura
	* TODO aggiungere pull dal mail server delle ultime 15 mail
	*/

	public void showMail(){
		for (int i = 0; i < 15; i++) {
			list.addElement(new Email("Mittente "+i, "Destinatario "+i, "Oggetto "+i, "Testo email"+i, 1, null,false));
		}
		setChanged();
		notifyObservers("updateMailList");
	}



/* TODO: aggiungere theread che in maniera pediodica va a fare la pool dal server centrale e se ci sono nuove mail le aggiunge alla lista e notifica gli osservatori */

    /*
    * Metodo che al momento della chiusura del client mail
    * notifica al server l'avvenuta chiusura.
    */
	public void closeOperation(){
		try {
			server.appendToLog("Client disconnesso");

		} catch (RemoteException e1) {
			e1.printStackTrace();
		}finally {
			System.exit(0);

		}
	}

	public void sendEmail(String toFieldText, String subjectFieldText, String contentFieldText) {
		new Email(emailClient,toFieldText,subjectFieldText,contentFieldText,1,null,false);
	}
}