package progettoEmail;

import java.util.Date;

public class Email {
  	private String mittEmail, destEmail, argEmail, testoEmail;
  	private int priorEmail;
  	private Date dataSpedEmail;
  	boolean isRead;
  	
	public Email(String mitt, String dest, String arg, String testo, int prior, Date dataSped, boolean isRead){
		this.mittEmail = mitt;
		this.destEmail = dest;
		this.argEmail = arg;
		this.testoEmail = testo;
		this.priorEmail = prior;
		this.dataSpedEmail = dataSped;
		this.isRead = isRead;
	}
	
	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getDestEmail() {
		return destEmail;
	}

	public void setDestEmail(String destEmail) {
		this.destEmail = destEmail;
	}

	public String getArgEmail() {
		return argEmail;
	}

	public void setArgEmail(String argEmail) {
		this.argEmail = argEmail;
	}

	public String getTestoEmail() {			
		return testoEmail;
	}

	public void setTestoEmail(String testoEmail) {
		this.testoEmail = testoEmail;
	}

	public int getPriorEmail() {
		return priorEmail;
	}

	public void setPriorEmail(int priorEmail) {
		this.priorEmail = priorEmail;
	}

	public Date getDataSpedEmail() {
		return dataSpedEmail;
	}

	public void setDataSpedEmail(Date dataSpedEmail) {
		this.dataSpedEmail = dataSpedEmail;
	}

	public void setMittEmail(String mittEmail) {
		this.mittEmail = mittEmail;
	}

	public String getMittEmail(){
		return mittEmail;			
	}
	
	
}
