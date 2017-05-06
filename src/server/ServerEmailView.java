package server;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Daniele on 05/05/2017.
 */

interface ServerEmailInterfaceView {

}

public class ServerEmailView extends JPanel implements ServerEmailInterfaceView, Observer {
    private ServerEmailController serverEmailCtrl;
    private LogPanel log;

    public ServerEmailView(ServerEmailController serverEmailCtrl){
        this.serverEmailCtrl = serverEmailCtrl;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
       // setBackground(Color.GREEN);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(10,10,10,10);

        c.fill = GridBagConstraints.BOTH;
        log = new LogPanel();
        add(log, c);
    }



    @Override
    public void update(Observable o, Object arg) {

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
