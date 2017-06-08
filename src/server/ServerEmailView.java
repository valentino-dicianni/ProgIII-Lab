package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

interface ServerEmailInterfaceView {
    JPanel logAreaPanel(String text);
    JPanel logOptions();
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

        serverEmailCtrl.createLog(logTxtArea.getText());
    }

    public JPanel logAreaPanel(String text) {
        removeAll();
        JPanel mainPanel = new JPanel(new BorderLayout());

        JButton cleanButton = new JButton("Pulisci log");
        cleanButton.setFont(new Font("Helvetica", Font.BOLD, 14));
        mainPanel.setBorder(BorderFactory.createTitledBorder("LOG"));
        logTxtArea.setEditable(false);
        logTxtArea.setRows(10);
        logTxtArea.setBackground(Color.BLACK);
        logTxtArea.setForeground(Color.GREEN);
        logTxtArea.setText(text);
        logTxtArea.setFont(new Font("Helvetica", Font.PLAIN, 14));
        logTxtArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane logTxTAreaPane = new JScrollPane(logTxtArea);
        logTxTAreaPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(logTxTAreaPane,BorderLayout.CENTER);
        cleanButton.addActionListener(serverEmailCtrl);
        mainPanel.add(cleanButton,BorderLayout.SOUTH);
        return mainPanel;
    }

    public JPanel logOptions(){
        JPanel logOptionsPanel = new JPanel(new BorderLayout());
        JCheckBox colorCheckBoxBW = new JCheckBox("<html>Nero/<br>bianco</html>");
        colorCheckBoxBW.setFont(new Font("Helvetica", Font.BOLD, 13));
        JCheckBox colorCheckBoxBG = new JCheckBox("<html>Nero/<br>Verde</html>");
        colorCheckBoxBG.setFont(new Font("Helvetica", Font.BOLD, 13));
        JCheckBox colorCheckBoxWB = new JCheckBox("<html>Bianco/<br>Nero</html>");
        colorCheckBoxWB.setFont(new Font("Helvetica", Font.BOLD, 13));

        colorCheckBoxBG.setSelected(true);

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

    /**
     * Una volta inviata una notifica dall'Observable, il seguente metodo viene eseguito.
     * L'argomento arg, per ora, contiene un oggetto Log.
     * E' necessario fare il parsing in quanto nel metodo update, arg è un argomento di tipo oggetto generico */
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof ServerEmailModel.Log){
            logTxtArea.setText(((ServerEmailModel.Log) arg).getTestoLog());
        }
    }
}

