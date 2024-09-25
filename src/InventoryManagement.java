package com.mycompany.phoneaccessories;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.table.TableColumn;

public class InventoryManagement extends JFrame {

    private final JPanel mainPanel;
    private ArrayList<String[]> inventoryItems; 
    private JTable inventoryTable; 
    private DefaultTableModel tableModel;

     public InventoryManagement(String staffID,String staffName) {
        setTitle("Inventory Management");
        
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

        
        inventoryItems = new ArrayList<>(); 
       
        loadInventoryData();

        
        showInventoryMenu(staffID,staffName);
    }

    private void loadInventoryData() {
        inventoryItems = new ArrayList<>(); 

        File file = new File("inventory.txt"); 
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] itemDetails = scanner.nextLine().split("\\|");
                if (itemDetails.length >= 14) { 
  
                        inventoryItems.add(itemDetails);
                    
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Inventory file not found!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

private void showInventoryMenu(String staffID, String staffName) {
    mainPanel.removeAll(); 
    mainPanel.setLayout(new BorderLayout()); 

    
    String[] columnNames = {"Product ID", "Product Name", "Warehouse Location", "Stock Quantity", "Minimum Stock Level", "Date Added/Last Updated"};
    tableModel = new DefaultTableModel(columnNames, 0); 

   
    for (String[] item : inventoryItems) {
        
        String[] displayedItem = {
            item[0],  
            item[1],  
            item[8],  
            item[9],  
            item[10], 
            item[11]  
        };
        tableModel.addRow(displayedItem); 
    }

  
    inventoryTable = new JTable(tableModel);
    inventoryTable.setFillsViewportHeight(true);
    inventoryTable.getTableHeader().setReorderingAllowed(false); 
    inventoryTable.setBackground(Color.LIGHT_GRAY); 
    inventoryTable.setForeground(Color.BLACK); 
    inventoryTable.setGridColor(Color.BLACK); 

   
    JScrollPane scrollPane = new JScrollPane(inventoryTable);
    mainPanel.add(scrollPane, BorderLayout.CENTER); 

   
    TableColumn column = inventoryTable.getColumnModel().getColumn(0); 
    column.setPreferredWidth(100); 

    column = inventoryTable.getColumnModel().getColumn(1); 
    column.setPreferredWidth(200);

    column = inventoryTable.getColumnModel().getColumn(2); 
    column.setPreferredWidth(150);

    column = inventoryTable.getColumnModel().getColumn(3); 
    column.setPreferredWidth(100);

    column = inventoryTable.getColumnModel().getColumn(4); 
    column.setPreferredWidth(150);
    
    column = inventoryTable.getColumnModel().getColumn(5); 
    column.setPreferredWidth(80);

    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBackground(Color.DARK_GRAY);

    JButton refreshButton = createButton("Refresh");
    buttonPanel.add(refreshButton);
    refreshButton.addActionListener(e -> refresh());

    JButton addButton = createButton("Add Product");
    buttonPanel.add(addButton);
    addButton.addActionListener(e -> addInventory());

    JButton updateButton = createButton("Update Inventory");
    buttonPanel.add(updateButton);
    updateButton.addActionListener(e -> updateInventory());

    JButton removeButton = createButton("Remove Product ");
    buttonPanel.add(removeButton);
    removeButton.addActionListener(e -> removeInventory());

    JButton reportButton = createButton("Report");
    buttonPanel.add(reportButton);
    reportButton.addActionListener(e -> report(staffID, staffName));

    JButton backButton = createButton("Back");
    buttonPanel.add(backButton);
    backButton.addActionListener(e -> {
        new StaffMenu(staffID, staffName).setVisible(true);
        dispose();
    });

    
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

   
    mainPanel.revalidate();
    mainPanel.repaint();
}


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.GRAY); 
        button.setFocusPainted(false);
        return button;
    }

    
    private void saveInventoryData() {
    try (PrintWriter writer = new PrintWriter(new File("inventory.txt"))) {
        for (String[] item : inventoryItems) {
            writer.println(String.join("|", item)); 
        }
    } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(this, "Error saving inventory!", "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


private void addInventory() {
    String productID = JOptionPane.showInputDialog(this, "Enter Product ID:");

    if (productID != null && !productID.trim().isEmpty()) {
        
        boolean exists = false;
        for (String[] item : inventoryItems) {
            if (item[0].equalsIgnoreCase(productID)) {
                exists = true;
                break;
            }
        }

        if (exists) {
            JOptionPane.showMessageDialog(this, "Product ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        
        JDialog addItemDialog = new JDialog(this, "Add Product", true);
        addItemDialog.setSize(400, 300);
        addItemDialog.setLayout(new BorderLayout(10, 10));

        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
         contentPanel.setBackground(Color.LIGHT_GRAY); 
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; 
        gbc.gridx = 0;

        
        JTextField productNameField = new JTextField(20);
        JTextField warehouseLocationField = new JTextField(20);
        JTextField stockQuantityField = new JTextField(20);
        JTextField lowStockThresholdField = new JTextField(20);
        JTextField dateField = new JTextField(20);

       
        gbc.gridy = 0;
        contentPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(productNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(new JLabel("Warehouse Location:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(warehouseLocationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(stockQuantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(new JLabel("Low Stock Threshold:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(lowStockThresholdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        contentPanel.add(dateField, gbc);

        
        JButton saveButton = new JButton("Add Product");
        saveButton.setBackground(Color.BLACK); 
        saveButton.setForeground(Color.CYAN); 
        saveButton.setOpaque(true); 
        saveButton.setBorderPainted(false); 
        saveButton.addActionListener(e -> {
            
            if (productNameField.getText().trim().isEmpty() || stockQuantityField.getText().trim().isEmpty() || lowStockThresholdField.getText().trim().isEmpty() || dateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

          
            try {
                int stockQuantity = Integer.parseInt(stockQuantityField.getText());
                int lowStockThreshold = Integer.parseInt(lowStockThresholdField.getText());
                String date = dateField.getText();
                
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Please enter the date in YYYY-MM-DD format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String[] newItem = {
                    productID,
                    productNameField.getText(),       
                    "null",                             
                    "null",                           
                    "0.00",                              
                    "0.00",                            
                    "null",                           
                    "null",                              
                    warehouseLocationField.getText(),   
                    String.valueOf(stockQuantity),       
                    String.valueOf(lowStockThreshold),
                    date,                                
                    "0",                                
                    "unpaid"                          
                };

                inventoryItems.add(newItem);

              
                String[] displayedItem = {
                    productID,                          
                    productNameField.getText(),          
                    warehouseLocationField.getText(),    
                    String.valueOf(stockQuantity),       
                    String.valueOf(lowStockThreshold),   
                    date
                };
                tableModel.addRow(displayedItem);

                
                saveInventoryData();

                JOptionPane.showMessageDialog(this, "Product added successfully!");
                addItemDialog.dispose();
                
            } catch (NumberFormatException ex) {
              
                JOptionPane.showMessageDialog(this, "Stock Quantity and Low Stock Threshold must be valid integers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

      
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        addItemDialog.add(contentPanel, BorderLayout.CENTER);
        addItemDialog.add(buttonPanel, BorderLayout.SOUTH);

      
        addItemDialog.setLocationRelativeTo(this);
        addItemDialog.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, "Product ID is required!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}




private void refresh() {
   
    tableModel.setRowCount(0);

   
    for (String[] item : inventoryItems) {
        String[] displayedItem = {
            item[0],  
            item[1],  
            item[8],
            item[9],  
            item[10],
            item[11]  
        };
        tableModel.addRow(displayedItem);
    }

   
    mainPanel.revalidate();
    mainPanel.repaint();
}





private void updateInventory() {
    String productID = JOptionPane.showInputDialog(this, "Enter Product ID to update:");

    if (productID == null || productID.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Product ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

 
    boolean itemFound = false;
    for (int i = 0; i < inventoryItems.size(); i++) {
        String[] item = inventoryItems.get(i);
        if (item[0].equals(productID)) {

            itemFound = true;
            
            JDialog updateItemDialog = new JDialog(this, "Update Inventory", true);
            updateItemDialog.setSize(400, 300);
            updateItemDialog.setLayout(new BorderLayout(10, 10)); 

            
            JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JTextField productNameField = new JTextField(item[1]); 
            JTextField warehouseLocationField = new JTextField(item[8]); 
            JTextField stockQuantityField = new JTextField(item[9]); 
            JTextField lowStockThresholdField = new JTextField(item[10]); 
            JTextField dateField = new JTextField(item[11]);

           
            inputPanel.add(new JLabel("Product Name:"));
            inputPanel.add(productNameField);
            inputPanel.add(new JLabel("Warehouse Location:"));
            inputPanel.add(warehouseLocationField);
            inputPanel.add(new JLabel("Stock Quantity:"));
            inputPanel.add(stockQuantityField);
            inputPanel.add(new JLabel("Low Stock Threshold:"));
            inputPanel.add(lowStockThresholdField);
            inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
            inputPanel.add(dateField);

            updateItemDialog.add(inputPanel, BorderLayout.CENTER);

          
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 

            JButton saveButton = new JButton("Save Changes");
            saveButton.setBackground(Color.BLACK); 
            saveButton.setForeground(Color.CYAN); 
            saveButton.setOpaque(true); 
            saveButton.setBorderPainted(false); 

           
            saveButton.addActionListener(e -> {
       
                try {
                    int stockQuantity = Integer.parseInt(stockQuantityField.getText());
                    int lowStockThreshold = Integer.parseInt(lowStockThresholdField.getText());
                    
                    String date = dateField.getText();
                    if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) { 
                        JOptionPane.showMessageDialog(this, "Please enter the date in YYYY-MM-DD format!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                 
                    item[1] = productNameField.getText();         
                    item[8] = warehouseLocationField.getText();    
                    item[9] = String.valueOf(stockQuantity);      
                    item[10] = String.valueOf(lowStockThreshold);  
                    item[11] = date;                             

                   
                    saveInventoryData();
                    JOptionPane.showMessageDialog(this, "Inventory updated successfully!");
                    updateItemDialog.dispose();

                } catch (NumberFormatException ex) {
                    
                    JOptionPane.showMessageDialog(this, "Stock Quantity and Low Stock Threshold must be valid integers!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            buttonPanel.add(saveButton);
            updateItemDialog.add(buttonPanel, BorderLayout.SOUTH);

           
            updateItemDialog.setLocationRelativeTo(this);
            updateItemDialog.setVisible(true);
            break;
        }
    }
    if (!itemFound) {
        JOptionPane.showMessageDialog(this, "Item with Product ID " + productID + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    refresh();
}




private void removeInventory() {
    String productID = JOptionPane.showInputDialog(this, "Enter Product ID to Remove:");

    if (productID != null) {
        boolean found = false;
        for (int i = 0; i < inventoryItems.size(); i++) {
            if (inventoryItems.get(i)[0].equalsIgnoreCase(productID)) {
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this product?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    inventoryItems.remove(i);
                    tableModel.removeRow(i); 
                    saveInventoryData(); 
                    JOptionPane.showMessageDialog(this, "Product removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                found = true;
                break;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this, "Product ID " + productID + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Input canceled.", "Canceled", JOptionPane.WARNING_MESSAGE);
    }
}

    
private void report(String staffID, String staffName) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    
    JPanel companyInfoPanel = new JPanel();
    companyInfoPanel.setLayout(new GridLayout(4, 1));
    companyInfoPanel.setBorder(BorderFactory.createTitledBorder("Company Information"));

    JLabel companyName = new JLabel("Company Name: XYZ Accessories Ltd.");
    JLabel address = new JLabel("Address: 1234 Market Street, Tech City");
    JLabel managerName = new JLabel("Staff ID : " + staffID);
    JLabel stockKeeper = new JLabel("Staff Name: " + staffName);

    companyInfoPanel.add(companyName);
    companyInfoPanel.add(address);
    companyInfoPanel.add(managerName);
    companyInfoPanel.add(stockKeeper);

    panel.add(companyInfoPanel);

    panel.add(Box.createRigidArea(new Dimension(0, 20))); 

    
    JLabel titleLabel = new JLabel("Low Stock Report");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); 
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
    panel.add(titleLabel);

    panel.add(Box.createRigidArea(new Dimension(0, 10))); 

  
    JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 4));
    tableHeaderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    tableHeaderPanel.add(new JLabel("Product ID", SwingConstants.CENTER));
    tableHeaderPanel.add(new JLabel("Product Name", SwingConstants.CENTER));
    tableHeaderPanel.add(new JLabel("Stock Quantity", SwingConstants.CENTER));
    tableHeaderPanel.add(new JLabel("Low Stock Threshold", SwingConstants.CENTER));
    panel.add(tableHeaderPanel);

   
    for (String[] item : inventoryItems) {
        int stockQuantity = Integer.parseInt(item[9]); 
        int lowStockThreshold = Integer.parseInt(item[10]); 

        JPanel tableRowPanel = new JPanel(new GridLayout(1, 4));
        tableRowPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        tableRowPanel.add(new JLabel(item[0], SwingConstants.CENTER)); 
        tableRowPanel.add(new JLabel(item[1], SwingConstants.CENTER)); 

        JLabel stockLabel = new JLabel(String.valueOf(stockQuantity), SwingConstants.CENTER);
        if (stockQuantity < lowStockThreshold) {
            stockLabel.setForeground(Color.RED); 
        }
        tableRowPanel.add(stockLabel);

        tableRowPanel.add(new JLabel(String.valueOf(lowStockThreshold), SwingConstants.CENTER));
        panel.add(tableRowPanel);
    }

  
    JDialog reportDialog = new JDialog(this, "Low Stock Report", true);
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
