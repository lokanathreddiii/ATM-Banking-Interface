package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class MiniStatement extends JFrame implements ActionListener {

    JButton b1;
    JLabel l1;
    String pin;

    MiniStatement(String pin) {
        super("Mini Statement");
        this.pin = pin;
        getContentPane().setBackground(Color.WHITE);
        setSize(800, 800); // Adjusted window size
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(null);

        l1 = new JLabel();
        l1.setBounds(20, 140, 760, 500); // Adjusted bounds to fit the content in the new window size
        add(l1);

        JLabel l2 = new JLabel("Indian Bank");
        l2.setBounds(320, 20, 200, 20); // Centered the label
        l2.setFont(new Font("System", Font.BOLD, 16)); // Added font style
        add(l2);

        JLabel l3 = new JLabel();
        l3.setBounds(20, 80, 760, 20); // Adjusted width to fit new window size
        add(l3);

        try {
            // Retrieve and display card number
            Conn c = new Conn();
            String query = "SELECT cardno FROM login WHERE pin = ?";
            PreparedStatement pstmt = c.conn.prepareStatement(query);
            pstmt.setString(1, pin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String cardNo = rs.getString("cardno");
                l3.setText("Card Number:    " + cardNo.substring(0, 4) + "XXXXXXXX" + cardNo.substring(12));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Retrieve and display transaction history
            Conn c1 = new Conn();
            String query = "SELECT * FROM bank WHERE pin = ? ORDER BY date DESC";
            PreparedStatement pstmt = c1.conn.prepareStatement(query);
            pstmt.setString(1, pin);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String date = rs.getString("date");
                String mode = rs.getString("type"); // Changed from "mode" to "type" to match your table structure
                String amount = rs.getString("amount");
                sb.append(String.format("<html>%s &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;%s &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;%s<br><br><html>", date, mode, amount));
            }
            l1.setText(sb.toString());

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        b1 = new JButton("Exit");
        b1.setBounds(350, 700, 100, 25); // Centered exit button
        add(b1);

        b1.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        this.setVisible(false);
    }

    public static void main(String[] args) {
        new MiniStatement("").setVisible(true);
    }
}
