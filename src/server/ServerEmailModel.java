package server;

import java.util.Date;
import java.util.Observable;

/**
 * Created by Daniele on 05/05/2017.
 */
public class ServerEmailModel extends Observable{
  //  private String logs; //i log di sistema...struttura dati da rivedere forse
    Log log;

    public void clearLog(Log log){
        log.setTestoLog("");
        setChanged();
        notifyObservers(log);
    }

    public Log getLog() {
        return log;
    }

    public Log addLog(int idLog, String nomeLog, String textLog, Date dataCreazioneLog){
        log = new Log(idLog,nomeLog,textLog,dataCreazioneLog);
        return log;
    }
}
