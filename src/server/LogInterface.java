package server;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Created by Daniele on 11/05/2017.
 */

/* Interfaccia che estende Remote per funzionamento RMI*/
public interface LogInterface extends Remote{
    void appendToLog(String testoLog) throws RemoteException;
    void inviaMail(Email mail) throws RemoteException;
    void forwardMail(Email mail) throws RemoteException;
    Email getEmail(String address) throws RemoteException;
    void deleteEmail(Email mail) throws RemoteException;

}
