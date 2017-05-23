package client;



import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ClientEmailApp extends JFrame {
    private String emailAddress;
    private String nickname;

	public ClientEmailApp(String nickname, String emailAddress) {

		// Model
		ClientEmailModel clientMailMod = new ClientEmailModel(nickname, emailAddress);

		// Controller
		ClientEmailController clientEmailCtrl = new ClientEmailController(clientMailMod);

		// View
		ClientEmailView clientEmailView = new ClientEmailView(clientEmailCtrl);

		this.emailAddress = emailAddress;
		this.nickname = nickname;

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
		//A prescindere dal tema, usa gli elementi di sistema per chiudere, ridurre a icona, ecc
		setDefaultLookAndFeelDecorated(true);


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
	}

	public static void main(String[] args) {
		// TODO creare un input panel per chiedere nickname e emailAddress per poterlo personalizzare
		ClientEmailApp client = new ClientEmailApp("Franz1", "user2@gmail.com");
	}

}