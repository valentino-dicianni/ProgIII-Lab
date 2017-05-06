package server;

import java.util.Date;

/**
 * Created by Daniele on 06/05/2017.
 */
public class Log {
    private String  nomeLog, testoLog;
    private Date dataCreazione;
    private int idLog;

    public Log() {

    }

    public Log(int idLog, String nomeLog, String testoLog, Date dataCreazione){
        this.idLog = idLog;
        this.nomeLog = nomeLog;
        this.testoLog = testoLog;
        this.dataCreazione = dataCreazione;
    }


    public String getTestoLog() {
        return testoLog;
    }

    public void setTestoLog(String testoLog) {
        this.testoLog = testoLog;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getNomeLog() {
        return nomeLog;
    }

    public void setNomeLog(String nomeLog) {
        this.nomeLog = nomeLog;
    }

    public void removeLog(int idLog){

    }
}
