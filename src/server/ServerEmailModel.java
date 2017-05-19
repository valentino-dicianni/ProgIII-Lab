package server;

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

/**
 * Created by Daniele on 05/05/2017.
 */


public class ServerEmailModel extends Observable {
    public Log log;
    private HashMap<String, ArrayList<Email>> serverMailList = new HashMap<>();


    public class Log extends UnicastRemoteObject implements LogInterface{
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

        public String newMessageLog(String content){
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); //esempio alternativa: SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            Date date = new Date();
            String newLogMessage;
            String header = null;
            try {
               //TODO aggiungere nome del client all'header
                header = "[ "+dateFormat.format(date)+ "Client IP:"+getClientHost()+"] - ";
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            newLogMessage = header + content;
            return newLogMessage;
        }

        public void appendToLog(String testoLog){
            setTestoLog(getTestoLog()+"\n"+newMessageLog(testoLog));
            System.out.println("Metodo server eseguito in seguito a richiesta client, nuovo log: "+getTestoLog());
            setChanged();
            notifyObservers(this);
        }
        public void clearLog(){
            setTestoLog("");
            setChanged();
            notifyObservers(this);
        }
    }

    public Log addLog(int idLog, String nomeLog, String textLog, Date dataCreazioneLog) throws RemoteException {
        log= new Log(idLog,nomeLog,textLog,dataCreazioneLog);
        return log;
    }
    public Log getLogServer() {
        return log;
    }


    public void inviaMail(Email mail){
        if(serverMailList.containsKey(mail.getDestEmail())){
            serverMailList.get(mail.getDestEmail()).add(mail);
        }
        else{
            ArrayList<Email> emailList = new ArrayList<>();
            emailList.add(mail);
            serverMailList.put(mail.getDestEmail(), emailList);
        }
    }
    public ArrayList<Email> getEmail(String address){
        return serverMailList.get(address);
    }


}
