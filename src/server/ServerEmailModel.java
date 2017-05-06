package server;

import java.util.Observable;

/**
 * Created by Daniele on 05/05/2017.
 */
public class ServerEmailModel extends Observable{
    private String logs; //i log di sistema...struttura dati da rivedere forse


    public void clearLog(){
        logs = "";
        setChanged();
        notifyObservers();
    }

    public String getLogs() {
        return logs;
    }
}
