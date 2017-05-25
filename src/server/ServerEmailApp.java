package server;

import javax.swing.*;

public class ServerEmailApp extends JFrame {
    public ServerEmailApp(){
        // Modello
        ServerEmailModel serverEmailMod = new ServerEmailModel();

        // Controller
        ServerEmailController serverEmailCtrl = new ServerEmailController(serverEmailMod);

        // View
        ServerEmailView serverEmailView =  new ServerEmailView(serverEmailCtrl);

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
            ServerEmailApp server = new ServerEmailApp();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to create Server", "createServerError", JOptionPane.ERROR_MESSAGE);
        }
    }
}

