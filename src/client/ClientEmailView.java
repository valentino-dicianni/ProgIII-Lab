package client;

import commonResources.Email;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.*;


//Interfaccia della vista
interface ClientEmailInterfaceView {
	void updateClientGUI(Email c);
	JPanel newEmailPanel(ArrayList textEmail);
	JPanel readEmailPanel();
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
		c.gridy = 1; //1 perchè è sotto la topbar
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.SOUTHEAST;
		add(defaultRightPanel(),c);
	}

	private JPanel TopLeftPanel(){
		JPanel topLeftPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel headerLabel = new JLabel("Posta in arrivo");
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
        c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		topLeftPanel.add(headerLabel,c);
		return topLeftPanel;
	}

	/* METODO PER VISUALIZZAZIONE PANNELLO DEFAULT TOPBAR, CON PULSANTI [PROVVISORIO]*/


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

			//todo capire eventualmente come fare per mettere l'actionlistener nel controller mandando pure i valori
			sendMailButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (clientEmailCtrl.newEmail(newMailDest.getText(), newEmailSubject.getText(), newEmailText.getText())){
							newEmailSubject.setText("");
							newMailDest.setText("");
							newEmailText.setText("");
						}
						else{
							System.out.println("inserire un indirizzo mail corretto");
							JOptionPane.showMessageDialog(null, "ATTENZIONE: indirizzo mail errato", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
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
        //scrollPane.setLayout(new GridBagLayout());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		defaultLeftPanel.add(scrollPane,c);
		//defaultLeftPanel.setPreferredSize(new Dimension(600,1));
		return defaultLeftPanel;
	}

	/**
	 * Metodo per visualizzazione pannello default destro */
	private JPanel defaultRightPanel() {
		interactiveRightPanel = new JPanel(new GridBagLayout());
		//interactiveRightPanel.setBackground(Color.BLUE);
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
		JLabel label = new JLabel("Bentornato");
		interactiveRightPanel.add(label,c);
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
			this.add(readEmailPanel(),c);
			this.add(TopRightPanel(true,false), topRightPanelConst);
			this.repaint();
		}
		else if(arg1 == "updateMailList"){
            clientEmailList.setModel(((ClientEmailModel)arg0).getMailList());
            clientEmailList.addMouseListener(clientEmailCtrl);
          	clientEmailList.setFixedCellHeight(75);
			clientEmailList.setCellRenderer(new MyListCellRenderer());
			clientEmailList.setFixedCellWidth(400);

            //Questo listener può rimanere in quanto ha funzioni puramente 'estetiche' ma non influenza in alcun modo il modello
            clientEmailList.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    //Point p = null;
                    JList theList = (JList) e.getSource();
                    //ListModel model = theList.getModel();
                    int index = theList.locationToIndex(e.getPoint());
                    if (index > -1) {
                        //theList.setToolTipText(null);
                        theList.setSelectedIndex(index);
                    }
                }
            });
		}
	}

	/**
	 * Metodo avente compito di aggiornamento lista e variabili in seguito ad apertura email dalla lista*/
	@Override
	public void updateClientGUI(Email selectedEmail) {
		clientEmailList.setCellRenderer(new MyListCellRenderer());
		receivedEmailSender.setText("DA: "+selectedEmail.getMittEmail());
		receivedEmailDest.setText("A: "+selectedEmail.getDestEmail());
		receivedEmailSubject.setText("OGGETTO: "+selectedEmail.getArgEmail());
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataSped = format.format(selectedEmail.getDataSpedEmail());
		receivedEmailDate.setText("DATA INVIO: "+dataSped);
		receivedEmailText.setText(selectedEmail.getTestoEmail());
	}

	/**
	 * Metodo avente compito di visualizzazione pannello di creazione email*/
	@Override
	public JPanel newEmailPanel(ArrayList optEmailData) {
        interactiveRightPanel.removeAll();
        if(optEmailData != null && optEmailData.get(0).toString() =="frwd"){
        	newEmailText.setText("\n\n--------MESSAGGIO INOLTRATO--------" +
			"\n\nDA: <"+optEmailData.get(1).toString()+">"+
			"\nA: <" +optEmailData.get(2).toString()+">"+
			"\nOGGETTO: "+optEmailData.get(3)+
			"\nCc: \n"+optEmailData.get(4)+
			"\n\n ------------------------------");
            newEmailText.getCaret().setVisible(true);
            newEmailText.setCaretPosition(0);
			newEmailSubject.setText("");
			newMailDest.setText("");
		}
		else if(optEmailData != null && optEmailData.get(0).toString() =="reply"){
        	newMailDest.setText(optEmailData.get(1).toString());
        	newEmailSubject.setText("RE: "+optEmailData.get(3).toString());
			newEmailText.setText("\n\n--------IIn risposta a--------" +
					"\n\nDA: <"+optEmailData.get(1).toString()+">"+
					"\nCc: \n"+optEmailData.get(4)+
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
		headerPanel.add(new JLabel("A:"),c);
		c.gridy++;
		headerPanel.add(newMailDest,c);
		c.gridy++;
		headerPanel.add(new JLabel("Oggetto:"),c);
		c.gridy++;
		headerPanel.add(newEmailSubject,c);
		c.gridy++;

		JPanel bodyPanel = new JPanel(new GridBagLayout());
		c.weighty = 1;
		newEmailText.setLineWrap(true);
		newEmailText.setWrapStyleWord(true);
		c.fill = GridBagConstraints.BOTH;
		bodyPanel.add(newEmailText,c);
		c.weighty = 0;
		c.gridy=0;

		headerPanel.setBorder(BorderFactory.createTitledBorder("Invia Nuova Mail"));
		c.insets = new Insets(5,5,5,5);
		interactiveRightPanel.add(headerPanel,c);
		c.gridy=1;
		c.weighty = 1;
		bodyPanel.setBorder(BorderFactory.createTitledBorder("Messaggio"));
		interactiveRightPanel.add(bodyPanel,c);

		interactiveRightPanel.revalidate();
		return interactiveRightPanel;
	}

	/**
	 * Metodo avente compito di creazione pannello di visualizzione email ricevuta*/
	@Override
	public JPanel readEmailPanel() {
		interactiveRightPanel.removeAll();
		interactiveRightPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		JPanel headerPanel = new JPanel(new GridBagLayout());
		receivedEmailText.setPreferredSize(new Dimension(1,1));
		receivedEmailText.setColumns(1);
		receivedEmailText.setEditable(false);
        receivedEmailText.setLineWrap(true);
        receivedEmailText.setWrapStyleWord(true);
		headerPanel.add(new JLabel(),c);
		c.gridy++;
		headerPanel.add(receivedEmailSender,c);
		c.gridy++;
		headerPanel.add(receivedEmailDest,c);
		c.gridy++;
		headerPanel.add(receivedEmailSubject,c);
		c.gridy++;
        headerPanel.add(receivedEmailDate,c);
        c.gridy++;

		JPanel bodyPanel = new JPanel(new GridBagLayout());
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		bodyPanel.add(receivedEmailText,c);
		c.weighty = 0;
		c.gridy=0;

		headerPanel.setBorder(BorderFactory.createTitledBorder("Dati"));
		c.insets = new Insets(5,5,5,5);
		interactiveRightPanel.add(headerPanel,c);
		c.gridy=1;
		c.weighty = 1;
		bodyPanel.setBorder(BorderFactory.createTitledBorder("Testo messaggio"));
		interactiveRightPanel.add(bodyPanel,c);

		JPanel footerPanel = new JPanel(new GridBagLayout());
		c.gridy=2;
		c.weighty = 0;
		JButton replyBtn = new JButton("Rispondi alla email"); //TODO Il replyAll da implementare cin seguito ome dialog di scelta.
		replyBtn.setName("replyEmailBtn");
		replyBtn.addActionListener(clientEmailCtrl);
		footerPanel.add(replyBtn);
		//JButton replyAllBtn = new JButton("Reply All");
		//footerPanel.add(replyAllBtn);
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
		String mitt = label.getMittEmail();
		String arg = label.getArgEmail();
		//String testo = label.getTestoEmail();
		boolean isRead = label.isRead();
	//	int prior = label.getPriorEmail();
		String labelText = "<html>Mittente: " + mitt + "<br/>Oggetto: " + arg+"</html>";
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
