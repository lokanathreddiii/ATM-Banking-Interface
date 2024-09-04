package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FastCash extends JFrame implements ActionListener {

    JLabel l1;
    JButton b1, b2, b3, b4, b5, b6, b7;
    String pin;

    FastCash(String pin) {
        this.pin = pin;

        // Set the background image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(800, 800, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 800, 800);
        add(l3);

        // Label for instruction
        l1 = new JLabel("SELECT WITHDRAWAL AMOUNT");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        // Buttons for withdrawal amounts and back option
        b1 = new JButton("Rs 100");
        b2 = new JButton("Rs 500");
        b3 = new JButton("Rs 1000");
        b4 = new JButton("Rs 2000");
        b5 = new JButton("Rs 5000");
        b6 = new JButton("Rs 10000");
        b7 = new JButton("BACK");

        setLayout(null);

        // Adjusting positions and sizes to fit the new window size
        l1.setBounds(200, 300, 400, 35); // Adjusted label position
        l3.add(l1);

        b1.setBounds(150, 400, 150, 35); // Adjusted button positions
        l3.add(b1);

        b2.setBounds(350, 400, 150, 35);
        l3.add(b2);

        b3.setBounds(150, 450, 150, 35);
        l3.add(b3);

        b4.setBounds(350, 450, 150, 35);
        l3.add(b4);

        b5.setBounds(150, 500, 150, 35);
        l3.add(b5);

        b6.setBounds(350, 500, 150, 35);
        l3.add(b6);

        b7.setBounds(350, 550, 150, 35); // Adjusted back button position
        l3.add(b7);

        // Adding action listeners
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
        try {
            String amountStr = ((JButton) ae.getSource()).getText().substring(3); // Extract amount from button text
            int amount = Integer.parseInt(amountStr);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);

            Conn c = new Conn();
            // Calculate the balance
            String query = "SELECT * FROM bank WHERE pin = ?";
            PreparedStatement pstmt = c.conn.prepareStatement(query);
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
            if (ae.getSource() != b7 && balance < amount) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance");
                c.close();
                return;
            }

            // Handle button actions
            if (ae.getSource() == b7) {
                setVisible(false);
                new Transactions(pin).setVisible(true);
            } else {
                // Perform the withdrawal
                query = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)";
                pstmt = c.conn.prepareStatement(query);
                pstmt.setString(1, pin);
                pstmt.setString(2, formattedDate);
                pstmt.setString(3, "Withdrawl");
                pstmt.setInt(4, amount); // Use setInt for integer values

                pstmt.executeUpdate();
                pstmt.close();
                c.close(); // Ensure resources are closed

                JOptionPane.showMessageDialog(null, "Rs. " + amount + " Debited Successfully");

                setVisible(false);
                new Transactions(pin).setVisible(true);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount format. Please enter a valid number.");
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FastCash("").setVisible(true);
    }
}
