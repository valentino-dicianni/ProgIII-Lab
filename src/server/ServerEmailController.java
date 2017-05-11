package server;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by Daniele on 05/05/2017.
 */
public class ServerEmailController implements ActionListener {
    private ServerEmailModel serverEmailMod;
    private ServerEmailModel.Log log;

    public ServerEmailController(ServerEmailModel serverEmailMod){
        this.serverEmailMod = serverEmailMod;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        serverEmailMod.log.clearLog();
    }

    public ServerEmailModel.Log createLog(int idLog, String nomeLog, String textLog, Date dataCreazioneLog) {
        try {
            return serverEmailMod.addLog(idLog,nomeLog,textLog,dataCreazioneLog);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
