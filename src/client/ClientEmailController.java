package client;

import commonResources.Email;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;


import javax.swing.*;

public class ClientEmailController implements ActionListener, MouseListener {
	
	private ClientEmailModel clientEmailMod;
	private Email currentOpenedEmail;
	
	public ClientEmailController(ClientEmailModel clientEmailMod){
		this.clientEmailMod = clientEmailMod;
	}
	
	public String getClientName(){
		return clientEmailMod.getNomeAcClient();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if((((JButton) e.getSource()).getName())=="frwdBtn"){
			clientEmailMod.showNewFrwdEmailForm(currentOpenedEmail);
		}
		else if((((JButton) e.getSource()).getName())=="newMailBtn"){
			//if(e.getActionCommand()==)
			clientEmailMod.showNewEmailForm();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		JList theList = (JList) e.getSource();
        ListModel model = theList.getModel();
        int index = theList.locationToIndex(e.getPoint());
        if (index > -1) {        
        	theList.setToolTipText(null);
            Email selectedEmail = (Email) model.getElementAt(index);
            clientEmailMod.openEmail(selectedEmail);
            currentOpenedEmail = selectedEmail;
        }		
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
		JList theList = (JList) e.getSource();
		theList.clearSelection();		
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	public void setMailList(){
	    clientEmailMod.showMail();
    }

	public void newEmail(String toFieldText, String subjectFieldText, String contentFieldText) throws RemoteException {
		clientEmailMod.sendEmail(toFieldText,subjectFieldText,contentFieldText);
	}
}
