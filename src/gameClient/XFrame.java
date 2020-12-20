package gameClient;

import javax.swing.*;
import java.awt.*;

public class XFrame extends JFrame {
    XPanel panel;
    private Arena _ar;

    public XFrame(String a){
        super(a);
        this.setTitle("Barak & Liroy");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.WHITE);

        panel =new XPanel(_ar);
        this.add(panel);
        this.setVisible(true);
    }
}
