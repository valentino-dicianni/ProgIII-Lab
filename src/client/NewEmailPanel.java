package client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by valentino on 01/05/17.
 */
public class NewEmailPanel extends JPanel {
    private JTextField fromField = new JTextField();
    private JTextField toField = new JTextField();
    private JTextField subjectField = new JTextField();
    private JTextArea contentTextArea = new JTextArea();


    public JTextField getFromField() {
        return fromField;
    }

    public void setFromField(JTextField fromField) {
        this.fromField = fromField;
    }

    public JTextField getToField() {
        return toField;
    }

    public void setToField(JTextField toField) {
        this.toField = toField;
    }

    public JTextField getSubjectField() {
        return subjectField;
    }

    public void setSubjectField(JTextField subjectField) {
        this.subjectField = subjectField;
    }

    public JTextArea getContentTextArea() {
        contentTextArea.setLineWrap(true);
        contentTextArea.setWrapStyleWord(true);
        contentTextArea.setPreferredSize(new Dimension(1,1));
        return contentTextArea;
    }

    public void setContentTextArea(JTextArea contentTextArea) {
        this.contentTextArea = contentTextArea;
    }
}
