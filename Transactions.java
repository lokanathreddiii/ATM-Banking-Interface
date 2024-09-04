package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Transactions extends JFrame implements ActionListener {

    JLabel l1;
    JButton b1, b2, b3, b4, b5, b6, b7;
    String pin;

    Transactions(String pin) {
        this.pin = pin;

        // Set the background image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(800, 800, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l2 = new JLabel(i3);
        l2.setBounds(0, 0, 800, 800);
        add(l2);

        // Transaction label
        l1 = new JLabel("Please Select Your Transaction");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        // Buttons for different transactions
        b1 = new JButton("DEPOSIT");
        b2 = new JButton("CASH WITHDRAWAL");
        b3 = new JButton("FAST CASH");
        b4 = new JButton("MINI STATEMENT");
        b5 = new JButton("PIN CHANGE");
        b6 = new JButton("BALANCE ENQUIRY");
        b7 = new JButton("EXIT");

        setLayout(null);

        // Adjust positions and sizes to fit the new window size
        l1.setBounds(200, 300, 400, 35); // Adjusted label position
        l2.add(l1);

        // Aligning the buttons more evenly
        b1.setBounds(150, 350, 150, 35);
        l2.add(b1);

        b2.setBounds(350, 350, 150, 35);
        l2.add(b2);

        b3.setBounds(150, 400, 150, 35);
        l2.add(b3);

        b4.setBounds(350, 400, 150, 35);
        l2.add(b4);

        b5.setBounds(150, 450, 150, 35);
        l2.add(b5);

        b6.setBounds(350, 450, 150, 35);
        l2.add(b6);

        b7.setBounds(250, 500, 150, 35); // Centered EXIT button
        l2.add(b7);

        // Add action listeners for buttons
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
        b7.addActionListener(this);

        // Adjust window size and position
        setSize(800, 800); // New window size
        setLocationRelativeTo(null); // Center the window on screen
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b1) {
            setVisible(false);
            new Deposit(pin).setVisible(true);
        } else if (ae.getSource() == b2) {
            setVisible(false);
            new Withdrawl(pin).setVisible(true);
        } else if (ae.getSource() == b3) {
            setVisible(false);
            new FastCash(pin).setVisible(true);
        } else if (ae.getSource() == b4) {
            new MiniStatement(pin).setVisible(true);
        } else if (ae.getSource() == b5) {
            setVisible(false);
            new Pin(pin).setVisible(true);
        } else if (ae.getSource() == b6) {
            this.setVisible(false);
            new BalanceEnquiry(pin).setVisible(true);
        } else if (ae.getSource() == b7) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Transactions("").setVisible(true);
    }
}
