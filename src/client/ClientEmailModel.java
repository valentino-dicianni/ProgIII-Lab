package client;


import server.LogInterface;

import javax.swing.*;

import java.net.InetAddress;
import java.rmi.Naming;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

public class ClientEmailModel extends Observable {

	private String nomeAcClient, emailClient;
	private DefaultListModel list = new DefaultListModel();
	private LogInterface server;

	public ClientEmailModel(String nomeAcClient, String emailClient) {
		this.nomeAcClient = nomeAcClient;
		this.emailClient = emailClient;
        String ipServer = JOptionPane.showInputDialog(null,"Inserire indirizzo IP locale del server","192.168.0.x");


        try {
            server = (LogInterface) Naming.lookup("rmi://"+ipServer+":2000/server");
            System.out.print("Client connesso al server");
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
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

	public void showMail(){

		for (int i = 0; i < 15; i++) {
			list.addElement(new Email("Mittente "+i, "Destinatario "+i, "Oggetto "+i, "Testo email"+i, 1, null,false));
		}
		setChanged();
		notifyObservers("updateMailList");
	}
/* TODO: aggiungere theread che in maniera pediodica va a fare la pool dal server centrale e se ci sono nuove mail le aggiunge alla lista e notifica gli osservatori */

}