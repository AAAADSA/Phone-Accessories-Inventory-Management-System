package com.mycompany.phoneaccessories;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TrackOrder extends JFrame {

    private JTextField trackingNumberField;
    private JButton trackButton;
    private JButton backButton;
    private JTextArea resultArea;
    private List<String[]> orderData = new ArrayList<>();
    private List<String[]> customerData = new ArrayList<>();
    private List<String[]> inventoryData = new ArrayList<>();
    private String orderFilePath = "order.txt"; 
    private String customerFilePath = "customer.txt"; 
    private String inventoryFilePath = "inventory.txt"; 
    private String staffID;
    private String staffName;

    public TrackOrder(String staffID, String staffName) {
        this.staffID = staffID;
        this.staffName = staffName;
        setTitle("Track Order");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();


        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);


        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("Order Tracking System");
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

     
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel trackingNumberLabel = new JLabel("Enter Tracking Number:");
        trackingNumberLabel.setForeground(Color.CYAN);
        trackingNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(trackingNumberLabel, gbc);

        trackingNumberField = new JTextField(30);
        trackingNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        inputPanel.add(trackingNumberField, gbc);

        trackButton = new JButton("Track Order");
        trackButton.setBackground(Color.CYAN);
        trackButton.setForeground(Color.BLACK);
        trackButton.setFont(new Font("Arial", Font.BOLD, 14));
        trackButton.setFocusPainted(false);
        trackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trackOrder();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        inputPanel.add(trackButton, gbc);

        
        backButton = new JButton("Back");
        backButton.setBackground(Color.CYAN);
        backButton.setForeground(Color.RED);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new OrderManagementMenu(staffID, staffName).setVisible(true);
                dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(backButton, gbc);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

       
        resultArea = new JTextArea(15, 50);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(Color.BLACK);
        resultArea.setForeground(Color.CYAN);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        
        add(mainPanel);
        setVisible(true);

      
        loadOrderData();
        loadCustomerData();
        loadInventoryData();
    }

  
    private void loadOrderData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(orderFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] order = line.split("\\|");
                if (order.length == 7) {
                    orderData.add(order);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private void loadCustomerData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(customerFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] customer = line.split("\\|");
                if (customer.length == 5) {
                    customerData.add(customer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void loadInventoryData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inventoryFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] product = line.split("\\|");
                if (product.length == 14) {
                    inventoryData.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private void trackOrder() {
        String trackingNumber = trackingNumberField.getText().trim();

        if (trackingNumber != null && !trackingNumber.isEmpty()) {
            boolean found = false;
            StringBuilder trackingInfo = new StringBuilder("Order Tracking Information:\n\n");

            for (String[] order : orderData) {
                if (trackingNumber.equals(order[4])) {
                    String orderID = order[0];
                    String customerID = order[1];
                    String productID = order[2];
                    String orderStatus = order[3];
                    String purchaseDate = order[5];
                    String requestRefund = order[6];

                    String customerName = "";
                    String email = "";
                    String phoneNumber = "";
                    String address = "";
                    for (String[] customer : customerData) {
                        if (customerID.equals(customer[0])) {
                            customerName = customer[1];
                            email = customer[2];
                            phoneNumber = customer[3];
                            address = customer[4];
                            break;
                        }
                    }

                    String productName = "";
                    for (String[] product : inventoryData) {
                        if (productID.equals(product[0])) {
                            productName = product[1];
                            break;
                        }
                    }

                    trackingInfo.append("Order ID               : ").append(orderID)
                                 .append("\nProduct Name      : ").append(productName)
                                 .append("\nCustomer Name   : ").append(customerName)
                                 .append("\nEmail                    : ").append(email)
                                 .append("\nPhone Number     : ").append(phoneNumber)
                                 .append("\nAddress                : ").append(address)
                                 .append("\nOrder Status         : ").append(orderStatus)
                                 .append("\nTracking Number  : ").append(trackingNumber)
                                 .append("\nPurchase Date      : ").append(purchaseDate)
                                 .append("\nRequest Refund    : ").append(requestRefund)
                                 .append("\n");
                    found = true;

                    break;
                }
            }

            if (found) {
                resultArea.setText(trackingInfo.toString());
            } else {
                resultArea.setText("No order found with that tracking number.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tracking number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
