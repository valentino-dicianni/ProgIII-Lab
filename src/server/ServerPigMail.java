package server;

import javax.swing.*;
import java.awt.*;

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
        ServerEmailView serverEmailView = new ServerEmailView(serverEmailCtrl);

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
        ServerPigMail server = new ServerPigMail();
    }
}

/*Class Log Panel*/
class LogPanel extends JPanel {
    private JTextArea logTxtArea;
    private JButton cleanButton;

    public LogPanel(){
        this.setLayout(new BorderLayout());
        this.logTxtArea = new JTextArea();
        this.cleanButton = new JButton("Pulisci log");
        this.setBorder(BorderFactory.createTitledBorder("LOG"));
        logTxtArea.setEditable(false);
        logTxtArea.setBackground(Color.BLACK);
        logTxtArea.setForeground(Color.GREEN);
        logTxtArea.setText("Prova prova ...");

        add(logTxtArea,BorderLayout.CENTER);
        add(cleanButton,BorderLayout.SOUTH);
    }
}
