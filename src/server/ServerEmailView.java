package server;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Daniele on 05/05/2017.
 */

interface ServerEmailInterfaceView {

}

public class ServerEmailView extends JPanel implements ServerEmailInterfaceView, Observer {
    private ServerEmailController serverEmailCtrl;
    private JTextArea logTxtArea = new JTextArea();

    public ServerEmailView(ServerEmailController serverEmailCtrl) {
        this.serverEmailCtrl = serverEmailCtrl;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 3;
        c.weighty = 1;
        c.insets = new Insets(10, 10, 10, 10);

        c.fill = GridBagConstraints.BOTH;
        add(logAreaPanel("Inizializzazione Log - Server operativo"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(logOptions(),c);

        serverEmailCtrl.createLog(1,"log1",logTxtArea.getText(),null);
    }

    private JPanel logAreaPanel(String text) {
        removeAll();
        JPanel mainPanel = new JPanel(new BorderLayout());

        JButton cleanButton = new JButton("Pulisci log");
        mainPanel.setBorder(BorderFactory.createTitledBorder("LOG"));

        logTxtArea.setEditable(false);
        logTxtArea.setBackground(Color.BLACK);
        logTxtArea.setForeground(Color.WHITE);
        logTxtArea.setText(text);

        mainPanel.add(logTxtArea,BorderLayout.CENTER);
        cleanButton.addActionListener(serverEmailCtrl);
        mainPanel.add(cleanButton,BorderLayout.SOUTH);
        return mainPanel;
    }

    private JPanel logOptions(){
        JPanel logOptionsPanel = new JPanel(new BorderLayout());
        JCheckBox colorCheckBoxBW = new JCheckBox("<html>Nero/<br>bianco</html>");
        JCheckBox colorCheckBoxBG = new JCheckBox("<html>Nero/<br>Verde</html>");
        JCheckBox colorCheckBoxWB = new JCheckBox("<html>Bianco/<br>Nero</html>");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(colorCheckBoxBG);
        buttonGroup.add(colorCheckBoxBW);
        buttonGroup.add(colorCheckBoxWB);


        colorCheckBoxBW.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logTxtArea.setBackground(Color.BLACK);
                logTxtArea.setForeground(Color.WHITE);
            }
        });
        colorCheckBoxBG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logTxtArea.setBackground(Color.BLACK);
                logTxtArea.setForeground(Color.GREEN);
            }
        });
        colorCheckBoxWB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logTxtArea.setBackground(Color.WHITE);
                logTxtArea.setForeground(Color.BLACK);
            }
        });

        logOptionsPanel.add(colorCheckBoxBG,BorderLayout.NORTH);
        logOptionsPanel.add(colorCheckBoxBW,BorderLayout.CENTER);
        logOptionsPanel.add(colorCheckBoxWB,BorderLayout.SOUTH);
        return logOptionsPanel;
    }

    /* Una volta inviata una notifica dall'Observable, il seguente metodo viene eseguito. L'argomento arg, per ora, contiene un oggetto Log
     * E' necessario fare il parsing in quanto nel metodo update, arg è un argomento di tipo oggetto generico */
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof ServerEmailModel.Log){
            logTxtArea.setText(((ServerEmailModel.Log) arg).getTestoLog());
        }
    }
}

