package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

class BalanceEnquiry extends JFrame implements ActionListener {

    JLabel l1;
    JButton b1;
    String pin;

    BalanceEnquiry(String pin) {
        this.pin = pin;

        // Setting up the background image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(800, 600, Image.SCALE_DEFAULT); // Updated dimensions
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 800, 600); // Updated dimensions
        add(l3);

        // Setting up the balance label
        l1 = new JLabel();
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));
        l1.setBounds(50, 200, 700, 35); // Adjusted position for new size
        l3.add(l1);

        // Setting up the back button
        b1 = new JButton("BACK");
        b1.setBounds(325, 400, 150, 35); // Adjusted position for new size
        l3.add(b1);

        b1.addActionListener(this);

        // Calculate the balance
        int balance = 0;
        try {
            Conn c1 = new Conn();
            String query = "SELECT * FROM bank WHERE pin = ?";
            PreparedStatement pstmt = c1.conn.prepareStatement(query);
            pstmt.setString(1, pin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                String amt = rs.getString("amount");
                if (type != null && amt != null) {
                    if (type.equals("Deposit")) {
                        balance += Integer.parseInt(amt);
                    } else if (type.equals("Withdrawl")) {
                        balance -= Integer.parseInt(amt);
                    }
                }
            }

            rs.close();
            pstmt.close();
            c1.close(); // Ensure resources are closed

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        l1.setText("Your Current Account Balance is Rs " + balance);

        // Frame settings
        setSize(800, 600); // Updated dimensions
        setUndecorated(true);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Transactions(pin).setVisible(true);
    }

    public static void main(String[] args) {
        new BalanceEnquiry("").setVisible(true);
    }
}
