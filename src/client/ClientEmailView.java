package client;

import commonResources.Email;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;


//Interfaccia della vista
interface ClientEmailInterfaceView {
	void emailSelectionGUIUpdate(Email selectedEmail);
	JPanel newEmailPanel(ArrayList optEmailData);
	JPanel readEmailPanel(Email selectedEmail);
}


public class ClientEmailView extends JPanel implements ClientEmailInterfaceView, Observer {
	private ClientEmailController clientEmailCtrl;
	private JPanel interactiveRightPanel;
	private JPanel interactiveTopPanel;
	private GridBagConstraints topRightPanelConst;

	//JList contenente le Email
	private JList<Email> clientEmailList =  new JList<Email>();

	//Vars form nuova mail
	private JTextField newMailDest = new JTextField();
    private JTextField newEmailSubject = new JTextField();
	private JTextArea newEmailText = new JTextArea();

	//Vars email ricevuta
	private JLabel receivedEmailSender = new JLabel();
	private JLabel receivedEmailDest = new JLabel();
	private JLabel receivedEmailCc = new JLabel();
	private JLabel receivedEmailSubject = new JLabel();
	private JLabel receivedEmailDate = new JLabel();
	private JTextArea receivedEmailText = new JTextArea();

	public ClientEmailView(ClientEmailController clientEmailCtrl) {
		this.clientEmailCtrl = clientEmailCtrl;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		/* TOPLEFTBAR
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello superiore-sinistro all'interno del pannello this) */
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		add(TopLeftPanel(), c);

		/* TOPRIGHTBAR
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello superiore-destro all'interno del pannello this) */
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHEAST;
		add(TopRightPanel(false,false), c);
		topRightPanelConst = (GridBagConstraints) c.clone();
		setBackground(Color.GREEN);
		
		/* PARTE SINISTRA
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello di sinistra all'interno del pannello this)*/
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.SOUTHWEST;
		add(defaultLeftPanel(),c);

		/* PARTE DESTRA
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello di destra all'interno del pannello this)*/
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.SOUTHEAST;
		add(defaultRightPanel(),c);
	}

	/**
	 * Metodo che crea e restituisce pannello topleft
	 */
	private JPanel TopLeftPanel(){
		JPanel topLeftPanel = new JPanel(new GridBagLayout());
		topLeftPanel.setBackground(Color.decode("#5460ce"));
		GridBagConstraints c = new GridBagConstraints();
		JLabel headerLabel = new JLabel("<html><i><b>Posta di "+clientEmailCtrl.getClientName()+"<b></i></html>");
		headerLabel.setFont(new Font("Helvetica", Font.PLAIN, 15));
		headerLabel.setForeground(Color.WHITE);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0;
        c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		topLeftPanel.add(headerLabel,c);
		return topLeftPanel;
	}

	/**
	 * Metodo che crea e restituisce pannello topright, con pulsanti necessari a seconda dell'impiego
	 */
	private JPanel TopRightPanel(boolean readEmailView,boolean sendEmailView) {
		interactiveTopPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		if(!sendEmailView) {
			JButton newMailBtn = new JButton(); //NEW EMAIL BUTTON
			Image img;
			try {
				img = ImageIO.read(getClass().getResource("/newMailBtn.png"));
				newMailBtn.setIcon(new ImageIcon(img));
			} catch (Exception ex) {
				System.out.println(ex);
			}
			newMailBtn.setName("newMailBtn");
			newMailBtn.setToolTipText("<html>Nuova Email</html>");
			interactiveTopPanel.add(newMailBtn, c);
			newMailBtn.addActionListener(clientEmailCtrl);
			if (readEmailView) {
				JButton forwardBtn = new JButton(); //FORWARD EMAIL BUTTON
				forwardBtn.setToolTipText("Inoltra");
				forwardBtn.setName("frwdBtn");
				try {
					img = ImageIO.read(getClass().getResource("/frwdBtn.png"));
					forwardBtn.setIcon(new ImageIcon(img));
				} catch (Exception ex) {
					System.out.println(ex);
				}
				c.gridx = 1;
				c.weightx = 0.5;
				forwardBtn.addActionListener(clientEmailCtrl);
				interactiveTopPanel.add(forwardBtn, c);
                JButton deleteBtn = new JButton(); //DELETE EMAIL BUTTON
                deleteBtn.setToolTipText("Elimina mail");
                deleteBtn.setName("delEmailBtn");
                try {
                    img = ImageIO.read(getClass().getResource("/delEmailBtn.png"));
                    deleteBtn.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                c.gridx = 2;
                c.weightx = 0.5;
                deleteBtn.addActionListener(clientEmailCtrl);
                interactiveTopPanel.add(deleteBtn, c);
			}
		}
		else{
			JButton sendMailButton = new JButton(""); //SEND EMAIL BUTTON
			Image img;
			try {
				img = ImageIO.read(getClass().getResource("/sendEmailBtn.png"));
				sendMailButton.setIcon(new ImageIcon(img));
			} catch (Exception ex) {
				System.out.println(ex);
			}
			sendMailButton.setName("newMailBtn");
			sendMailButton.setToolTipText("<html>Invia Mail</html>");
			sendMailButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String[] splits = newMailDest.getText().split(",");
					ArrayList<String> dests = new ArrayList<>(Arrays.asList(splits));

					if (clientEmailCtrl.newEmail(dests, newEmailSubject.getText(), newEmailText.getText())){
                        newEmailSubject.setText("");
                        newMailDest.setText("");
                        newEmailText.setText("");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "ATTENZIONE: Indirizzo mail errato", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
                    }
				}
			});
			interactiveTopPanel.add(sendMailButton,c);
		}
		return interactiveTopPanel;
	}

	/**
	 * Metodo che crea e restituisce pannello sinistro
	 * */
	private JPanel defaultLeftPanel() {
		JPanel defaultLeftPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 1;
        c.fill=GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(clientEmailList);
        scrollPane.setMinimumSize(new Dimension(400,1));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		defaultLeftPanel.add(scrollPane,c);
		return defaultLeftPanel;
	}

	/**
	 * Metodo che crea e restituisce pannello destro, con pulsanti necessari a seconda dell'impiego
	 * */
	private JPanel defaultRightPanel() {
		interactiveRightPanel = new JPanel(new GridBagLayout());
		interactiveRightPanel.setBackground(Color.WHITE);
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
		JLabel welcomeLabel = new JLabel("<html><b>Bentornato, "+clientEmailCtrl.getClientName()+". <br>Hai ricevuto "+clientEmailCtrl.getNumMsgNonLetti() +" mail dal tuo ultimo accesso.</b></html>");
		welcomeLabel.setFont(new Font("Helvetica", Font.PLAIN, 15));
		interactiveRightPanel.add(welcomeLabel,c);
		interactiveRightPanel.revalidate();
		return interactiveRightPanel;
	}

	/**
	 * Metodo update, viene richiamato in seguito ad una modifica dell'elemento osservato notificata attraverso il metodo notifyObservers
	 * */
	@Override
	public void update(Observable arg0, Object arg1) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 2;
		c.weighty = 1;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.fill = GridBagConstraints.BOTH;
		/* Caso di creazione nuova email: viene visualizzato panel con form di creazione email */
		if(arg1=="newEmailForm"){
			this.remove(interactiveTopPanel);
			this.remove(interactiveRightPanel);
			this.revalidate();
			this.add(newEmailPanel(null),c);
			this.add(TopRightPanel(false,true), topRightPanelConst);
			this.repaint();
		}
		/* Caso di inoltro o risposta ad una email: viene visualizzato panel con form di creazione email, con set di dati di forward/reply */
		else if(arg1 instanceof ArrayList){
			this.remove(interactiveTopPanel);
			this.remove(interactiveRightPanel);
			this.revalidate();
			ArrayList EmailData = ((ArrayList) arg1);
			this.add(newEmailPanel(EmailData),c);
			this.add(TopRightPanel(false,true), topRightPanelConst);
			this.repaint();
		}
		/* Caso di selezione email dalla lista: viene visualizzato panel di lettura email */
		else if(arg1 instanceof Email){
			this.remove(interactiveTopPanel);
			this.remove(interactiveRightPanel);
			this.revalidate();
			emailSelectionGUIUpdate((Email)arg1);
			this.add(readEmailPanel((Email)arg1),c);
			this.add(TopRightPanel(true,false), topRightPanelConst);
			this.repaint();
		}
		/* Caso di aggiornamento della lista mail all'avvio */
		else if(arg1 == "updateMailList"){
			setEmailList(((ClientEmailModel)arg0).getMailList());
		}
		/* Caso di eliminazione email completata: viene visualizzato panel destro di default */
		else if(arg1 == "deleteCompleted"){
			this.remove(interactiveRightPanel);
			this.revalidate();
			add(defaultRightPanel(),c);
		}
		/* Caso di ricezione nuova email: viene visualizzato dialog informativo */
		else if(arg1 == "newEmailReceived"){
			JOptionPane.showMessageDialog(null, "Hai ricevuto un nuovo messaggio", "Nuovo messaggio!", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Metodo che imposta jlist contenente le email con la lista delle mail ricevute dell'utente
	 * */
	public void setEmailList(DefaultListModel mailList) {
		clientEmailList.setModel(mailList);
		clientEmailList.addMouseListener(clientEmailCtrl);
		clientEmailList.setFixedCellHeight(75);
		clientEmailList.setCellRenderer(new MyListCellRenderer());
		clientEmailList.setFixedCellWidth(400);
		clientEmailList.setBackground(Color.decode("#f9f9f7"));
		clientEmailList.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				JList theList = (JList) e.getSource();
				int index = theList.locationToIndex(e.getPoint());
				if (index > -1) {
					theList.setSelectedIndex(index);
				}
			}
		});
	}

	/**
	 * Metodo d'appoggio che aggiorna jlist mail (per impostare nuovo stato di email letta) e carica dati relativi alla email selezionata per la lettura
	 * */
	@Override
	public void emailSelectionGUIUpdate(Email selectedEmail) {
		clientEmailList.setCellRenderer(new MyListCellRenderer());
		receivedEmailSender.setText("<html><b>DA: </b>"+selectedEmail.getMittEmail()+"</html>");
		receivedEmailSender.setFont(new Font("Helvetica", Font.PLAIN, 13));
		receivedEmailDest.setText("<html><b>A:</b> "+selectedEmail.getCcString()+"</html>");
		receivedEmailDest.setFont(new Font("Helvetica", Font.PLAIN, 13));
		receivedEmailSubject.setText("<html><b>OGGETTO:</b> "+selectedEmail.getArgEmail()+"</html>");
		receivedEmailSubject.setFont(new Font("Helvetica", Font.PLAIN, 13));
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataSped = format.format(selectedEmail.getDataSpedEmail());
		receivedEmailDate.setText("<html><b>DATA INVIO:</b> "+dataSped+"</html>");
		receivedEmailDate.setFont(new Font("Helvetica", Font.PLAIN, 13));
		receivedEmailText.setText(selectedEmail.getTestoEmail());
		receivedEmailText.setFont(new Font("Helvetica", Font.PLAIN, 14));
	}

	/**
	 * Metodo che crea e restituisce pannello di creazione email. optEmailData contiene le informazioni opzionali nel caso in cui l'utente stia scrivendo una nuova mail di
	 * forward o reply
	 * */
	@Override
	public JPanel newEmailPanel(ArrayList optEmailData) {
		JLabel labelTo = new JLabel();
		JLabel labelSubj = new JLabel();
		JLabel labelCc = new JLabel();
		labelTo.setFont(new Font("Helvetica", Font.PLAIN, 13));
		labelSubj.setFont(new Font("Helvetica", Font.PLAIN, 13));
		labelCc.setFont(new Font("Helvetica", Font.PLAIN, 13));
		newEmailSubject.setFont(new Font("Helvetica", Font.PLAIN, 13));
		newMailDest.setFont(new Font("Helvetica", Font.PLAIN, 13));
		newEmailText.setFont(new Font("Helvetica", Font.PLAIN, 14));
		newEmailText.setRows(30);
        interactiveRightPanel.removeAll();
        if(optEmailData != null && optEmailData.get(0).toString().equals("frwd")){
        	newEmailText.setText("\n\n--------MESSAGGIO INOLTRATO--------" +
			"\n\nDA: <"+optEmailData.get(1).toString()+">"+
			"\nA: <" +optEmailData.get(2).toString()+">"+
			"\nOGGETTO: "+optEmailData.get(3).toString()+
			"\nTESTO:\n:"+optEmailData.get(4).toString()+
			"\n\n ------------------------------");
            newEmailText.getCaret().setVisible(true);
            newEmailText.setCaretPosition(0);
			newEmailSubject.setText("");
			newMailDest.setText("");
		}
		else if(optEmailData != null && optEmailData.get(0).toString().equals("reply")){
        	newMailDest.setText(optEmailData.get(1).toString());
        	newEmailSubject.setText("RE: "+optEmailData.get(3).toString());
			newEmailText.setText("\n\n--------In risposta a--------" +
					"\n\nDA: <"+optEmailData.get(1).toString()+">"+
					"\n\nOGGETTO: <"+optEmailData.get(3).toString()+">"+
                    "\nTESTO:\n "+optEmailData.get(4).toString()+
                    "\n\n ------------------------------");
			newEmailText.getCaret().setVisible(true);
			newEmailText.setCaretPosition(0);
		}
        else if(optEmailData != null && optEmailData.get(0).toString().equals("replyAll")) {
            HashSet<String> newDest = new HashSet<>();
            newDest.add(optEmailData.get(1).toString());
            ArrayList<String> dst = (ArrayList<String>) optEmailData.get(2);
            newDest.addAll(dst);
            String res = "";
            for(String str:newDest){
                if(str != optEmailData.get(5))
                    res =res+str+",";
            }
            if (!res.equals("") && res.length() > 0 ) {
                res = res.substring(0, res.length()-1);
            }

            newMailDest.setText(res);
            newEmailSubject.setText("RE: "+optEmailData.get(3).toString());
            newEmailText.setText("\n\n--------In risposta a--------" +
                    "\n\nDA: <"+optEmailData.get(1).toString()+">"+
                    "\nA: <"+res+">"+
                    "\n\nOGGETTO: <"+optEmailData.get(3).toString()+">"+
                    "\nTESTO:\n"+optEmailData.get(4).toString()+
                    "\n\n ------------------------------");
            newEmailText.getCaret().setVisible(true);
            newEmailText.setCaretPosition(0);
        }
		else {
			newEmailText.setText("");
		}

        interactiveRightPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		//SEZIONE CON DATI EMAIL (DESTINATARIO E OGGETTO EMAIL)
		JPanel headerPanel = new JPanel(new GridBagLayout());
		labelTo.setText("A:");
		headerPanel.add(labelTo,c);
		c.gridy++;
		headerPanel.add(newMailDest,c);
		c.gridy++;
		labelSubj.setText("Oggetto:");
		headerPanel.add(labelSubj,c);
		c.gridy++;
		headerPanel.add(newEmailSubject,c);
		c.gridy++;

		//SEZIONE CON CORPO DELLA MAIL (TESTO EMAIL)
		JPanel bodyPanel = new JPanel(new GridBagLayout());
		c.weighty = 1;
		newEmailText.setLineWrap(true);
		newEmailText.setWrapStyleWord(true);
		c.fill = GridBagConstraints.BOTH;
		JScrollPane newMailTextScrollPane = new JScrollPane(newEmailText);
		newMailTextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		bodyPanel.add(newMailTextScrollPane ,c);
		c.weighty = 0;
		c.gridy=0;

		TitledBorder titledBorderData = BorderFactory.createTitledBorder("Invia Nuova Email");
		titledBorderData.setTitleColor(Color.decode("#5460ce"));
		headerPanel.setBorder(titledBorderData);
		c.insets = new Insets(5,5,5,5);
		interactiveRightPanel.add(headerPanel,c);
		c.gridy=1;
		c.weighty = 1;
		TitledBorder titledBorderText = BorderFactory.createTitledBorder("Messaggio");
		titledBorderText.setTitleColor(Color.decode("#5460ce"));
		bodyPanel.setBorder(titledBorderText);
		interactiveRightPanel.add(bodyPanel,c);

		interactiveRightPanel.revalidate();
		return interactiveRightPanel;
	}

	/**
	 * Metodo che crea e restituisce pannello di lettura email.
	 * */
	@Override
	public JPanel readEmailPanel(Email selectedEmail) {
		interactiveRightPanel.removeAll();
		interactiveRightPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		JPanel headerPanel = new JPanel(new GridBagLayout());
		receivedEmailText.setColumns(1);
		receivedEmailText.setRows(30);
		receivedEmailText.setEditable(false);
        receivedEmailText.setLineWrap(true);
        receivedEmailText.setWrapStyleWord(true);
		headerPanel.add(new JLabel(),c);
		c.gridy++;
		headerPanel.add(receivedEmailSender,c);
		c.gridy++;
		headerPanel.add(receivedEmailDest,c);
		c.gridy++;
        headerPanel.add(receivedEmailCc,c);
        c.gridy++;
		headerPanel.add(receivedEmailSubject,c);
		c.gridy++;
        headerPanel.add(receivedEmailDate,c);
        c.gridy++;

		JPanel bodyPanel = new JPanel(new GridBagLayout());
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		receivedEmailText.setCaretPosition(0);
		receivedEmailText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JScrollPane newMailTextScrollPane = new JScrollPane(receivedEmailText);
		newMailTextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		bodyPanel.add(newMailTextScrollPane ,c);
		c.weighty = 0;
		c.gridy=0;

		TitledBorder titledBorderData = BorderFactory.createTitledBorder("Dati");
		titledBorderData.setTitleColor(Color.decode("#5460ce"));
		headerPanel.setBorder(titledBorderData);
		c.insets = new Insets(5,5,5,5);
		interactiveRightPanel.add(headerPanel,c);
		c.gridy=1;
		c.weighty = 1;
		TitledBorder titledBorderText = BorderFactory.createTitledBorder("Testo messaggio");
		titledBorderText.setTitleColor(Color.decode("#5460ce"));
		bodyPanel.setBorder(titledBorderText);
		interactiveRightPanel.add(bodyPanel,c);

		JPanel footerPanel = new JPanel(new GridBagLayout());
		c.gridy=2;
		c.weighty = 0;
		JButton replyBtn = new JButton("Rispondi alla email");
		replyBtn.setFont(new Font("Helvetica", Font.BOLD, 13));
		replyBtn.setName("replyEmailBtn");
		replyBtn.addActionListener(clientEmailCtrl);
		footerPanel.add(replyBtn);
		if(selectedEmail.getDestEmail().size()>1){
            JButton replyAllBtn = new JButton("Rispondi a tutti");
            replyAllBtn.setFont(new Font("Helvetica", Font.BOLD, 13));
            replyAllBtn.setName("replyAllEmailBtn");
            replyAllBtn.addActionListener(clientEmailCtrl);
            footerPanel.add(replyAllBtn);
		}
		interactiveRightPanel.add(footerPanel,c);

		interactiveRightPanel.revalidate();
		return interactiveRightPanel;
	}
}

/**
 * Classe renderer personalizzata per la cella della lista JList contenente le email.
 * L'interfaccia ListCellRenderer permette di identificare i component da utilizzare come
 * 'stampino' per il paint della cella nella lista. In questo caso ogni cella è rappresentata da
 * una JLabel, il cui testo identifica mittente e oggetto della mail ricevuta, mentre l'icona permette di distinguere una mail non
 * letta (email non aperta) da una email letta.
 * */
class MyListCellRenderer extends JLabel implements ListCellRenderer<Email> {

	public MyListCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Email> list, Email value,int index, boolean isSelected, boolean cellHasFocus) {

		setFont(new Font("Helvetica", Font.PLAIN, 14));
		String mitt = value.getMittEmail();
		String arg = value.getArgEmail();
		boolean isRead = value.isRead();
		String labelText = "<html><b>Mittente: </b>" + mitt + "<br/><b>Oggetto:</b> " + arg+"</html>";
		setText(labelText);

		ImageIcon imageIcon;
		if (!isRead) {
			imageIcon = new ImageIcon(getClass().getResource("/newMail.png"));
		} else {
			imageIcon = new ImageIcon(getClass().getResource("/openedEmail.png"));
		}
		setIcon(imageIcon);
		
		if (isSelected) {
			setBackground(list.getSelectionBackground());
		} else {
            setBackground(list.getBackground());
		}
		setBorder(BorderFactory.createLineBorder(Color.decode("#e1e8ef"),1,false));
		return this;
	}
}