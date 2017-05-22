package client;

import commonResources.Email;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Caret;


//Interfaccia della vista
interface ClientEmailInterfaceView {
	void updateEmailListGUI(Email c);
	JPanel newEmailPanel(ArrayList textEmail);
	JPanel readEmailPanel();
}


public class ClientEmailView extends JPanel implements ClientEmailInterfaceView, Observer {	
	private ClientEmailController clientEmailCtrl;	
	private JPanel interactiveRightPanel;
	private JPanel interactiveTopPanel;
	private GridBagConstraints topRightPanelConst;

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

		/* TOPLEFTBAR
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello superiore all'interno del pannello this) */
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx =1;
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
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.SOUTHWEST;
		add(defaultLeftPanel(), c);
		
		/* PARTE DESTRA 
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello di destra all'interno del pannello this)*/
		c.gridx = 1;
		c.gridy = 1; //1 perchè è sotto la topbar
		c.weightx = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.SOUTHEAST;
		add(defaultRightPanel(),c);
	}

	private JPanel TopLeftPanel(){
		JPanel topLeftPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel headerLabel = new JLabel("Posta in arrivo");
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.ipady = 20;
		topLeftPanel.add(headerLabel,c);
		return topLeftPanel;
	}

	/* METODO PER VISUALIZZAZIONE PANNELLO DEFAULT TOPBAR, CON PULSANTI [PROVVISORIO]*/


	private JPanel TopRightPanel(boolean showEmailRelatedOptions,boolean hideButtons) {
		if(showEmailRelatedOptions)
			interactiveTopPanel.removeAll();
		//interactiveRightPanel.setLayout(new GridBagLayout());
		interactiveTopPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		if(!hideButtons) {
			JButton newMailBtn = new JButton();
			Image img = null;
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
			if (showEmailRelatedOptions) {

			    //PULSANTE FORWARD EMAIL
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

                //PULSANTE ELIMINAZIONE MAIL
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
                c.weightx = 0.1;
                deleteBtn.addActionListener(clientEmailCtrl);
                interactiveTopPanel.add(deleteBtn, c);
			}

		}
		else{
			JButton sendMailButton = new JButton("");

			Image img = null;
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
						clientEmailCtrl.newEmail(toField.getText(),subjectField.getText(),contentTextArea.getText());
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			});

			interactiveTopPanel.add(sendMailButton,c);
		}
		return interactiveTopPanel;
	}

	/* METODO PER VISUALIZZAZIONE PANNELLO DEFAULT SINISTRO, CON LISTA EMAIL [PROVVISORIO]*/
	private JPanel defaultLeftPanel() {		
		JPanel defaultLeftPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
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
			this.add(TopRightPanel(true,true), topRightPanelConst);
			this.repaint();
		}
		else if(arg1 instanceof ArrayList){
			this.remove(interactiveTopPanel);
			this.remove(interactiveRightPanel);
			this.revalidate();
			ArrayList frwdEmailData = ((ArrayList) arg1);
			//String textEmail = frwdEmailData.get(1).toString();
			this.add(newEmailPanel(frwdEmailData),c);
			this.add(TopRightPanel(true,true), topRightPanelConst);
			this.repaint();
		}
		/* Caso in cui un email viene selezionata dalla lista: viene mostrato panel di visualizzazione email */
		else if(arg1 instanceof Email){			
			updateEmailListGUI((Email)arg1);
			this.remove(interactiveTopPanel);
			this.remove(interactiveRightPanel);
			this.revalidate();
			this.add(readEmailPanel(),c);
			this.add(TopRightPanel(true,false), topRightPanelConst);
			this.repaint();
		}
		else if(arg1 == "updateMailList"){
            list.setModel(((ClientEmailModel)arg0).getMailList());
            list.setCellRenderer(new MyListCellRenderer());
            list.addMouseListener(clientEmailCtrl);
            list.setFixedCellHeight(75);
            list.setFixedCellWidth(100);

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
	public JPanel newEmailPanel(ArrayList frwdEmailData) {
        interactiveRightPanel.removeAll();
        if(frwdEmailData != null){
        	contentTextArea.setText("\n\n----MESSAGGIO INOLTRATO---" +
			"\n\nDA: <"+frwdEmailData.get(0).toString()+">"+
			"\nA: <" +frwdEmailData.get(1).toString()+">"+
			"\nOGGETTO: "+frwdEmailData.get(2)+
			"\nCc: \n"+frwdEmailData.get(3)+
			"\n\n ------------------------------");
            contentTextArea.getCaret().setVisible(true);
            contentTextArea.setCaretPosition(0);
		}
		else {
			contentTextArea.setText("");
		}
		subjectField.setText("");
		toField.setText("");
        interactiveRightPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		JPanel headerPanel = new JPanel(new GridBagLayout());
		headerPanel.add(new JLabel("A:"),c);
		c.gridy++;
		headerPanel.add(toField,c);
		c.gridy++;
		headerPanel.add(new JLabel("Oggetto:"),c);
		c.gridy++;
		headerPanel.add(subjectField,c);
		c.gridy++;

		JPanel bodyPanel = new JPanel(new GridBagLayout());
		c.weighty = 1;
		contentTextArea.setLineWrap(true);
		contentTextArea.setWrapStyleWord(true);
		contentTextArea.setPreferredSize(new Dimension(1,1));
		c.fill = GridBagConstraints.BOTH;
		bodyPanel.add(contentTextArea,c);
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
		fromLabel.setPreferredSize(new Dimension(1,1));
		toLabel.setPreferredSize(new Dimension(1,1));
		subjectLabel.setPreferredSize(new Dimension(1,1));
		txtLabel.setPreferredSize(new Dimension(1,1));
		headerPanel.add(new JLabel("DA: "+fromLabel.getText()),c);
		c.gridy++;
		headerPanel.add(new JLabel("A: "+toLabel.getText()),c);
		c.gridy++;
		headerPanel.add(new JLabel("OGGETTO: "+subjectLabel.getText()),c);
		c.gridy++;

		JPanel bodyPanel = new JPanel(new GridBagLayout());
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		bodyPanel.add(txtLabel,c);
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
		JButton replyBtn = new JButton("Reply");
		//JButton replyAllBtn = new JButton("Reply All");
		footerPanel.add(replyBtn);
		//footerPanel.add(replyAllBtn);
		interactiveRightPanel.add(footerPanel,c);
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
