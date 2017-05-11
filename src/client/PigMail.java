package client;



import server.LogInterface;

import javax.swing.*;
import java.net.InetAddress;
import java.rmi.Naming;

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

		//Serve per il tema, in testing
		//initLookAndFeel();
		//A prescindere dal tema, usa gli elementi di sistema per chiudere, ridurre a icona, ecc
		//setDefaultLookAndFeelDecorated(true);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Email di " + nickname);

        pack();
        setSize(1000, 700);
        setVisible(true);
	}

/*	private static void initLookAndFeel() {
		String lookAndFeel = null;
		final String LOOKANDFEEL = "Metal";
		final String THEME = "DefaultMetal";

		if (LOOKANDFEEL != null) {
			if (LOOKANDFEEL.equals("Metal")) {
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			else if (LOOKANDFEEL.equals("System")) {
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			}

			else if (LOOKANDFEEL.equals("Motif")) {
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			}

			else if (LOOKANDFEEL.equals("GTK")) {
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			}

			else {
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			try {

				UIManager.setLookAndFeel(lookAndFeel);

				if (LOOKANDFEEL.equals("Metal")) {
					if (THEME.equals("DefaultMetal"))
						MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
					else if (THEME.equals("Ocean"))
						MetalLookAndFeel.setCurrentTheme(new OceanTheme());
					else
					//	errore

					UIManager.setLookAndFeel(new MetalLookAndFeel());
				}
			}

			catch (ClassNotFoundException e) {

			}

			catch (UnsupportedLookAndFeelException e) {

			}

			catch (Exception e) {

			}
		}
	}*/

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PigMail client = new PigMail("Franz");
		//ServerPigMail server = new ServerPigMail();

		try {
			//LogInterface server = (LogInterface) Naming.lookup("rmi://127.0.0.1:2000/Log"); // cerco il server sulla porta 2000
			LogInterface server = (LogInterface) Naming.lookup("rmi://127.0.0.1:2000/Log");

			System.out.println("Sto per invocare il metodo sul server");
			server.appendToLog("Client connesso - "+ InetAddress.getLocalHost());
			//System.out.println(result.intValue());
		}
		catch(Exception e) {e.printStackTrace();}
	}

}