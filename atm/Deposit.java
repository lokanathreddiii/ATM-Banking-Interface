package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

public class Deposit extends JFrame implements ActionListener {

    JTextField t1;
    JButton b1, b2;
    JLabel l1;
    String pin;

    Deposit(String pin) {
        this.pin = pin;

        // Set the background image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(800, 800, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 800, 800);
        add(l3);

        // Label for entering deposit amount
        l1 = new JLabel("ENTER AMOUNT YOU WANT TO DEPOSIT");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        // Text field for deposit amount
        t1 = new JTextField();
        t1.setFont(new Font("Raleway", Font.BOLD, 22));

        // Buttons for depositing and going back
        b1 = new JButton("DEPOSIT");
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);

        b2 = new JButton("BACK");
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);

        setLayout(null);

        // Adjusting positions and sizes to fit the new window size
        l1.setBounds(150, 300, 500, 35); // Adjusted label position
        l3.add(l1);

        t1.setBounds(150, 350,150,50); // Adjusted text field position
        l3.add(t1);

        b1.setBounds(150, 450, 150, 35); // Adjusted deposit button position
        l3.add(b1);

        b2.setBounds(300, 450, 150, 35); // Adjusted back button position
        l3.add(b2);

        b1.addActionListener(this);
        b2.addActionListener(this);

        // Adjust window size and position
        setSize(800, 800); // New window size
        setLocationRelativeTo(null); // Center the window on screen
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String amount = t1.getText();
        if (amount.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the Amount you want to Deposit");
            return;
        }

        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);

            Conn c1 = new Conn();
            String query = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = c1.conn.prepareStatement(query);

            pstmt.setString(1, pin);
            pstmt.setString(2, formattedDate);
            pstmt.setString(3, "Deposit");
            pstmt.setString(4, amount);

            pstmt.executeUpdate();
            pstmt.close();
            c1.close(); // Ensure resources are closed

            JOptionPane.showMessageDialog(null, "Rs. " + amount + " Deposited Successfully");

            setVisible(false);
            new Transactions(pin).setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Deposit("").setVisible(true);
    }
}
