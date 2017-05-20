package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.*;


//Interfaccia della vista
interface ClientEmailInterfaceView {
	void updateEmailListGUI(Email c);
	JPanel newEmailPanel();
	JPanel readEmailPanel();
}


public class ClientEmailView extends JPanel implements ClientEmailInterfaceView, Observer {	
	private ClientEmailController clientEmailCtrl;	
	private JPanel interactiveRightPanel;
	
	//email list panel
	private JList<Email> list =  new JList<Email>();
	
	//New email form panel	
	private JTextField fromField = new JTextField();
	private JTextField toField = new JTextField();
	private JTextField subjectField = new JTextField();
	private JTextArea contentTextArea = new JTextArea();


	//read email panel
	private JLabel fromLabel = new JLabel();
	private JLabel toLabel = new JLabel();
	private JLabel subjectLabel = new JLabel();
	private JLabel txtLabel = new JLabel();

	public ClientEmailView(ClientEmailController clientEmailCtrl) {
		this.clientEmailCtrl = clientEmailCtrl;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        JLabel headerLabel = new JLabel("Posta in arrivo"); //TODO da sistemare poi bene nella topbar, è solo una prova per vedere come potrebbe venire

        c.gridwidth = 1;
        c.gridheight = 1;

        c.gridx = 0; //TODO idem sopra
        c.gridy = 0; //TODO idem sopra
        headerLabel.setHorizontalAlignment(JLabel.CENTER); //TODO idem sopra
        c.fill = GridBagConstraints.HORIZONTAL; //TODO idem sopra
        add(headerLabel, c);//TODO idem sopra

		/* TOPBAR 
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello superiore all'interno del pannello this) */
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(defaultTopPanel(), c);
		
		/* PARTE SINISTRA 
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello di sinistra all'interno del pannello this)*/
		c.gridx = 0;
		c.gridy = 1; //1 perchè è sotto la topbar
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(defaultLeftPanel(), c);
		
		/* PARTE DESTRA 
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello di destra all'interno del pannello this)*/
		c.gridx = 1;
		c.gridy = 1; //1 perchè è sotto la topbar
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;

		add(defaultRightPanel() , c);
	}

	/* METODO PER VISUALIZZAZIONE PANNELLO DEFAULT TOPBAR, CON PULSANTI [PROVVISORIO]*/
	private JPanel defaultTopPanel() {
		JPanel defaultTopPanel = new JPanel(new GridBagLayout());
		JButton newMailBtn = new JButton();

		Image img = null;
		try {
			img = ImageIO.read(getClass().getResource("/newMailBtn.png"));
			newMailBtn.setIcon(new ImageIcon(img));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		JButton forwardBtn = new JButton();
		try {
			img = ImageIO.read(getClass().getResource("/frwdBtn.png"));
			forwardBtn.setIcon(new ImageIcon(img));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weightx = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		newMailBtn.setName("newMailBtn");
		forwardBtn.setName("frwdBtn");
		newMailBtn.setToolTipText("<html>Nuova Email</html>");
		forwardBtn.setToolTipText("Inoltra");
		defaultTopPanel.add(newMailBtn,c);
		c.gridx = 1;
        c.weightx = 0.5;
		defaultTopPanel.add(forwardBtn,c);
		newMailBtn.addActionListener(clientEmailCtrl);
		return defaultTopPanel;		
	}

	/* METODO PER VISUALIZZAZIONE PANNELLO DEFAULT SINISTRO, CON LISTA EMAIL [PROVVISORIO]*/
	private JPanel defaultLeftPanel() {		
		JPanel defaultLeftPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1;
        c.fill=GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(list);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		defaultLeftPanel.add(scrollPane,c);
		return defaultLeftPanel;		
	}
	
	/* METODO PER VISUALIZZAZIONE PANNELLO DEFAULT DESTRO, AVENTE FUNZIONI MULTIPLE (DEFAULT: VUOTO//LABEL SEMPLICE) [PROVVISORIO]*/
	private JPanel defaultRightPanel() {
		interactiveRightPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
		JLabel label = new JLabel("Bentornato");
		interactiveRightPanel.add(label);
		interactiveRightPanel.revalidate();
		return interactiveRightPanel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
	
		GridBagConstraints c = new GridBagConstraints(); 
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;		
		c.fill = GridBagConstraints.BOTH;
		//c.insets = new Insets(10,10,10,10);
		/* Caso in cui si crea una nuova email: viene mostrato panel di creazione email */
		if(arg1=="newEmailForm"){	
			this.remove(interactiveRightPanel);
			this.revalidate();
			this.add(newEmailPanel(),c);
			this.repaint();
		}
		/* Caso in cui un email viene selezionata dalla lista: viene mostrato panel di visualizzazione email */
		else if(arg1 instanceof Email){			
			updateEmailListGUI((Email)arg1);			
			this.remove(interactiveRightPanel);
			this.revalidate();
			this.add(readEmailPanel(),c);
			this.repaint();
		}
		else if(arg1 == "updateMailList"){
            list.setModel(((ClientEmailModel)arg0).getList());
            list.setCellRenderer(new MyListCellRenderer());
            list.addMouseListener(clientEmailCtrl);
            list.setFixedCellHeight(75);
            list.setFixedCellWidth(1);


            //Questo listener può rimanere in quanto ha funzioni puramente 'estetiche' ma non influenza in alcun modo il modello
            list.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    Point p = null;
                    JList theList = (JList) e.getSource();
                    ListModel model = theList.getModel();
                    int index = theList.locationToIndex(e.getPoint());
                    if (index > -1) {
                        theList.setToolTipText(null);
                        theList.setSelectedIndex(index);
                    }
                }
            });
		}
	}

	@Override
	public void updateEmailListGUI(Email selectedEmail) {		
		list.setCellRenderer(new MyListCellRenderer());
		fromLabel.setText(selectedEmail.getMittEmail());
		toLabel.setText(selectedEmail.getDestEmail());
		subjectLabel.setText(selectedEmail.getArgEmail());
		txtLabel.setText(selectedEmail.getTestoEmail());
	}

	@Override
	public JPanel newEmailPanel() {
        interactiveRightPanel.removeAll();
        interactiveRightPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

		JPanel formPanel = new JPanel(new BorderLayout());
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(4, 1));

		/*headerPanel.add(new JLabel("Da:"));
		headerPanel.add(newEmailPanel.getFromField());*/

		/*TODO: N.B da modificare i destinatari...aggiungere un campo "Cc" dove si ha la possibilità di aggiungere altri destinatari*/
		headerPanel.add(new JLabel("A:"));
		headerPanel.add(toField);

		headerPanel.add(new JLabel("Oggetto:"));
		headerPanel.add(subjectField);
		headerPanel.setBorder(BorderFactory.createTitledBorder("Invia Nuova Mail"));
		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BorderLayout());
		//bodyPanel.add(new JLabel("Messaggio:"), BorderLayout.NORTH);
		contentTextArea.setLineWrap(true);
		contentTextArea.setWrapStyleWord(true);
		contentTextArea.setPreferredSize(new Dimension(1,1));
		bodyPanel.add(contentTextArea, BorderLayout.CENTER);
        bodyPanel.setBorder(BorderFactory.createTitledBorder("Testo Messaggio"));
		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new BorderLayout());
		
		JButton sendMailButton = new JButton("Invia email");

		//todo capire eventualmente come fare per mettere l'actionlistener nel controller mandando pure i valori
		sendMailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					clientEmailCtrl.newEmail(toField.getText(),subjectField.getText(),contentTextArea.getText());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		footerPanel.add(sendMailButton, BorderLayout.SOUTH);

		formPanel.add(headerPanel, BorderLayout.NORTH);
		formPanel.add(bodyPanel);
		formPanel.add(footerPanel, BorderLayout.SOUTH);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
		interactiveRightPanel.add(formPanel,c);
		interactiveRightPanel.revalidate();
		return interactiveRightPanel;				
	}

	@Override
	public JPanel readEmailPanel() {
        interactiveRightPanel.removeAll();
        interactiveRightPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

		JPanel readPanel = new JPanel(new BorderLayout());		
		JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(3, 1)); //TODO Da convertire in gridbaglayout
		headerPanel.add(new JLabel("DA: "+fromLabel.getText())); //TODO Il getText è un workaround. Valutare se far ritornare direttamente una stringa invece che la label
		//headerPanel.add(readEmailPanel.getFromLabel());
		headerPanel.add(new JLabel("A: "+toLabel.getText())); //TODO Idem sopra
		//headerPanel.add(readEmailPanel.getToLabel());
		headerPanel.add(new JLabel("OGGETTO: "+subjectLabel.getText())); //TODO Idem sopra
		//headerPanel.add(readEmailPanel.getSubjectLabel());
		headerPanel.setBorder(BorderFactory.createTitledBorder("Dati"));
        headerPanel.setBackground(Color.WHITE);

		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BorderLayout());
		bodyPanel.add(txtLabel);
		bodyPanel.setBorder(BorderFactory.createTitledBorder("Testo Messaggio"));

		readPanel.add(headerPanel, BorderLayout.NORTH);
		readPanel.add(bodyPanel);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
		interactiveRightPanel.add(readPanel,c);
		interactiveRightPanel.revalidate();
		return interactiveRightPanel;		
	}
}


//Classe di visualizzazione della lista di email
class MyListCellRenderer extends JCheckBox implements ListCellRenderer<Email> {

	public MyListCellRenderer() {
		setOpaque(true);

	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Email> list, Email value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		Email label = value;
		String mitt = label.getMittEmail();
		String dest = label.getDestEmail();
		String arg = label.getArgEmail();
		String testo = label.getTestoEmail();
		boolean isRead = label.isRead();
		int prior = label.getPriorEmail();
		String labelText = "<html>Mittente: " + mitt + "<br/>Oggetto: " + arg;
		setText(labelText);

		ImageIcon imageIcon;
		if (isRead == false) {
			imageIcon = new ImageIcon(getClass().getResource("/newMail.png"));
		} else {
			imageIcon = new ImageIcon(getClass().getResource("/openedEmail.png"));
		}
		setIcon(imageIcon);
		
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
            setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}
}
