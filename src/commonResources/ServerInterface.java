package commonResources;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Daniele on 11/05/2017.
 */

/* Interfaccia che estende Remote per funzionamento RMI*/
public interface ServerInterface extends Remote{
    void appendToLog(String testoLog) throws RemoteException;
    boolean inviaMail(Email mail) throws RemoteException;
    ArrayList<Email> getEmail(String address) throws RemoteException;
    void deleteEmail(String client, Email mail) throws RemoteException;

}
