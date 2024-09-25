package com.mycompany.phoneaccessories;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.table.TableColumn;

public class ProductDetailManagement extends JFrame {

    private final JPanel mainPanel;
    private ArrayList<String[]> inventoryItems; 
    private JTable inventoryTable; 
    private DefaultTableModel tableModel;
    private ArrayList<String[]> suppliers; 

     public ProductDetailManagement(String staffID, String staffName) {
        setTitle("Product Details");
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

       
        loadInventoryData();
        loadSupplierData();

      
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
    
    private void loadSupplierData() {
        suppliers = new ArrayList<>();
        File file = new File("supplier.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] supplierDetails = scanner.nextLine().split("\\|");
                if (supplierDetails.length >= 4) {
                    suppliers.add(supplierDetails);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Supplier file not found!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

 private void showInventoryMenu(String staffID, String staffName) {
    mainPanel.removeAll(); 

  
    mainPanel.setLayout(new BorderLayout());

    String[] columnNames = {"Product ID", "Product Name", "Category", "Description", "Retail Price", "Buy Price", "Supplier", "Brand", "Stock Quantity", "Sales Quantity"};
    tableModel = new DefaultTableModel(columnNames, 0);

    for (String[] item : inventoryItems) {
        String[] displayedItem = {
            item[0], item[1], item[2], item[3], item[4], item[5], item[6], item[7], item[9], item[12]
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
    scrollPane.setPreferredSize(new Dimension(800, 400));

 
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.DARK_GRAY);

    JButton refreshButton = createButton("Refresh Product");
    buttonPanel.add(refreshButton);
    refreshButton.addActionListener(e -> refresh());

    JButton updateButton = createButton("Update Product Details");
    buttonPanel.add(updateButton);
    updateButton.addActionListener(e -> updateProductDetails());

    JButton removeButton = createButton("Remove Product");
    buttonPanel.add(removeButton);
    removeButton.addActionListener(e -> removeItem());

    JButton reportButton = createButton("Report");
    buttonPanel.add(reportButton);
    reportButton.addActionListener(e -> report(staffID, staffName));

    JButton searchButton = createButton("Search Product");
    buttonPanel.add(searchButton);
    searchButton.addActionListener(e -> searchProducts());

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


private void updateProductDetails() {
    String productID = JOptionPane.showInputDialog(this, "Enter Product ID:");

    if (productID != null && !productID.trim().isEmpty()) {
        final int[] productIndex = {-1};  
        boolean exists = false;
        for (int i = 0; i < inventoryItems.size(); i++) {
            String[] item = inventoryItems.get(i);
            if (item[0].equalsIgnoreCase(productID)) {
                exists = true;
                productIndex[0] = i;
                break;
            }
        }

        if (!exists) {
            JOptionPane.showMessageDialog(this, "Product ID does not exist! Cannot add details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        JDialog addItemDialog = new JDialog(this, "Add Product Details", true);
        addItemDialog.setSize(400, 300);
        addItemDialog.setLayout(new BorderLayout());

        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        
        JTextField productNameField = new JTextField(20);
        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"Protective Gear", "Charging Solutions", "Audio Accessories", "Storage Solution","Mounts and Holders","Connectivity"});
        JTextField descriptionField = new JTextField(20);
        JTextField retailPriceField = new JTextField(20);
        JTextField buyPriceField = new JTextField(20);
        JTextField supplierField = new JTextField(20);
        JTextField brandField = new JTextField(20);
      

        String[] existingItem = inventoryItems.get(productIndex[0]);
        productNameField.setText(existingItem[1]);
        categoryComboBox.setSelectedItem(existingItem[2]);
        descriptionField.setText(existingItem[3]);
        retailPriceField.setText(existingItem[4]);
        buyPriceField.setText(existingItem[5]);
        supplierField.setText(existingItem[6]);
        brandField.setText(existingItem[7]);
   

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(productNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        centerPanel.add(new JLabel("Retail Price:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(retailPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        centerPanel.add(new JLabel("Buy Price:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(buyPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        centerPanel.add(new JLabel("Supplier:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(supplierField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        centerPanel.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(brandField, gbc);

       
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton saveButton = new JButton("Add Details");
        saveButton.setBackground(Color.BLACK);
        saveButton.setForeground(Color.CYAN);
        buttonPanel.add(saveButton);

       
        saveButton.addActionListener(e -> {
            
            if (productNameField.getText().trim().isEmpty() || descriptionField.getText().trim().isEmpty() || supplierField.getText().trim().isEmpty() || brandField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            try {
                double retailPrice = Double.parseDouble(retailPriceField.getText());
                double buyPrice = Double.parseDouble(buyPriceField.getText());

                
                boolean supplierExists = false;
                String supplierID = supplierField.getText().trim();
                for (String[] supplier : suppliers) {
                    if (supplier[0].equalsIgnoreCase(supplierID)) {                       
                        supplierExists = true;
                        break;
                    }
                }

                if (!supplierExists) {
                    JOptionPane.showMessageDialog(this, "Supplier ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                
                String[] updatedItem = {
                    productID,                       
                    productNameField.getText(),      
                    (String) categoryComboBox.getSelectedItem(), 
                    descriptionField.getText(),      
                    String.valueOf(retailPrice),    
                    String.valueOf(buyPrice),        
                    supplierField.getText(),         
                    brandField.getText(),           
                    existingItem[8],                 
                    existingItem[9],               
                    existingItem[10],              
                    existingItem[11],                 
                    existingItem[12],                
                    existingItem[13]                  
                };

              
                inventoryItems.set(productIndex[0], updatedItem);

             
                tableModel.setValueAt(productNameField.getText(), productIndex[0], 1);
                tableModel.setValueAt(categoryComboBox.getSelectedItem(), productIndex[0], 2);
                tableModel.setValueAt(descriptionField.getText(), productIndex[0], 3);
                tableModel.setValueAt(String.valueOf(retailPrice), productIndex[0], 4);
                tableModel.setValueAt(String.valueOf(buyPrice), productIndex[0], 5);
                tableModel.setValueAt(supplierField.getText(), productIndex[0], 6);
                tableModel.setValueAt(brandField.getText(), productIndex[0], 7);

              
                saveInventoryData(); 
           
               

                JOptionPane.showMessageDialog(this, "Product details updated successfully!");
                addItemDialog.dispose();

            } catch (NumberFormatException ex) {
                
                JOptionPane.showMessageDialog(this, "Retail Price and Buy Price must be valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

       
        addItemDialog.add(centerPanel, BorderLayout.CENTER);
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
            item[2],  
            item[3], 
            item[4], 
            item[5],  
            item[6], 
            item[7],
            item[9],   
            item[12],   
            item[13]  
          
        };
        tableModel.addRow(displayedItem);
    }

    
    mainPanel.revalidate();
    mainPanel.repaint();
}


private void removeItem() {
        String productName = JOptionPane.showInputDialog(this, "Enter Product ID to Remove:");

        if (productName != null) {
            boolean found = false;
            for (int i = 0; i < inventoryItems.size(); i++) {
                if (inventoryItems.get(i)[0].equalsIgnoreCase(productName)) {
                    int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this item?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        inventoryItems.remove(i);
                        tableModel.removeRow(i); 
                        saveInventoryData(); 
                        JOptionPane.showMessageDialog(this, "Item removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Input canceled.", "Canceled", JOptionPane.WARNING_MESSAGE);
        }
    }
    
private void report(String staffID, String staffName) {
    
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    
    JLabel reportTitle = new JLabel("Product Inventory Report", JLabel.CENTER);
    reportTitle.setFont(new Font("Serif", Font.BOLD, 22)); 
    reportTitle.setForeground(new Color(0, 51, 102)); 
    panel.add(reportTitle);

    panel.add(Box.createRigidArea(new Dimension(0, 15)));

  
    JPanel companyInfoPanel = new JPanel(new GridLayout(4, 1, 10, 10));
    companyInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1),
            "Company Information", 0, 0, new Font("Serif", Font.BOLD, 16), Color.DARK_GRAY));

    JLabel companyName = new JLabel("Company Name: XYZ Accessories Ltd.", JLabel.LEFT);
    JLabel address = new JLabel("Address: 1234 Market Street, Tech City", JLabel.LEFT);
    JLabel staffIDLabel = new JLabel("Staff ID: " + staffID, JLabel.LEFT);
    JLabel staffNameLabel = new JLabel("Staff Name: " + staffName, JLabel.LEFT);

    companyInfoPanel.add(companyName);
    companyInfoPanel.add(address);
    companyInfoPanel.add(staffIDLabel);
    companyInfoPanel.add(staffNameLabel);
    panel.add(companyInfoPanel);

    panel.add(Box.createRigidArea(new Dimension(0, 20)));

    
    JLabel totalProductsLabel = new JLabel("Total Products in Inventory: " + inventoryItems.size());
    totalProductsLabel.setFont(new Font("Arial", Font.BOLD, 14)); 
    totalProductsLabel.setForeground(new Color(0, 102, 0)); 
    panel.add(totalProductsLabel);

    panel.add(Box.createRigidArea(new Dimension(0, 10))); 

  
    JLabel bestSellingLabel = new JLabel("Best-Selling Product: " + getBestSellingProduct());
    bestSellingLabel.setFont(new Font("Arial", Font.BOLD, 14)); 
    bestSellingLabel.setForeground(new Color(0, 102, 0)); 
    panel.add(bestSellingLabel);

    panel.add(Box.createRigidArea(new Dimension(0, 15)));

   
    JLabel lowStockLabel = new JLabel("Low Stock Products:");
    lowStockLabel.setFont(new Font("Arial", Font.BOLD, 14)); 
    lowStockLabel.setForeground(new Color(153, 0, 0));
    panel.add(lowStockLabel);

 
    JPanel lowStockPanel = new JPanel();
    lowStockPanel.setLayout(new BoxLayout(lowStockPanel, BoxLayout.Y_AXIS));
    for (String[] item : inventoryItems) {
        int stockQuantity = Integer.parseInt(item[9]); 
        int lowStockThreshold = Integer.parseInt(item[10]); 
        if (stockQuantity < lowStockThreshold) {
            JLabel lowStockItemLabel = new JLabel("Product ID: " + item[0] + " - " + item[1]
                    + " (Category: " + item[2] + ", Stock: " + stockQuantity + ")");
            lowStockItemLabel.setForeground(new Color(204, 0, 0)); 
            lowStockItemLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            lowStockPanel.add(lowStockItemLabel);
        }
    }
    panel.add(lowStockPanel);

    panel.add(Box.createRigidArea(new Dimension(0, 15)));

   
    JLabel categoryStatsLabel = new JLabel("Product Category Statistics:");
    categoryStatsLabel.setFont(new Font("Arial", Font.BOLD, 14)); 
    categoryStatsLabel.setForeground(new Color(0, 51, 102)); 
    panel.add(categoryStatsLabel);

  
    String[] columnNames = {"Category", "Count"};
    Object[][] data = getCategoryStats();
    JTable categoryTable = new JTable(data, columnNames);
    categoryTable.setFont(new Font("Arial", Font.PLAIN, 12));
    categoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    categoryTable.getTableHeader().setBackground(new Color(224, 224, 224));
    categoryTable.setGridColor(Color.LIGHT_GRAY); 

 
    panel.add(categoryTable.getTableHeader(), BorderLayout.NORTH);
    panel.add(categoryTable, BorderLayout.CENTER);

    
    JDialog reportDialog = new JDialog(this, "Product Report", true);
    reportDialog.setLayout(new BorderLayout());
    reportDialog.add(panel, BorderLayout.CENTER);

    
    JButton closeButton = new JButton("Close");
    closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
    closeButton.setPreferredSize(new Dimension(100, 30));
    closeButton.setBackground(new Color(0, 51, 102)); 
    closeButton.setForeground(Color.WHITE); 
    closeButton.setFocusPainted(false); 

    closeButton.addActionListener(e -> reportDialog.dispose());

  
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 
    buttonPanel.add(closeButton);
    reportDialog.add(buttonPanel, BorderLayout.SOUTH);

  
    reportDialog.pack();

   
    reportDialog.setLocationRelativeTo(null);


    reportDialog.setVisible(true);
}





private String getBestSellingProduct() {
    if (inventoryItems.isEmpty()) {
        return "No products available";
    }

    String bestSellingProduct = "";
    int highestSalesQuantity = -1;

    for (String[] item : inventoryItems) {
        int salesQuantity;
        try {
            salesQuantity = Integer.parseInt(item[12]); 
        } catch (NumberFormatException e) {
            continue;
        }

        if (salesQuantity > highestSalesQuantity) {
            highestSalesQuantity = salesQuantity;
            bestSellingProduct = item[1] + " (Product ID: " + item[0] + ")";
        }
    }

    return bestSellingProduct.isEmpty() ? "No sales data available" : bestSellingProduct;
}

private Object[][] getCategoryStats() {
    int protectiveGearSales = 0;
    int chargingSolutionsSales = 0;
    int audioAccessoriesSales = 0;
    int storageSolutionSales = 0;
    int mountsAndHoldersSales = 0;
    int connectivitySales = 0;

   
    for (String[] item : inventoryItems) {
        String category = item[2]; 
        int salesQuantity;
        try {
            salesQuantity = Integer.parseInt(item[12]); 
        } catch (NumberFormatException e) {
            salesQuantity = 0; 
        }

        
        switch (category) {
            case "Protective Gear":
                protectiveGearSales += salesQuantity;
                break;
            case "Charging Solutions":
                chargingSolutionsSales += salesQuantity;
                break;
            case "Audio Accessories":
                audioAccessoriesSales += salesQuantity;
                break;
            case "Storage Solution":
                storageSolutionSales += salesQuantity;
                break;
            case "Mounts and Holders":
                mountsAndHoldersSales += salesQuantity;
                break;
            case "Connectivity":
                connectivitySales += salesQuantity;
                break;
        }
    }

    
    Object[][] categoryStats = {
        {"Protective Gear", protectiveGearSales},
        {"Charging Solutions", chargingSolutionsSales},
        {"Audio Accessories", audioAccessoriesSales},
        {"Storage Solution", storageSolutionSales},
        {"Mounts and Holders", mountsAndHoldersSales},
        {"Connectivity", connectivitySales}
    };

    return categoryStats;
}


private void searchProducts() {
    
    String searchProductID = JOptionPane.showInputDialog(this, "Enter product ID to search:");

    
    if (searchProductID == null || searchProductID.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Product ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }


    tableModel.setRowCount(0);

    boolean foundMatch = false; 

   
    for (String[] item : inventoryItems) {
        boolean matches = true;

        
        if (!item[0].equalsIgnoreCase(searchProductID)) {
            matches = false;
        }

    
        if (matches) {
            foundMatch = true; 
            String[] displayedItem = {
                item[0],  
                item[1],  
                item[2],  
                item[3],
                item[4],  
                item[5], 
                item[6],  
                item[7],  
                item[9],  
                item[12]  
            };
            tableModel.addRow(displayedItem);
        }
    }

 
    if (!foundMatch) {
        JOptionPane.showMessageDialog(this, "No items found for the given Product ID.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
    }

    
    mainPanel.revalidate();
    mainPanel.repaint();
}




}
