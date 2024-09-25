package com.mycompany.phoneaccessories;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransactionMenu extends JFrame {
    private JButton payButton;
    private JButton refundButton;
    private JButton backButton;
    private JLabel titleLabel;
    private String staffId;
    private String staffName;

    public TransactionMenu(String staffID, String staffName) {
        this.staffId = staffID;
        this.staffName = staffName;

      
        setTitle("Transaction Menu");


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
       
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        
     
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); 

        
        titleLabel = new JLabel("Transaction Menu");
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

  
        payButton = createButton("Pay to Supplier");
        refundButton = createButton("Refund to Customer");
        backButton = createButton("Back to Staff Menu");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.anchor = GridBagConstraints.CENTER; 

       
        add(titleLabel, gbc);

        gbc.gridy++; 
        add(payButton, gbc);

        gbc.gridy++;
        add(refundButton, gbc);

        gbc.gridy++;
        add(backButton, gbc);

        getContentPane().setBackground(Color.BLACK);
        
    
        setLocationRelativeTo(null);
        
     
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SupplierPayment(staffID,staffName);
                dispose();
            }
        });

        refundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CustomerRefund(staffID,staffName);
                dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StaffMenu(staffId,staffName).setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }

    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFocusable(false); 
        return button;
    }
}
