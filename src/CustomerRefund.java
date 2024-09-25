package com.mycompany.phoneaccessories;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import javax.swing.table.JTableHeader;

public class CustomerRefund extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String staffID; 
    private String staffName; 

    public CustomerRefund(String staffID, String staffName) {
        this.staffID = staffID;
        this.staffName = staffName;
        setTitle("Refund Requests");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        tableModel = new DefaultTableModel(new String[]{"CustomerID", "CustomerName", "Email", "PhoneNumber", "Address", "OrderID", "Amount", "RequestRefund"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

     
        loadRefundRequests();

   
        JPanel buttonPanel = new JPanel();

        
        JButton refundButton = new JButton("Process Selected Refund");
        refundButton.addActionListener(e -> processSelectedRefund());
        refundButton.setBackground(Color.BLACK); 
        refundButton.setForeground(Color.CYAN); 
        buttonPanel.add(refundButton);

        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new TransactionMenu(staffID, staffName).setVisible(true);
            dispose();
        });
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.CYAN);
        buttonPanel.add(backButton);

        
        add(buttonPanel, BorderLayout.SOUTH);

        
        getContentPane().setBackground(Color.GREEN);
        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.BLACK);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.CYAN);

        setVisible(true);
    }

    public void loadRefundRequests() {
        HashMap<String, String[]> customers = new HashMap<>();
        String customerFile = "customer.txt";
        String orderFile = "order.txt";
        String inventoryFile = "inventory.txt";
        HashMap<String, String> productBuyPrices = new HashMap<>();

        
        try (BufferedReader br = new BufferedReader(new FileReader(inventoryFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) { 
                    String productID = parts[0].trim(); 
                    String buyPrice = parts[5].trim();  
                    productBuyPrices.put(productID, buyPrice);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    
        try (BufferedReader br = new BufferedReader(new FileReader(customerFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String customerID = parts[0].trim();
                    customers.put(customerID, new String[]{parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) { 
                    String customerID = parts[1].trim();
                    String productID = parts[2].trim();
                    String requestRefund = parts[6].trim();

                    if ("yes".equalsIgnoreCase(requestRefund) && customers.containsKey(customerID)) {
                        String[] customerInfo = customers.get(customerID);
                        String buyPrice = productBuyPrices.getOrDefault(productID, "0");

                        tableModel.addRow(new Object[]{
                                customerID, customerInfo[0], customerInfo[1], customerInfo[2], customerInfo[3],
                                parts[0].trim(), buyPrice, requestRefund
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processSelectedRefund() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String customerID = tableModel.getValueAt(selectedRow, 0).toString();
            String orderID = tableModel.getValueAt(selectedRow, 5).toString();
            String amountString = tableModel.getValueAt(selectedRow, 6).toString();
            String customerName = tableModel.getValueAt(selectedRow, 1).toString(); 
         
            String accountNumber = JOptionPane.showInputDialog(this, "Enter Account Number:");
            if (accountNumber == null ) {
                return;
            }
            
            if (accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Account number cannot be empty.");
            return;
        }
            
      
            if (!isValidAccount(accountNumber)) {
                JOptionPane.showMessageDialog(this, "Invalid account number.");
                return;
            }

     
            double refundAmount;
            try {
                refundAmount = Double.parseDouble(amountString);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid refund amount.");
                return;
            }

          
            if (!isSufficientBalance(accountNumber, refundAmount)) {
                JOptionPane.showMessageDialog(this, "Insufficient funds in the account.");
                return;
            }

           
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to refund customer " + customerID + " an amount of " +
                            NumberFormat.getCurrencyInstance().format(refundAmount) + "?",
                    "Confirm Refund", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.NO_OPTION) {
                return; 
            }

       
            updateRefundStatus(orderID);

           
        updateAccountBalance(accountNumber, refundAmount);


          
            report(staffID, staffName, refundAmount, customerID, orderID, accountNumber,customerName);

        
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to refund.");
        }
    }

    private boolean isValidAccount(String accountNumber) {
        try (BufferedReader br = new BufferedReader(new FileReader("account.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2 && parts[0].trim().equals(accountNumber)) {
                    return true; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; 
    }

    private void updateAccountBalance(String accountNumber, double refundAmount) {
    StringBuilder updatedAccounts = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader("account.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length == 2 && parts[0].trim().equals(accountNumber)) {
                double currentAmount = Double.parseDouble(parts[1].trim());
                double newAmount = currentAmount - refundAmount; 
                parts[1] = String.valueOf(newAmount);
            }
            updatedAccounts.append(String.join("|", parts)).append("\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

   
    try (FileWriter writer = new FileWriter("account.txt")) {
        writer.write(updatedAccounts.toString());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    private boolean isSufficientBalance(String accountNumber, double amount) {
        try (BufferedReader br = new BufferedReader(new FileReader("account.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2 && parts[0].trim().equals(accountNumber)) {
                    double currentAmount = Double.parseDouble(parts[1].trim());
                    return currentAmount >= amount; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; 
    }

private void updateRefundStatus(String orderID) {
    StringBuilder updatedOrders = new StringBuilder();
    boolean found = false; 

    try (BufferedReader br = new BufferedReader(new FileReader("order.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                if (parts[0].trim().equals(orderID)) {
                    parts[6] = "no"; 
                    found = true; 
                }
            }
            updatedOrders.append(String.join("|", parts)).append("\n"); 
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

   
    if (found) {
        try (FileWriter writer = new FileWriter("order.txt")) {
            writer.write(updatedOrders.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("Order ID not found: " + orderID);
    }
}


private void report(String staffID, String staffName, double amount, String customerID, String orderID, String accountNumber,String customerName) {
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

    // Title
    JLabel titleLabel = new JLabel("Refund Report");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(titleLabel);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));


JLabel accountLabel = new JLabel("Account Number: " + accountNumber);
accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
panel.add(accountLabel);
panel.add(Box.createRigidArea(new Dimension(0, 10))); 

JLabel customerLabel = new JLabel("Customer ID: " + customerID);
customerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
panel.add(customerLabel);
panel.add(Box.createRigidArea(new Dimension(0, 10))); 

JLabel customerNameLabel = new JLabel("Customer Name: " + customerName);
customerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
panel.add(customerNameLabel);
panel.add(Box.createRigidArea(new Dimension(0, 10))); 

JLabel orderLabel = new JLabel("Order ID: " + orderID);
orderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
panel.add(orderLabel);
panel.add(Box.createRigidArea(new Dimension(0, 10))); 

// Total amount
JLabel totalAmountLabel = new JLabel("Refund Amount: " + NumberFormat.getCurrencyInstance().format(amount));
totalAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
panel.add(totalAmountLabel);

    panel.add(Box.createRigidArea(new Dimension(0, 20)));

   
    JDialog reportDialog = new JDialog(this, "Refund Report", true);
    reportDialog.setSize(620, 450);
    reportDialog.setLayout(new BorderLayout());
    reportDialog.add(panel, BorderLayout.CENTER);

 
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


}