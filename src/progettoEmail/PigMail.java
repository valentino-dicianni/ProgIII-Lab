package progettoEmail;

import javax.swing.*;

public class PigMail extends JFrame {

	public PigMail(String nickname) {
		
		// Modello
		ClientEmailModel clientMailMod = new ClientEmailModel(nickname, "user1@mail.it");

		// Controller
		ClientEmailController clientEmailCtrl = new ClientEmailController(clientMailMod);

		// View
		ClientEmailView clientEmailView = new ClientEmailView(clientEmailCtrl);

		//Instaurazione relazione observer-observerable tra vista (Observer) e modello (Observable)
		clientMailMod.addObserver(clientEmailView);
        clientEmailCtrl.setMailList();
		add(clientEmailView);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Email di " + nickname);
        pack();
        setSize(1000, 600);
        setVisible(true);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PigMail frame = new PigMail("Franz");

	}
}