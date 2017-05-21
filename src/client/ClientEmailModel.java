package client;

import commonResources.Email;
import commonResources.ServerInterface;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;


public class ClientEmailModel extends Observable {

    private String nomeAcClient, emailClient;
    private DefaultListModel mailList = new DefaultListModel();
    private ServerInterface server;
	private String ipServer;
	private RefreshMailThread refreshThread = new RefreshMailThread(this);


	public ClientEmailModel(String nomeAcClient, String emailClient) {
		this.nomeAcClient = nomeAcClient;
		this.emailClient = emailClient;
        this.ipServer = JOptionPane.showInputDialog(null,"Inserire indirizzo IP locale del server","localhost");

        try {
            server = (ServerInterface) Naming.lookup("rmi://"+ipServer+":2000/server");
            System.out.println("Client connesso al server");
            server.appendToLog("Client " + nomeAcClient +" connesso");


        }
        catch(Exception e) {
            System.out.println("Failed to find distributor " + e.getMessage());
        }
        //start refresh list thread
        new Thread(refreshThread).start();
    }

	public String getNomeAcClient() {
		return nomeAcClient;
	}

	public String getEmailClient() {
		return emailClient;
	}

    public ServerInterface getServer() {
        return server;
    }

    public DefaultListModel getMailList() {
        return mailList;
    }


    public String toString() {
		return ("Modello della Mail di " + nomeAcClient);

	}

	/**
     *  metodo per set email come letta, notifica agli observers
     */

	public void openEmail(Email selectedEmail) {
		selectedEmail.setRead(true);
		setChanged();
		notifyObservers(selectedEmail);
	}

	/**
     *  metodo per notificare richiesta di caricamento form di creazione email
     */
	public void showNewEmailForm(){
		setChanged();
		notifyObservers("newEmailForm");
	}


	/**
	 * Metodo che inizializza la casella mail all'apertura
	 * TODO aggiungere pull dal mail server delle ultime 15 mail
	 */

	public void showMail(){
		for (int i = 0; i < 15; i++) {
			mailList.addElement(new Email("Mittente "+i, "Destinatario "+i, "Oggetto "+i, "Testo email"+i, 1, null,false));
		}
		setChanged();
		notifyObservers("updateMailList");
	}

    /**
     * metodo che elimina la mail selezionata dalla lista delle mail*/
	public void deleteClientMail(Email mail) throws RemoteException {
	    mailList.removeElement(mail);
	    server.deleteEmail(emailClient,mail);
	    //forse setCanged() e notifyObservers() ????
    }

    /**
     * Metodo che chiama rmi sul server e invia un oggetto serializable Email al server
     */
    public void sendEmail(String toFieldText, String subjectFieldText, String contentFieldText) throws RemoteException {
        server.inviaMail(new Email(emailClient,toFieldText,subjectFieldText,contentFieldText,1,null,false));
        System.out.println("Email inviata con successo al server...");
    }


    /**
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





}
/**
 * Thread che in maniera periodica va a fare la pool dall mail box del server e ritorna eventuali
 * nuovi messaggi per l'utente specifico.
 * @pre: - clientList possiede tutte le mail vecchie (oppure nessuna quando si apre la casella)
 *       - serverList possiede tutte le mail vecchie più quelle nuove
 * @post: dopo aver fatto il merge delle due liste, vengono aggiunte le mail mancanti nella lista di clientEmail.
 */

class RefreshMailThread implements Runnable {
    private ClientEmailModel model;

    public RefreshMailThread(ClientEmailModel model){this.model=model;}

    public void run() {
        System.out.println(Thread.currentThread().getName() + " di " + model.getEmailClient());
        while(true){
            try {
                Thread.sleep(2000);

                ArrayList serverList = model.getServer().getEmail(model.getEmailClient());
                DefaultListModel clientList = model.getMailList();
                Object[] arr=clientList.toArray();

                if(serverList != null){//da controllare se funziona(in teoria si una volta che prende la vera lista mail)
                    for (int j = arr.length-1; j >= 0; j--) {
                        serverList.remove(arr[j]);
                    }

                    for (Object mergeElem : serverList) {
                        clientList.add(0,mergeElem);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}