package server;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Created by Daniele on 11/05/2017.
 */

/* Interfaccia che estende Remote per funzionamento RMI (interfaccia che il client usa per chiamare il metodo appendToLog ) */
public interface LogInterface extends Remote{
    void appendToLog(String testoLog) throws RemoteException;
}
