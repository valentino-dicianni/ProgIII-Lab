package progettoEmail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;


//Interfaccia della vista
interface ClientEmailInterfaceView {
	void updateEmailListGUI(Email c);
	JPanel newEmailPanel(GridBagConstraints c);
	JPanel readEmailPanel(GridBagConstraints c);
}


public class ClientEmailView extends JPanel implements ClientEmailInterfaceView, Observer {	
	private ClientEmailController clientEmailCtrl;	
	private JPanel interactiveRightPanel;
	
	//email list panel
	private JList<Email> list =  new JList<Email>();
	
	//New email form panel	
	private NewEmailPanel newEmailPanel = new NewEmailPanel();

	//read email panel
    private ReadEmailPanel readEmailPanel = new ReadEmailPanel();

	public ClientEmailView(ClientEmailController clientEmailCtrl) {
		this.clientEmailCtrl = clientEmailCtrl;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setBackground(Color.GRAY);

		/* TOPBAR 
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello superiore all'interno del pannello this) */
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(defaultTopPanel(), c);
		
		/* PARTE SINISTRA 
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello di sinistra all'interno del pannello this)*/
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.3;
		c.fill = GridBagConstraints.BOTH;
		add(defaultLeftPanel(), c);
		
		/* PARTE DESTRA 
		 * (A seguire le costanti necessarie per il corretto posizionamento del pannello di destra all'interno del pannello this)*/
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(defaultRightPanel(), c);
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
		c.gridx =2;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1; 	
		c.fill = GridBagConstraints.HORIZONTAL;
		//c.anchor = GridBagConstraints.CENTER;
		newMailBtn.setName("newMailBtn");
		forwardBtn.setName("frwdBtn");
		newMailBtn.setToolTipText("<html>Nuova Email</html>");
		forwardBtn.setToolTipText("Inoltra");
		defaultTopPanel.add(newMailBtn,c);
		c.gridx =1;
		defaultTopPanel.add(forwardBtn);
		newMailBtn.addActionListener(clientEmailCtrl);
		return defaultTopPanel;		
	}

	/* METODO PER VISUALIZZAZIONE PANNELLO DEFAULT SINISTRO, CON LISTA EMAIL [PROVVISORIO]*/
	private JPanel defaultLeftPanel() {		
		JPanel defaultLeftPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); 
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;		
		c.gridx=1;
		c.gridy=1;
		c.weighty =1;
		c.weightx = 1;

		defaultLeftPanel.add(new JScrollPane(list),c);		
		return defaultLeftPanel;		
	}
	
	/* METODO PER VISUALIZZAZIONE PANNELLO DEFAULT DESTRO, AVENTE FUNZIONI MULTIPLE (DEFAULT: VUOTO//LABEL SEMPLICE) [PROVVISORIO]*/
	private JPanel defaultRightPanel() {
		interactiveRightPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); 
		JLabel label = new JLabel("Benvenuto su PigMail");		
		interactiveRightPanel.add(label);
		interactiveRightPanel.revalidate();
		return interactiveRightPanel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
	
		GridBagConstraints c = new GridBagConstraints(); 
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;		
		c.fill = GridBagConstraints.BOTH;
		
		/* Caso in cui si crea una nuova email: viene mostrato panel di creazione email */
		if(arg1=="newEmailForm"){	
			this.remove(interactiveRightPanel);
			this.revalidate();
			this.add(newEmailPanel(c),c);
			this.repaint();
		}
		/* Caso in cui un email viene selezionata dalla lista: viene mostrato panel di visualizzazione email */
		else if(arg1 instanceof Email){			
			updateEmailListGUI((Email)arg1);			
			this.remove(interactiveRightPanel);
			this.revalidate();
			this.add(readEmailPanel(c),c);
			this.repaint();
		}
		else if(arg1 == "updateMailList"){

            list.setModel(((ClientEmailModel)arg0).getList());
            list.setCellRenderer(new MyListCellRenderer());
            list.addMouseListener(clientEmailCtrl);

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

		readEmailPanel.getFromLabel().setText(selectedEmail.getMittEmail());
		readEmailPanel.getToLabel().setText(selectedEmail.getDestEmail());
		readEmailPanel.getSubjectLabel().setText(selectedEmail.getArgEmail());
		readEmailPanel.getTxtLabel().setText(selectedEmail.getTestoEmail());
	}

	@Override
	public JPanel newEmailPanel(GridBagConstraints c) {		
		interactiveRightPanel.removeAll();
		JPanel formPanel = new JPanel(new BorderLayout());
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(6, 2));
		headerPanel.add(new JLabel("Da:"));
		headerPanel.add(newEmailPanel.getFromField());

		headerPanel.add(new JLabel("A:"));
		headerPanel.add(newEmailPanel.getToField());

		headerPanel.add(new JLabel("Oggetto:"));
		headerPanel.add(newEmailPanel.getSubjectField());

		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BorderLayout());
		bodyPanel.add(new JLabel("Messaggio:"), BorderLayout.NORTH);
		bodyPanel.add(newEmailPanel.getContentTextArea(), BorderLayout.CENTER);

		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new BorderLayout());
		
		JButton sendMailButton = new JButton("Invia email");
		footerPanel.add(sendMailButton, BorderLayout.SOUTH);

		formPanel.add(headerPanel, BorderLayout.NORTH);
		formPanel.add(bodyPanel, BorderLayout.CENTER);
		formPanel.add(footerPanel, BorderLayout.SOUTH);

		interactiveRightPanel.add(formPanel,c);
		interactiveRightPanel.revalidate();
		return interactiveRightPanel;				
	}

	@Override
	public JPanel readEmailPanel(GridBagConstraints c) {	
		
		interactiveRightPanel.removeAll();		
		JPanel readPanel = new JPanel(new BorderLayout());		
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(6, 2));
		headerPanel.add(new JLabel("Da:"));
		headerPanel.add(readEmailPanel);

		headerPanel.add(new JLabel("A:"));
		headerPanel.add(readEmailPanel.getToLabel());

		headerPanel.add(new JLabel("Oggetto:"));
		headerPanel.add(readEmailPanel.getSubjectLabel());

		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BorderLayout());
		bodyPanel.add(new JLabel("Messaggio:"), BorderLayout.NORTH);
		bodyPanel.add(readEmailPanel.getTxtLabel(), BorderLayout.CENTER);

		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new BorderLayout());
		
		JButton sendMailButton = new JButton("Invia email");
		footerPanel.add(sendMailButton, BorderLayout.SOUTH);

		readPanel.add(headerPanel, BorderLayout.NORTH);
		readPanel.add(bodyPanel, BorderLayout.CENTER);
		readPanel.add(footerPanel, BorderLayout.SOUTH);
		
		interactiveRightPanel.add(readPanel,c);
		interactiveRightPanel.revalidate();
		return interactiveRightPanel;		
	}
}


//Classe di visualizzazione della lista di email
class MyListCellRenderer extends JLabel implements ListCellRenderer<Email> {

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
