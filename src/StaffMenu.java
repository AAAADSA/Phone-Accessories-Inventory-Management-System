package com.mycompany.phoneaccessories;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StaffMenu extends JFrame {

    private final JPanel mainPanel;
    private final String staffID;
    private final String staffName;

 
    public StaffMenu(String staffID,String staffName) {
        this.staffID = staffID;
        this.staffName =staffName;

     
        setTitle("Staff Menu");
     
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    
  
    int width = (int) (screenSize.width * 0.9);
    int height = (int) (screenSize.height * 0.9);
    
    
    setSize(width, height);
    setLocationRelativeTo(null); 

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   
    mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(Color.BLACK);
    setContentPane(mainPanel);

        showStaffMenu(); 
    }

    private void showStaffMenu() {
        mainPanel.removeAll(); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

       
        JLabel titleLabel = new JLabel("Staff Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

     
        JButton InventoryButton = createButton("Inventory Management");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(InventoryButton, gbc);
        InventoryButton.addActionListener(e -> {
    new InventoryManagement(staffID,staffName).setVisible(true);
    dispose();
});
        
      

        JButton productButton = createButton("Product Management");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(productButton, gbc);
        productButton.addActionListener(e -> {
    new ProductDetailManagement(staffID,staffName).setVisible(true);
    dispose();
});


        JButton orderButton = createButton("Order Management");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(orderButton, gbc);

        orderButton.addActionListener(e -> {
    new OrderManagementMenu(staffID,staffName).setVisible(true);
    dispose();
});

        JButton supplierButton = createButton("Supplier");
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(supplierButton, gbc);
          supplierButton.addActionListener(e -> {
    new Supplier(staffID,staffName).setVisible(true);
    dispose();
});
          
        JButton transactionButton = createButton("Transaction");
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(transactionButton, gbc);
          transactionButton.addActionListener(e -> {
    new TransactionMenu(staffID,staffName).setVisible(true);
    dispose();
});

       
        JButton profileButton = createButton("Profile");
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(profileButton, gbc);

        
        JButton backButton = createButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(backButton, gbc);
        
        

   
profileButton.addActionListener(e -> {
    new Profile(staffID,staffName);
    dispose();
});


        backButton.addActionListener(e -> backToMainMenu());

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        return button;
    }

    



    
    private void backToMainMenu() {
        new MainMenu().setVisible(true); 
        dispose(); 
    }
}
