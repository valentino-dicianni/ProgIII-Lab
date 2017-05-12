package server;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

/**
 * Created by Daniele on 05/05/2017.
 */
public class ServerPigMail extends JFrame {

    public ServerPigMail(){
        // Modello
        ServerEmailModel serverEmailMod = new ServerEmailModel();

        // Controller
        ServerEmailController serverEmailCtrl = new ServerEmailController(serverEmailMod);

        // View
        ServerEmailView serverEmailView = null;
        serverEmailView = new ServerEmailView(serverEmailCtrl);

        //Instaurazione relazione observer-observerable tra vista (Observer) e modello (Observable)
        serverEmailMod.addObserver(serverEmailView);

        add(serverEmailView);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");
        pack();
        setSize(1000, 600);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            ServerPigMail server = new ServerPigMail();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to create Server", "createServerError", JOptionPane.ERROR_MESSAGE);
        }
    }
}

