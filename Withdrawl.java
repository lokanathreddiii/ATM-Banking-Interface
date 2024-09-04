package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

public class Withdrawl extends JFrame implements ActionListener {

    JTextField t1;
    JButton b1, b2;
    JLabel l1, l2;
    String pin;

    Withdrawl(String pin) {
        this.pin = pin;

        // Set the background image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(800, 800, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 800, 800);
        add(l3);

        // Labels for withdrawal information
        l1 = new JLabel("MAXIMUM WITHDRAWAL IS RS.10,000");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        l2 = new JLabel("PLEASE ENTER YOUR AMOUNT");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("System", Font.BOLD, 16));

        // Text field for withdrawal amount
        t1 = new JTextField();
        t1.setFont(new Font("Raleway", Font.BOLD, 22));

        // Buttons for withdrawal and going back
        b1 = new JButton("WITHDRAW");
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);

        b2 = new JButton("BACK");
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);

        setLayout(null);

        // Adjusting positions and sizes to fit the new window size
        l1.setBounds(150, 300, 500, 35); // Adjusted label position
        l3.add(l1);

        l2.setBounds(150, 350, 500, 35); // Adjusted label position
        l3.add(l2);

        t1.setBounds(150, 400, 500, 35); // Adjusted text field position
        l3.add(t1);

        b1.setBounds(200, 450, 150, 35); // Adjusted withdraw button position
        l3.add(b1);

        b2.setBounds(400, 450, 150, 35); // Adjusted back button position
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
        try {
            String amountStr = t1.getText();
            if (amountStr.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter the Amount you want to Withdraw");
                return;
            }

            int amount = Integer.parseInt(amountStr);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);

            Conn c1 = new Conn();
            // Calculate the balance
            String query = "SELECT * FROM bank WHERE pin = ?";
            PreparedStatement pstmt = c1.conn.prepareStatement(query);
            pstmt.setString(1, pin);
            ResultSet rs = pstmt.executeQuery();

            int balance = 0;
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

            // Check if sufficient balance is available
            if (balance < amount) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance");
                c1.close();
                return;
            }

            // Perform the withdrawal
            query = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)";
            pstmt = c1.conn.prepareStatement(query);
            pstmt.setString(1, pin);
            pstmt.setString(2, formattedDate);
            pstmt.setString(3, "Withdrawl");
            pstmt.setInt(4, amount); // Use setInt for integer values

            pstmt.executeUpdate();
            pstmt.close();
            c1.close(); // Ensure resources are closed

            JOptionPane.showMessageDialog(null, "Rs. " + amount + " Debited Successfully");

            setVisible(false);
            new Transactions(pin).setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount format. Please enter a valid number.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Withdrawl("").setVisible(true);
    }
}
