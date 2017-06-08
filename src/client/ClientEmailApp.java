package client;



import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.event.*;
import java.util.Objects;

public class ClientEmailApp extends JFrame {

	public ClientEmailApp(String nickname, String emailAddress, String ipServer) {
		// Model
		ClientEmailModel clientMailMod = new ClientEmailModel(nickname, emailAddress,ipServer);

		// Controller
		ClientEmailController clientEmailCtrl = new ClientEmailController(clientMailMod);

		// View
		ClientEmailView clientEmailView = new ClientEmailView(clientEmailCtrl);

        //Instaurazione relazione observer-observerable tra vista (Observer) e modello (Observable)
		clientMailMod.addObserver(clientEmailView);
        clientEmailCtrl.setMailList();
		add(clientEmailView);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(
						null, "Sei sicuro di voler uscire?",
						"Conferma chiusura applicazione", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
				    clientMailMod.closeOperation();
                    System.exit(0);
                }
			}
		};

		//Serve per il tema, in testing
		initLookAndFeel();

		addWindowListener(exitListener);
        setTitle("Email di " + nickname);

        pack();
       	setSize(1024, 768);
        setVisible(true);
	}

	private static void initLookAndFeel() {
		String lookAndFeel = null;
		final String LOOKANDFEEL = "Metal";
		final String THEME = "Ocean";

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
			} else {
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
	}

	public static void main(String[] args) {
		String nickname = null, email=null,ipServer,s;

		Object[] possibilities = {"Franz - user@gmail.com", "Bob - user2@gmail.com", "Pino - user3@gmail.com"};
		JFrame frame = new JFrame("InputDialog Example #1");
		s = (String) JOptionPane.showInputDialog(frame,"Seleziona l'utente:","Selezione utente",	JOptionPane.PLAIN_MESSAGE,null,possibilities,"ham");

		if ((s != null) && (s.length() > 0)) {
			if(Objects.equals(s, "Franz - user@gmail.com")){
				nickname = "Franz";
				email = "user@gmail.com";
			}
			else if(Objects.equals(s, "Bob - user2@gmail.com")){
				nickname = "Bob";
				email = "user2@gmail.com";
			}
			else if(Objects.equals(s, "Pino - user3@gmail.com")){
				nickname = "Pino";
				email = "user3@gmail.com";
			}
		}else{
			System.exit(0);
		}

		ipServer = JOptionPane.showInputDialog(null, "Inserire indirizzo IP locale del server", "localhost");
		if ((ipServer != null) && (ipServer.length() > 0)) {
			try {
				ClientEmailApp client = new ClientEmailApp(nickname, email, ipServer);
			} catch (Exception e) {
                System.exit(0);
			}
		}else{
			System.exit(0);
		}
	}
}