package client;

import commonResources.Email;
import commonResources.ServerInterface;
import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;


public class ClientEmailModel extends Observable {

    private String nomeAcClient, emailClient, ipServer;
    private DefaultListModel mailList = new DefaultListModel();
    private ServerInterface server;
    private RefreshMailThread refreshThread = new RefreshMailThread(this);

    public ClientEmailModel(String nomeAcClient, String emailClient, String ipServer) {
        this.nomeAcClient = nomeAcClient;
        this.emailClient = emailClient;
        this.ipServer = ipServer;

        try {
            server = (ServerInterface) Naming.lookup("rmi://" + ipServer + ":2000/server");
            server.appendToLog("Client " + nomeAcClient + " connesso");
        } catch (Exception e) {
            System.out.println("Impossibile trovare il server\n" + e.getMessage());
            return;
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
     * Metodo di set dell'email come letta, notifica apertura email agli observers
     */
    public void openEmail(Email selectedEmail) {
        selectedEmail.setRead(true);
        try {
            server.setReadMail(emailClient, selectedEmail);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers(selectedEmail);
    }

    /**
     * Metodo per notificare richiesta di caricamento form di creazione email
     */
    public void showNewEmailForm() {
        setChanged();
        notifyObservers("newEmailForm");
    }

    /**
     * Metodo che restituisce il numero di messaggi ancora da leggere
     */
    public int getNonLetti() {
        int num = 0;
        try {
            num = server.getInfoLetture(emailClient);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * Metodo che inizializza la casella mail all'apertura
     */
    public void showMail() {
        ArrayList<Email> serverList = new ArrayList();
        try {
            serverList = server.getEmail(emailClient);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (Email mail : serverList) {
            mailList.addElement(mail);
        }
        setChanged();
        notifyObservers("updateMailList");
    }

    /**
     * Metodo che preleva informazioni riguardanti la email da inoltrare o a cui
     * rispondere
     */
    public void getSelectedEmailData(Email openedEmail, String action) {
        ArrayList a = new ArrayList();
        a.add(action);
        a.add(openedEmail.getMittEmail());
        a.add(openedEmail.getDestEmail());
        a.add(openedEmail.getArgEmail());
        a.add(openedEmail.getTestoEmail());
        a.add(openedEmail.getDest());
        setChanged();
        notifyObservers(a);
    }

    /**
     * Metodo che elimina la mail selezionata dalla lista delle mail
     */
    public void deleteMail(Email mail) {
        mailList.removeElement(mail);
        try {
            server.deleteEmail(emailClient, mail);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("deleteCompleted");
    }

    /**
     * Metodo che chiama rmi sul server e invia uno o più oggetti serializable Email al server.
     * In caso di errore avvisa l'utente tramite la variabile success.
     */
    public boolean sendEmail(ArrayList<String> toFieldText, String subjectFieldText, String contentFieldText) {
        boolean success = false;
        try {
            String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Date date = new Date();
            String newTExtField = contentFieldText.replace("\n", "§");

            if (!toFieldText.isEmpty()) {
                for (String destinatario : toFieldText) {
                    if (!destinatario.matches(emailPattern)) {
                        return success;
                    }
                    success = server.inviaMail(new Email(emailClient, destinatario, toFieldText, subjectFieldText, newTExtField, 1, date, false));
                }
            }
            if (success) {
                //Nota: non viene stampato nessun messaggio sul log del server in quanto l'indirizzo mail non è ben formato e
                //il controllo viene bloccato lato client, senza interrogare il server.
                System.out.println("Email inviata con successo al server...");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * Metodo che al momento della chiusura del client mail
     * notifica al server l'avvenuta chiusura.
     */
    public void closeOperation() {
        try {
            server.appendToLog("Client disconnesso");
            server.writeFile();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } finally {
            System.exit(0);
        }
    }


    /**
     * Thread che in maniera periodica va a fare la pool dall mail box del server e restituisce eventuali
     * nuovi messaggi per l'utente specifico.
     *
     * @pre: - clientList possiede tutte le mail vecchie (oppure nessuna quando si apre la casella)
     * - serverList possiede tutte le mail vecchie più quelle nuove
     * @post: dopo aver fatto il merge delle due liste, vengono aggiunte le mail mancanti nella lista di clientEmail.
     */
    class RefreshMailThread implements Runnable {
        private ClientEmailModel model;

        public RefreshMailThread(ClientEmailModel model) {
            this.model = model;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " di " + model.getEmailClient());
            while (true) {
                try {
                    Thread.sleep(2000);
                    ArrayList serverList = model.getServer().getEmail(model.getEmailClient());
                    DefaultListModel clientList = model.getMailList();
                    Object[] arr = clientList.toArray();

                    if (serverList != null) {
                        for (int j = arr.length - 1; j >= 0; j--) {
                            serverList.remove(arr[j]);
                        }
                        for (Object mergeElem : serverList) {
                            clientList.add(0, mergeElem);
                        }
                        if (serverList.size() > 0) {
                            model.getServer().appendToLog("Ci sono nuove mail disponibili nella casella del client: " + model.getNomeAcClient());
                            model.setChanged();
                            model.notifyObservers("newEmailReceived");
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}