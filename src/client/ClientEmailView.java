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
	void updateClientGUI(Email selectedEmail);
	JPanel newEmailPanel(ArrayList optEmailData);
	JPanel readEmailPanel(Email selectedEmail);
}


public class ClientEmailView extends JPanel implements ClientEmailInterfaceView, Observer {
	private ClientEmailController clientEmailCtrl;
	private JPanel interactiveRightPanel;
	private JPanel interactiveTopPanel;
	private GridBagConstraints topRightPanelConst;

	//JList contenente Email
	private JList<Email> clientEmailList =  new JList<Email>();

	//new Email text vars
	private JTextField newMailDest = new JTextField();
    private JTextField newEmailSubject = new JTextField();
	private JTextArea newEmailText = new JTextArea();

	//received Email vars
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
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello superiore all'interno del pannello this) */
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx =0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		add(TopLeftPanel(), c);

		/* TOPRIGHTBAR
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello superiore all'interno del pannello this) */
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHEAST;
		add(TopRightPanel(false,false), c);
		topRightPanelConst = (GridBagConstraints) c.clone();
		setBackground(Color.GREEN);
		
		/* PARTE SINISTRA
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello di sinistra all'interno del pannello this)*/
		c.ipady=0;
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
	 * metodo per visualizzazione pannello topleft
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
	 * metodo per visualizzazione pannello topbar default, con pulsanti
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
			JButton newMailBtn = new JButton();
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
			    //FORWARD EMAIL BUTTON
				JButton forwardBtn = new JButton();
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

                //DELETE EMAIL BUTTON
                JButton deleteBtn = new JButton();
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
			JButton sendMailButton = new JButton("");
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
                        System.out.println("inserire un indirizzo mail corretto");
                        JOptionPane.showMessageDialog(null, "ATTENZIONE: indirizzo mail errato", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
                    }
				}
			});

			interactiveTopPanel.add(sendMailButton,c);
		}
		return interactiveTopPanel;
	}

	/**
	 * Metodo per visualizzazione pannello default sinistro, con lista email */
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
	 * Metodo per visualizzazione pannello default destro */
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
	 * Override metodo update, avente ruolo di cattura notifiche inviate dal model */
	@Override
	public void update(Observable arg0, Object arg1) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 2;
		c.weighty = 1;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.fill = GridBagConstraints.BOTH;
		if(arg1=="newEmailForm"){
			this.remove(interactiveTopPanel);
			this.remove(interactiveRightPanel);
			this.revalidate();
			this.add(newEmailPanel(null),c);
			this.add(TopRightPanel(false,true), topRightPanelConst);
			this.repaint();
		}
		/* Caso in cui si desidera inoltrare o rispondere ad una email: viene mostrato panel creazione nuova mail, con dati del forward/reply */
		else if(arg1 instanceof ArrayList){
			this.remove(interactiveTopPanel);
			this.remove(interactiveRightPanel);
			this.revalidate();
			ArrayList EmailData = ((ArrayList) arg1);
			this.add(newEmailPanel(EmailData),c);
			this.add(TopRightPanel(false,true), topRightPanelConst);
			this.repaint();
		}
		/* Caso in cui un email viene selezionata dalla lista: viene mostrato panel di visualizzazione email */
		else if(arg1 instanceof Email){
			updateClientGUI((Email)arg1);
			this.remove(interactiveTopPanel);
			this.remove(interactiveRightPanel);
			this.revalidate();
			this.add(readEmailPanel((Email)arg1),c);
			this.add(TopRightPanel(true,false), topRightPanelConst);
			this.repaint();
		}
		else if(arg1 == "updateMailList"){
			updateEmailList(((ClientEmailModel)arg0).getMailList());
		}
		else if(arg1 == "deleteCompleted"){
			updateEmailList(((ClientEmailModel)arg0).getMailList());
			this.remove(interactiveRightPanel);
			this.revalidate();
			add(defaultRightPanel(),c);
		}
		else if(arg1 == "newEmailReceived"){
			JOptionPane.showMessageDialog(null, "Hai ricevuto un nuovo messaggio", "Nuovo messaggio!", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void updateEmailList(DefaultListModel mailList) {
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
	 * Metodo avente compito di aggiornamento lista e variabili in seguito ad apertura email dalla lista*/
	@Override
	public void updateClientGUI(Email selectedEmail) {
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
	 * Metodo con il compito di visualizzazione pannello di creazione email*/
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
        if(optEmailData != null && optEmailData.get(0).toString() =="frwd"){
        	newEmailText.setText("\n\n--------MESSAGGIO INOLTRATO--------" +
			"\n\nDA: <"+optEmailData.get(1).toString()+">"+
			"\nA: <" +optEmailData.get(2).toString()+">"+
			"\nOGGETTO: "+optEmailData.get(3).toString()+
			"\nTESTO:\n:"+optEmailData.get(4).toString()+">"+
			"\n\n ------------------------------");
            newEmailText.getCaret().setVisible(true);
            newEmailText.setCaretPosition(0);
			newEmailSubject.setText("");
			newMailDest.setText("");
		}
		else if(optEmailData != null && optEmailData.get(0).toString() =="reply"){
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
        else if(optEmailData != null && optEmailData.get(0).toString() =="replyAll") {
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
	 * Metodo avente compito di creazione pannello di visualizzione email ricevuta*/
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
 * Classe renderer personalizzata per JList con lista email ricevute dal client*/
class MyListCellRenderer extends JLabel implements ListCellRenderer<Email> {

	public MyListCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Email> list, Email value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		Email label = value;
		setFont(new Font("Helvetica", Font.PLAIN, 14));
		String mitt = label.getMittEmail();
		String arg = label.getArgEmail();
		boolean isRead = label.isRead();
		String labelText = "<html><b>Mittente: </b>" + mitt + "<br/><b>Oggetto:</b> " + arg+"</html>";
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
			//setForeground(list.getSelectionForeground());
		} else {
            setBackground(list.getBackground());
			//setForeground(list.getForeground());
		}
		setBorder(BorderFactory.createLineBorder(Color.decode("#e1e8ef"),1,false));
		return this;
	}
}