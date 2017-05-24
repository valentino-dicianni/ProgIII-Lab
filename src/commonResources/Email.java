package commonResources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Email implements Serializable {
  	private String mittEmail, destEmail, argEmail, testoEmail;
  	private ArrayList<String> multiDes = new ArrayList<>();
  	private int priorEmail;
  	private Date dataSpedEmail;
  	boolean isRead;

  	/*TODO: modificare il campo destinatari! deve essere una lista (ArrayList preferibilmente) di stringhe*/

	public Email(String mitt, String dest, String arg, String testo, int prior, Date dataSped, boolean isRead){
		this.mittEmail = mitt;
		this.destEmail = dest;
		this.argEmail = arg;
		this.testoEmail = testo;
		this.priorEmail = prior;
		this.dataSpedEmail = dataSped;
		this.isRead = isRead;
	}

	public Email(String mitt, String dest, ArrayList<String> multiDes,
				 String arg, String testo, int prior, Date dataSped, boolean isRead){
		this.mittEmail = mitt;
		this.destEmail = dest;
		this.multiDes = multiDes;
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

    public ArrayList<String> getMultiDes() {
        return multiDes;
    }

    public String getCcString(){
	    String res="";
	    for(String iter:multiDes){
	        res=res+iter+",";
        }
        if (!res.equals("") && res.length() > 0 ) {
            res = res.substring(0, res.length()-1);
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
	    Email mail = (Email)o;
        if(this.mittEmail.equals(mail.getMittEmail()) &&
                this.destEmail.equals(mail.getDestEmail()) &&
                this.argEmail.equals(mail.getArgEmail()) &&
                this.testoEmail.equals(mail.getTestoEmail())){return true;}
        else return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
