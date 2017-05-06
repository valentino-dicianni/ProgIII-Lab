package client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by valentino on 01/05/17.
 */
public class ReadEmailPanel extends JPanel {
    private JLabel fromLabel = new JLabel();
    private JLabel toLabel = new JLabel();
    private JLabel subjectLabel = new JLabel();
    private JLabel txtLabel = new JLabel();


    public JLabel getFromLabel() {
       /* fromLabel.setPreferredSize(new Dimension(1,1));*/
        return fromLabel;
    }

    public void setFromLabel(JLabel fromLabel) {
        this.fromLabel = fromLabel;
    }

    public JLabel getToLabel() {
        /*toLabel.setPreferredSize(new Dimension(1,1));*/
        return toLabel;
    }

    public void setToLabel(JLabel toLabel) {
        this.toLabel = toLabel;
    }

    public JLabel getSubjectLabel() {
     /*   subjectLabel.setPreferredSize(new Dimension(1,1));*/
        return subjectLabel;
    }

    public void setSubjectLabel(JLabel subjectLabel) {
        this.subjectLabel = subjectLabel;
    }

    public JLabel getTxtLabel() {
        return txtLabel;
    }

    public void setTxtLabel(JLabel txtLabel) {
        this.txtLabel = txtLabel;
    }
}
