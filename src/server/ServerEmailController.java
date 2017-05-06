package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Daniele on 05/05/2017.
 */
public class ServerEmailController implements ActionListener {
    private ServerEmailModel serverEmailMod;

    public ServerEmailController(ServerEmailModel serverEmailMod){
        this.serverEmailMod = serverEmailMod;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object panel =  e.getSource();
        System.out.print(panel);
    }
}
