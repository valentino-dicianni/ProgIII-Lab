package server;

import commonResources.Email;
import commonResources.ServerInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.util.Date;


public class ServerEmailModel extends Observable {
    private Log log;
    private HashMap<String, ArrayList<Email>> serverMailList = new HashMap<>();

    /**
     * Classe innestata Log
     */
    public class Log extends UnicastRemoteObject implements ServerInterface {
        private String  nomeLog, testoLog;
        private Date dataCreazione;

        public Log(String nomeLog, String testoLog, Date dataCreazione) throws RemoteException {

            this.nomeLog = nomeLog;
            this.testoLog = testoLog;
            this.dataCreazione = dataCreazione;

            // Set del file delle policy di sicurezza (affinché il Security Manager possa leggerlo)
            String result =  System.setProperty("java.security.policy","file:server.policy");

            // Creazione del SecurityManager, se non esiste già
            try {
                if (System.getSecurityManager() == null) {System.setSecurityManager(new SecurityManager());}
                // Creazione registry che ascolta su una specifica porta
                java.rmi.registry.LocateRegistry.createRegistry(2000);
                // Binding del server su rmiregistry alla porta opportuna
                Naming.rebind("rmi://127.0.0.1:2000/server", this);
            }
            catch (Exception e) {
                System.err.println("Bind a RMI Registry fallito" + e);
                System.exit(1);
            }
            System.out.println("Server attivo...");
        }

        public String getTestoLog() {
            return testoLog;
        }
        public void setTestoLog(String testoLog) {
            this.testoLog = testoLog;
        }
        public Date getDataCreazione() {
            return dataCreazione;
        }
        public void setDataCreazione(Date dataCreazione) {
            this.dataCreazione = dataCreazione;
        }
        public String getNomeLog() {
            return nomeLog;
        }
        public void setNomeLog(String nomeLog) {
            this.nomeLog = nomeLog;
        }

        /**
         * Metodo che controlla quali mail in un determinato indirizzo mail sono state lette
         * e ne ritorna il numero esatto
         */
        @Override
        public int getInfoLetture(String address){
            int num = 0;
            ArrayList<Email> list = serverMailList.get(address);
                for(Email iter : list){
                    if(!iter.isRead()){
                        num++;
                    }
                }
            return num;
        }

        /**
         * Metodo che aggiunge una nuova linea di testo al Log
         * e notifica alla vista il cambiamento effettuato
         */
        @Override
        public void appendToLog(String testoLog){
            setTestoLog(getTestoLog()+"\n"+newMessageLog(testoLog));
            setChanged();
            notifyObservers(this);
        }

        public String newMessageLog(String content){
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); //esempio alternativa: SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            Date date = new Date();
            String newLogMessage;
            String header = null;
            try {
                header = "[ "+dateFormat.format(date)+ " Client IP:"+getClientHost()+"] - ";
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            newLogMessage = header + content;
            return newLogMessage;
        }

        /**
         * Metodo che cancella il Log attuale e notifica alla vista le modfiche
         */
        public void clearLog(){
            setTestoLog("");
            setChanged();
            notifyObservers(this);
        }

        /**
         * Metodo che imposta come letta una determinata mail all'interno del server quando
         * lato client si clicca su di essa
         */
        @Override
        public synchronized void setReadMail(String address, Email mail) throws RemoteException{
            ArrayList<Email> list = serverMailList.get(address);
            if(list.contains(mail)){
                for(Email iter : list){
                    if(iter.equals(mail)){
                        iter.setRead(true);
                    }
                }
            }
            else appendToLog("Errore: la mail "+mail+" non è presente nel database");
        }

        /**
         * Metodo che aggiunge alla lista delle mail di un utente la nuova mail inviata
         * @syncronized per per evitare che che ci sia una lettura mentre avviene la scrittura
         * nel caso in cui non esistelle la casella mail a cui aggiungerla: messaggio di errore
         * @param mail mail da inviare
         */
        @Override
        public synchronized boolean inviaMail(Email mail) throws RemoteException{
            boolean success;
                if (serverMailList.containsKey(mail.getDest())) {
                    mail.setTestoEmail(mail.getTestoEmail().replace("§", "\n"));
                    serverMailList.get(mail.getDest()).add(0, mail);
                    success = true;
                    appendToLog (mail.getMittEmail() + " ha inviato una nuova mail a: " + mail.getCcString());

                }
                else {
                    appendToLog("Errore nell'invio della mail da " + mail.getMittEmail() + ": Indirizzo mail non esistente");
                    success = false;
                }
            return success;
        }

        /**
         * Metodo che ritorna la lista di mail associate ad un certo account
         * @param address indirizzo associato all'account di cui si vuole la
         * lista di email
         */
        @Override
        public synchronized ArrayList<Email> getEmail(String address) throws RemoteException{
           return serverMailList.get(address);
        }

        /**
         * Metodo che rimuove dalla lista di mail la
         * @param mail è la mail da eliminare
         * @param key l'indirizzo mail associato
         */
        @Override
        public synchronized void deleteEmail(String key,Email mail) throws RemoteException {
            serverMailList.get(key).remove(mail);
            appendToLog("Mail " + mail + " eliminata dall'account: " + key);
        }

        /**
         * Metodo che sovrascrive il file email.csv con la lista aggiornata di email.
         * Ogni volta che un client esegue il logout(non forzato) viene sovrascritto il
         * file email.csv con la lista delle mail presenti nella hashtable del server.
         */
        public synchronized void writeFile(){
            BufferedWriter bw;
            try {
                bw = new BufferedWriter (new FileWriter("src/server/email.csv"));
                for (String key: serverMailList.keySet()) {
                    ArrayList<Email> list = (ArrayList<Email>) (serverMailList.get(key)).clone();
                    Collections.reverse(list);
                    for(Email mail:list){
                        Date dataSpedizioneEmail = mail.getDataSpedEmail();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String formattedDate = dateFormat.format(dataSpedizioneEmail);
                        bw.write(mail.getMittEmail()+"#"+mail.getDest()+"#"+mail.getArgEmail()+
                                "#"+mail.getTestoEmail().replace("\n","§")+"#"+ mail.getPriorEmail()+
                                "#"+formattedDate+"#"+mail.isRead()+"#"+mail.getCcString()+"\n");
                        bw.flush();
                    }
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//fine Class Log

    /**
     * Metodo che crea un nuovo log
     */
    public Log addLog(String nomeLog, String textLog, Date dataCreazioneLog) throws RemoteException {
        log= new Log(nomeLog,textLog,dataCreazioneLog);
        return log;
    }
    public Log getLogServer() {
        return log;
    }

    /**
     * Costruttore del server Model:
     * legge su file email.csv e ad ogni riga, crea una oggetto Email da
     * aggiungere all'interno della hashtable serverMailList.
     */
    public ServerEmailModel() {
        this.serverMailList = new HashMap<>();
        String csvFile = "src/server/email.csv";
        BufferedReader br = null;
        String line;
        String cvsSplitBy = "#";
        ArrayList<Email> emailListUser = new ArrayList<>();
        ArrayList<Email> emailListUser2 = new ArrayList<>();
        ArrayList<Email> emailListUser3 = new ArrayList<>();
        String keyUser = "user@gmail.com";
        String keyUser2 = "user2@gmail.com";
        String keyUser3 = "user3@gmail.com";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] email = line.split(cvsSplitBy);
                Email mail;

                boolean read = Boolean.parseBoolean(email[6]);
                int prior = Integer.parseInt(email[4]);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date dataSped = format.parse(email[5]);

                String[] dests = email[7].split(",");
                ArrayList<String> cCField = new ArrayList<>(Arrays.asList(dests));
                mail = new Email(email[0], email[1], cCField,email[2],email[3].replace("§","\n"), prior, dataSped, read);

                if (keyUser.equals(email[1]))
                    emailListUser.add(0,mail);
                else if (keyUser2.equals(email[1]))
                    emailListUser2.add(0,mail);
                else if (keyUser3.equals(email[1]))
                    emailListUser3.add(0,mail);
                else
                    System.out.print("Mail non appartenente ad un utente del nostro servizio");
            }
            this.serverMailList.put(keyUser, emailListUser);
            this.serverMailList.put(keyUser2, emailListUser2);
            this.serverMailList.put(keyUser3, emailListUser3);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
