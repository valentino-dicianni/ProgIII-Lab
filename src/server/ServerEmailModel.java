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
        public synchronized boolean inviaMail(Email mail) throws RemoteException{
            if(serverMailList.containsKey(mail.getDestEmail())){
                serverMailList.get(mail.getDestEmail()).add(0,mail);
                appendToLog("Mail inviata da " + mail.getMittEmail() + " a " + mail.getDestEmail());
                Date dataSpedizioneEmail = mail.getDataSpedEmail();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String formattedDate = dateFormat.format(dataSpedizioneEmail);

                BufferedWriter bw= null;
                //scrittura su file
                try {
                    bw = new BufferedWriter (new FileWriter("src/server/email.csv", true));
                    System.out.println(mail.getDataSpedEmail());
                    bw.write("\n"+mail.getMittEmail()+"#"+mail.getDestEmail()+"#"+mail.getArgEmail()+
                                "#"+mail.getTestoEmail()+"#"+ mail.getPriorEmail()+"#"+formattedDate+
                                "#false");
                    bw.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                return true;

            }//se la scrittura non è andata a buon fine
            else{
                appendToLog("Errore nell'invio della mail da " + mail.getMittEmail() +": Indirizzo mail non esistente" );
                return false;
            }

        }

        @Override
        public synchronized void forwardMail(Email mail) throws RemoteException {

        }

        @Override
        public synchronized ArrayList<Email> getEmail(String address) throws RemoteException{
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
     */

    public ServerEmailModel() {
        this.serverMailList = new HashMap<>();
        String csvFile = "src/server/email.csv";
        BufferedReader br = null;
        String line = "";
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
                boolean read = Boolean.parseBoolean(email[6]);
                int prior = Integer.parseInt(email[4]);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date dataSped = format.parse(email[5]);
                Email mail = new Email(email[0], email[1], email[2], email[3], prior, dataSped, read);
                if (keyUser.equals(mail.getDestEmail()))
                    emailListUser.add(0,mail);
                else if (keyUser2.equals(mail.getDestEmail()))
                    emailListUser2.add(0,mail);
                else if (keyUser3.equals(mail.getDestEmail()))
                    emailListUser3.add(0,mail);
               else
                    System.out.print("Mail non appartenente ad un utente del nostro servizio");
            }
            this.serverMailList.put(keyUser, emailListUser);
            this.serverMailList.put(keyUser2, emailListUser2);
            this.serverMailList.put(keyUser3, emailListUser3);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

}
