package client;



import server.LogInterface;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PigMail extends JFrame {

	public PigMail(String nickname) {

		//Dialog da guerra per comodità.. (se il prefisso dell'indirizzo locale non è come indicato nell'argomento initialSelectionValue, modificare a piacimento)
		String ipServer = JOptionPane.showInputDialog(null,"Inserire indirizzo IP locale del server","192.168.0.x");

		//TODO Messo qua provvisoriamente, ma è da decidere poi assieme dove va tutta la parte di connessione al server (secondo me controller). Ovviamente da implementare tutta la parte di controllo errori
		try {
			LogInterface server = (LogInterface) Naming.lookup("rmi://"+ipServer+":2000/Log");
			System.out.println("Sto per invocare il metodo sul server");
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); //esempio alternativa: SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
			Date date = new Date();
			server.appendToLog("[ "+dateFormat.format(date)+" - Utente "+nickname+"] Client connesso - "+ InetAddress.getLocalHost());
			/*TODO migliorare il tutto, ad esempio scomponendo i messaggi di log in 2 parti: La prima di 'intestazione' sempre uguale per il determianto client (in questo caso ad esempio data+utente), e una di 'corpo', ossia il messaggio di log vero e proprio
			*/
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}


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

		//setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(
						null, "Sei sicuro di voler uscire?",
						"Conferma chiusura applicazione", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {

					try {LogInterface server = (LogInterface) Naming.lookup("rmi://"+ipServer+":2000/Log");
						System.out.println("Sto per invocare il metodo sul server");
						DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); //esempio alternativa: SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						Date date = new Date();
						server.appendToLog("[ "+dateFormat.format(date)+" - Utente "+nickname+"] Client disconnesso - " + InetAddress.getLocalHost());
						//TODO Idem todo sopra
					} catch (NotBoundException e1) {
						e1.printStackTrace();
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
					finally {
						//todo aggiungere altro eventuale cleanup
						System.exit(0);
					}
				}
			}
		};

		//Serve per il tema, in testing
		//initLookAndFeel();
		//A prescindere dal tema, usa gli elementi di sistema per chiudere, ridurre a icona, ecc
		//setDefaultLookAndFeelDecorated(true);


		addWindowListener(exitListener);
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
	}

}