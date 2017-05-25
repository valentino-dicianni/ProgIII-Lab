package commonResources;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *  Interfaccia che estende Remote per funzionamento RMI
 */
public interface ServerInterface extends Remote{
    void appendToLog(String testoLog) throws RemoteException;
    ArrayList<Email> getEmail(String address) throws RemoteException;
    boolean inviaMail(Email mail) throws RemoteException;
    void deleteEmail(String client, Email mail) throws RemoteException;
    void writeFile() throws RemoteException;
    void setReadMail(String address, Email mail) throws RemoteException;
    int getInfoLetture(String addres) throws RemoteException;


}
