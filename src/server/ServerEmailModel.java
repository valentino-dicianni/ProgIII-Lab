package server;

import commonResources.Email;
import commonResources.ServerInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;


public class ServerEmailModel extends Observable {
    private Log log;
    private HashMap<String, ArrayList<Email>> serverMailList = new HashMap<>();


    //classe Log
    public class Log extends UnicastRemoteObject implements ServerInterface {
        private String  nomeLog, testoLog;
        private Date dataCreazione;
        private int idLog;

        public Log(int idLog, String nomeLog, String testoLog, Date dataCreazione) throws RemoteException {

            this.idLog = idLog;
            this.nomeLog = nomeLog;
            this.testoLog = testoLog;
            this.dataCreazione = dataCreazione;

            // set del file delle policy di sicurezza (affinché il Security Manager possa leggerlo)
            String result =  System.setProperty("java.security.policy","file:server.policy");

            // creo il SecurityManager, se non esiste già
            try {
                if (System.getSecurityManager() == null) {System.setSecurityManager(new SecurityManager());}

                // creo registry che ascolta su una specifica porta
                java.rmi.registry.LocateRegistry.createRegistry(2000);
                // binding del server su rmiregistry alla porta opportuna
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

        public void removeLog(int idLog){
            this.idLog = idLog;
        }

        public void appendToLog(String testoLog){
            setTestoLog(getTestoLog()+"\n"+newMessageLog(testoLog));
            System.out.println("Metodo server eseguito in seguito a richiesta client, nuovo log: "+getTestoLog());
            setChanged();
            notifyObservers(this);
        }

        public String newMessageLog(String content){
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); //esempio alternativa: SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            Date date = new Date();
            String newLogMessage;
            String header = null;
            try {
               //TODO aggiungere nome del client all'header
                header = "[ "+dateFormat.format(date)+ " Client IP:"+getClientHost()+"] - ";
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            newLogMessage = header + content;
            return newLogMessage;
        }


        public void clearLog(){
            setTestoLog("");
            setChanged();
            notifyObservers(this);
        }

        /**
         * metodo che aggiunge alla lista delle mail di un utente la nuova mail inviata
         * @syncronized per per evitare che che ci sia una lettura mentre avviene la scrittura
         * nel caso in cui non esistelle la casella mail a cui aggiungerla: messaggio di errore*/
        @Override
        public synchronized void inviaMail(Email mail) {
            if(serverMailList.containsKey(mail.getDestEmail())){
                serverMailList.get(mail.getDestEmail()).add(mail);
                appendToLog("Mail inviata da " + mail.getMittEmail() + " a " + mail.getDestEmail());
            }
            else{
                ArrayList<Email> emailList = new ArrayList<>();
                emailList.add(mail);
                serverMailList.put(mail.getDestEmail(), emailList);
                appendToLog("Mail inviata da " + mail.getMittEmail() + " a " + mail.getDestEmail());

            }
        }

        @Override
        public synchronized void forwardMail(Email mail) throws RemoteException {

        }

        @Override
        public synchronized ArrayList<Email> getEmail(String address){
           return serverMailList.get(address);
        }

        @Override
        public synchronized void deleteEmail(String key,Email mail) throws RemoteException {
            serverMailList.get(key).remove(mail);
            appendToLog("Mail " + mail + " eliminata dall'account: " + key);
        }
    }//fine Class Log


    public Log addLog(int idLog, String nomeLog, String textLog, Date dataCreazioneLog) throws RemoteException {
        log= new Log(idLog,nomeLog,textLog,dataCreazioneLog);
        return log;
    }
    public Log getLogServer() {
        return log;
    }

    /**
     * costruttore che va a prendere da file le vecchie mail e inizializza la lista di mail dei contatti
     * TODO @Fra965 -> implementa lettura da file
     */
    public ServerEmailModel(){}


}
