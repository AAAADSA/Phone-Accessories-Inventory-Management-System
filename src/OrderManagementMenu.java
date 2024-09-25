package com.mycompany.phoneaccessories;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OrderManagementMenu extends JFrame {

    private final JPanel mainPanel;
    private String staffName;
    private String staffID;
    private List<String[]> orderData = new ArrayList<>(); 

   
    public OrderManagementMenu(String staffID, String staffName) {
        this.staffID = staffID;
        this.staffName = staffName;

        setTitle("Order Management Menu");
      
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

        showOrderManagementMenu();

        
        loadOrderData();
    }

    private void showOrderManagementMenu() {
        mainPanel.removeAll(); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        
        JLabel titleLabel = new JLabel("Order Management Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

       
        JButton updateStatusButton = createButton("Update Order Status");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(updateStatusButton, gbc);
        updateStatusButton.addActionListener(e -> {
            new Order(staffID,staffName).setVisible(true);
            dispose();
        });

        JButton trackOrderButton = createButton("Track Order");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(trackOrderButton, gbc);
        trackOrderButton.addActionListener(e -> {
            new TrackOrder(staffID, staffName).setVisible(true);
            dispose();
        });

        JButton reportButton = createButton("Report");
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(reportButton, gbc);
        reportButton.addActionListener(e -> viewReport(staffID,staffName));

        
        JButton backButton = createButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(backButton, gbc);

        
        backButton.addActionListener(e -> backToStaffMenu());

      
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


 
    private void loadOrderData() {
        File file = new File("order.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] order = line.split("\\|");
                if (order.length == 7) {
                    orderData.add(order);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Order file not found!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void viewReport(String staffID, String staffName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

      
        JPanel companyInfoPanel = new JPanel();
        companyInfoPanel.setLayout(new GridLayout(4, 1));
        companyInfoPanel.setBorder(BorderFactory.createTitledBorder("Company Information"));

        JLabel companyName = new JLabel("Company Name: XYZ Accessories Ltd.");
        JLabel address = new JLabel("Address: 1234 Market Street, Tech City");
        JLabel managerName = new JLabel("Staff ID: " + staffID);
        JLabel stockKeeper = new JLabel("Staff Name: " + staffName);

        companyInfoPanel.add(companyName);
        companyInfoPanel.add(address);
        companyInfoPanel.add(managerName);
        companyInfoPanel.add(stockKeeper);

        panel.add(companyInfoPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

       
        JLabel titleLabel = new JLabel("Order Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10))); 

     
        JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
        tableHeaderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tableHeaderPanel.add(new JLabel("Order ID", SwingConstants.CENTER));
        tableHeaderPanel.add(new JLabel("Product ID", SwingConstants.CENTER));
        tableHeaderPanel.add(new JLabel("Order Status", SwingConstants.CENTER));
        panel.add(tableHeaderPanel);

      
        Map<String, Integer> statusCount = new HashMap<>();
        
      
        for (String[] order : orderData) {
            String orderID = order[0];
            String productID = order[2];
            String status = order[3];

          
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);

            JPanel tableRowPanel = new JPanel(new GridLayout(1, 3));
            tableRowPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            tableRowPanel.add(new JLabel(orderID, SwingConstants.CENTER)); 
            tableRowPanel.add(new JLabel(productID, SwingConstants.CENTER)); 
            tableRowPanel.add(new JLabel(status, SwingConstants.CENTER)); 

            panel.add(tableRowPanel);
        }

    
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(statusCount.size() + 1, 1));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Order Status Summary"));

    
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            JLabel statusSummary = new JLabel(entry.getKey() + ": " + entry.getValue() + " orders");
            summaryPanel.add(statusSummary);
        }

        panel.add(Box.createRigidArea(new Dimension(0, 20))); 
        panel.add(summaryPanel);

        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollPane.setPreferredSize(new Dimension(600, 500));

        
        JDialog reportDialog = new JDialog(this, "Order Report", true);
        reportDialog.setSize(620, 550);
        reportDialog.setLayout(new BorderLayout());
        reportDialog.add(scrollPane, BorderLayout.CENTER);

         
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.setPreferredSize(new Dimension(100, 30));
        closeButton.setBackground(Color.BLACK); 
        closeButton.setForeground(Color.CYAN); 
        closeButton.setFocusPainted(false); 

        closeButton.addActionListener(e -> reportDialog.dispose());

       
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(closeButton);
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);

      
        reportDialog.setLocationRelativeTo(null);
        reportDialog.setVisible(true);
    }


 
    private void backToStaffMenu() {
        new StaffMenu(staffID,staffName).setVisible(true); 
        dispose(); 
    }
}
