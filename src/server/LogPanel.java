package server;

import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;

/**
 * Created by valentino on 06/05/17.
 */
public class LogPanel extends JPanel {
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
