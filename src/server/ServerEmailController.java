package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Date;

public class ServerEmailController implements ActionListener {
    private ServerEmailModel serverEmailMod;

    public ServerEmailController(ServerEmailModel serverEmailMod){
        this.serverEmailMod = serverEmailMod;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        serverEmailMod.getLogServer().clearLog();
    }

    public ServerEmailModel.Log createLog(String nomeLog, String textLog, Date dataCreazioneLog) {
        try {
            return serverEmailMod.addLog(nomeLog,textLog,dataCreazioneLog);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
