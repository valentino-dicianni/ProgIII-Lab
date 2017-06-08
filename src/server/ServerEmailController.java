package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class ServerEmailController implements ActionListener {
    private ServerEmailModel serverEmailMod;

    public ServerEmailController(ServerEmailModel serverEmailMod){
        this.serverEmailMod = serverEmailMod;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        serverEmailMod.getLogServer().clearLog();
    }

    public void createLog(String textLog) {
        try {
            serverEmailMod.addLog("log1", textLog);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
