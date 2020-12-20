package gameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class XLogin extends JFrame implements ActionListener {
    JTextField textId;
    JTextField textLevel;
    JLabel _ID;
    JLabel _Level;
    JButton _button;
    int id;
    int level;
    boolean connected;

    public XLogin() {
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textId = new JTextField();
        textId.setBounds(75, 50, 150, 25);
        textLevel = new JTextField();
        textLevel.setBounds(75, 90, 150, 25);
        textId.addActionListener(this);
        this._ID = new JLabel("ID: ");
        this._ID.setBounds(50, 50, 50, 25);
        this._Level = new JLabel("Level: ");
        this._Level.setBounds(50, 90, 50, 25);
        this._button = new JButton("Start");
        this._button.setBounds(50, 150, 200, 30);
        this.add(textId);
        this.add(textLevel);
        this.add(_button);
        this.add(_ID);
        this.add(_Level);
        this.setResizable(false);
        this._button.addActionListener(this);
        this.setBounds(300, 300, 300, 300);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String _id = this.textId.getText();
        String _level = this.textLevel.getText();
        boolean validID = _id.chars().allMatch( Character::isDigit);
        boolean validLevel=_level.chars().allMatch( Character::isDigit);
        if(validID && validLevel) {
            this.id= Integer.parseInt(_id);
            this.level=Integer.parseInt(_level);
            this.connected = true;
        }
        else System.out.println("Invalid value, Please enter only numbers");
    }

    public int getId() {
        return this.id;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean connected() {
        return this.connected;
    }




}