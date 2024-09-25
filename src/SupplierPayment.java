package com.mycompany.phoneaccessories;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.text.NumberFormat;
import javax.swing.table.JTableHeader;

public class SupplierPayment extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String staffID; 
    private String staffName; 

    public SupplierPayment(String staffID, String staffName) {
        this.staffID = staffID; 
        this.staffName = staffName; 
        setTitle("Unpaid Suppliers");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     
        tableModel = new DefaultTableModel(new String[]{"SupplierID", "SupplierName", "Contact", "Email", "ProductID", "Retail Price", "Quantity", "Payment Status"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        
        loadUnpaidSuppliers();

  
        JPanel buttonPanel = new JPanel();

      
        JButton payButton = new JButton("Pay Selected Supplier");
        payButton.addActionListener(e -> paySelectedSupplier());
        payButton.setBackground(Color.BLACK); 
        payButton.setForeground(Color.CYAN); 
        buttonPanel.add(payButton);

     
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


    private void loadUnpaidSuppliers() {
        HashMap<String, String[]> suppliers = new HashMap<>();
        String inventoryFile = "inventory.txt";
        String supplierFile = "supplier.txt";

      
        try (BufferedReader br = new BufferedReader(new FileReader(supplierFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String supplierID = parts[0].trim();
                    suppliers.put(supplierID, new String[]{parts[1].trim(), parts[2].trim(), parts[3].trim()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       
        try (BufferedReader br = new BufferedReader(new FileReader(inventoryFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 14) { 
                    String supplierID = parts[6].trim(); 
                    String paymentStatus = parts[13].trim(); 
                    String retailPrice = parts[4].trim(); 
                    String quantity = parts[12].trim(); 

                    if ("unpaid".equalsIgnoreCase(paymentStatus) && suppliers.containsKey(supplierID)) {
                        String[] supplierInfo = suppliers.get(supplierID);
                        tableModel.addRow(new Object[]{
                                supplierID, supplierInfo[0], supplierInfo[1], supplierInfo[2],
                                parts[0].trim(), retailPrice, quantity, paymentStatus
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




private void paySelectedSupplier() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow != -1) {
        String supplierID = tableModel.getValueAt(selectedRow, 0).toString();
        String productID = tableModel.getValueAt(selectedRow, 4).toString();
        String retailPriceString = tableModel.getValueAt(selectedRow, 5).toString();
        String quantityString = tableModel.getValueAt(selectedRow, 6).toString();

        
        String accountNumber = JOptionPane.showInputDialog(this, "Enter Your Account Number:");
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

    
        String supplierAccountNumber = JOptionPane.showInputDialog(this, "Enter Supplier's Account Number:");
        if (supplierAccountNumber == null || supplierAccountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Supplier's account number cannot be empty.");
            return;
        }

       
        double amount;
        try {
            double retailPrice = Double.parseDouble(retailPriceString);
            int quantity = Integer.parseInt(quantityString);
            amount = retailPrice * quantity;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid retail price or quantity.");
            return;
        }

   
        if (!isSufficientBalance(accountNumber, amount)) {
            JOptionPane.showMessageDialog(this, "Insufficient funds in the account.");
            return;
        }


        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to pay " + supplierAccountNumber + " an amount of " + 
            NumberFormat.getCurrencyInstance().format(amount) + "?", 
            "Confirm Payment", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.NO_OPTION) {
            return;
        }
        
    
        updatePaymentStatus(supplierID, productID, accountNumber, amount);
        
  
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        String formattedAmount = currencyFormat.format(amount);
        
     

        
report(staffID, staffName, formattedAmount, supplierID, productID, accountNumber, supplierAccountNumber);

        
      
        tableModel.removeRow(selectedRow);
    } else {
        JOptionPane.showMessageDialog(this, "Please select a supplier to pay.");
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

    private void updatePaymentStatus(String supplierID, String productID, String accountNumber, double amount) {

        StringBuilder updatedInventory = new StringBuilder();
        boolean paymentUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader("inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 14) { 
                    String currentSupplierID = parts[6].trim(); 
                    String currentProductID = parts[0].trim(); 
                    String paymentStatus = parts[13].trim(); 

 
                    if (currentSupplierID.equals(supplierID) && currentProductID.equals(productID) && "unpaid".equalsIgnoreCase(paymentStatus)) {
                        parts[13] = "paid"; 
                        paymentUpdated = true; 
                    }
                    updatedInventory.append(String.join("|", parts)).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (paymentUpdated) {
            try (FileWriter writer = new FileWriter("inventory.txt")) {
                writer.write(updatedInventory.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

     
        try (BufferedReader br = new BufferedReader(new FileReader("account.txt"))) {
            StringBuilder updatedAccounts = new StringBuilder();
            String line;
            double newAmount = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String accNumber = parts[0].trim();
                    double accAmount = Double.parseDouble(parts[1].trim());
                    if (accNumber.equals(accountNumber)) {
                        newAmount = accAmount - amount; 
                    } else {
                        updatedAccounts.append(line).append("\n"); 
                    }
                }
            }

            
            try (FileWriter accountWriter = new FileWriter("account.txt")) {
                updatedAccounts.append(accountNumber).append("|").append(newAmount).append("\n");
                accountWriter.write(updatedAccounts.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
 private void report(String staffID, String staffName, String formattedAmount, String supplierID, String productID, String accountNumber, String supplierAccountNumber) {
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

   
    JLabel titleLabel = new JLabel("Payment Report");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(titleLabel);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    JLabel accountLabel = new JLabel("Account Number: " + accountNumber);
    accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(accountLabel);
    
    JLabel supplierAccountLabel = new JLabel("Supplier's Account Number: " + supplierAccountNumber);
    supplierAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(supplierAccountLabel);
    

    JLabel totalAmountLabel = new JLabel("Total Amount: " + formattedAmount);
    totalAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(totalAmountLabel);
    panel.add(Box.createRigidArea(new Dimension(0, 20)));


    JDialog reportDialog = new JDialog(this, "Payment Report", true);
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
