package com.mycompany.phoneaccessories;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order extends JFrame {

    private JTable orderTable;
    private JComboBox<String> actionComboBox;
    private JButton goButton, backButton;
    private List<String[]> orderData = new ArrayList<>();
    private Map<String, String[]> customerData = new HashMap<>();
    private Map<String, String[]> inventoryData = new HashMap<>();
    private String orderFilePath = "order.txt";
    private String customerFilePath = "customer.txt";
    private String inventoryFilePath = "inventory.txt";
    private String staffName;
    private String staffID;

    public Order(String staffID, String staffName) {
        this.staffID = staffID;
        this.staffName = staffName;
        
        setTitle("Orders");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    

    int width = (int) (screenSize.width * 0.9);
    int height = (int) (screenSize.height * 0.9);
    
    
    setSize(width, height);
    setLocationRelativeTo(null); 

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        loadOrderData();
        loadCustomerData();
        loadInventoryData();

        
        String[] columnNames = {
                "Order ID", "Product Name", "Customer Name", "Email", "Phone Number", 
                "Address", "Order Status", "Tracking Number", "Purchase Date", "Request Refund"
        };
        DefaultTableModel model = new DefaultTableModel(convertData(), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; 
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        orderTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(orderTable);

      
        actionComboBox = new JComboBox<>(new String[]{"- Please Select -", "Update Order Status", "Update Tracking Number"});
        JPanel actionPanel = new JPanel();
        actionPanel.add(new JLabel("Action: "));
        actionPanel.add(actionComboBox);

       
        goButton = new JButton("Go");
        goButton.setBackground(Color.BLACK);
        goButton.setForeground(Color.CYAN);
        goButton.setFocusPainted(false); 
        goButton.setFont(new Font("Arial", Font.BOLD, 14));

        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleAction();
            }
        });
        actionPanel.add(goButton);

        
        backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.CYAN);
        backButton.setFocusPainted(false); 
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        backButton.addActionListener(e -> backToOrderManagementMenu());
        actionPanel.add(backButton);

     
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

       
        add(tableScrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

      
        setLocationRelativeTo(null);

        setVisible(true);
    }

  
    private void loadOrderData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(orderFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] order = line.split("\\|");
                orderData.add(order);
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
                customerData.put(customer[0], customer); 
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
                inventoryData.put(product[0], product); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private Object[][] convertData() {
        Object[][] data = new Object[orderData.size()][10];
        for (int i = 0; i < orderData.size(); i++) {
            String[] order = orderData.get(i);
            String orderId = order[0];
            String customerId = order[1];
            String productId = order[2];
            String orderStatus = order[3];
            String trackingNumber = order[4];
            String purchaseDate = order[5];
            String requestRefund = order[6];

            String[] customer = customerData.get(customerId);
            String[] product = inventoryData.get(productId);

            if (customer != null && product != null) {
                data[i][0] = orderId;
                data[i][1] = product[1]; 
                data[i][2] = customer[1]; 
                data[i][3] = customer[2]; 
                data[i][4] = customer[3]; 
                data[i][5] = customer[4]; 
                data[i][6] = orderStatus;
                data[i][7] = trackingNumber;
                data[i][8] = purchaseDate;
                data[i][9] = requestRefund;
            }
        }
        return data;
    }

   
    private void handleAction() {
        String selectedAction = (String) actionComboBox.getSelectedItem();
        if (selectedAction.equals("Update Order Status")) {
            updateOrderStatus();
        } else if (selectedAction.equals("Update Tracking Number")) {
            updateTrackingNumber();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an action.");
        }
    }

    
    private void updateOrderStatus() {
        int[] selectedRows = orderTable.getSelectedRows();
        if (selectedRows.length > 0) {
          
            String[] statuses = {"Pending", "Shipped", "Delivered", "Cancelled"};
            JComboBox<String> statusComboBox = new JComboBox<>(statuses);
            int result = JOptionPane.showConfirmDialog(this, statusComboBox, "Select new status", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newStatus = (String) statusComboBox.getSelectedItem();
                if (newStatus != null && !newStatus.trim().isEmpty()) {
                     int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to update the status to \"" + newStatus + "\"?", "Confirm Update", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    for (int row : selectedRows) {
                         String productName = (String) orderTable.getValueAt(row, 1); 
                        String productId = getProductIdByName(productName); 

                        orderTable.setValueAt(newStatus, row, 6);  
                        updateOrderFile(row, 3, newStatus);  
                 
                        if ("Delivered".equalsIgnoreCase(newStatus)) {
                            updateStockQuantity(productId);
                        }   
                    }
                }
                } else {
                    JOptionPane.showMessageDialog(this, "Status cannot be empty.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select at least one order.");
        }
    }

 
    private void updateTrackingNumber() {
        int[] selectedRows = orderTable.getSelectedRows();
        if (selectedRows.length > 0) {
            for (int row : selectedRows) {
                String orderId = (String) orderTable.getValueAt(row, 0);
                String newTrackingNumber = JOptionPane.showInputDialog(this, "Enter new tracking number for Order ID: " + orderId);

                if (newTrackingNumber != null && !newTrackingNumber.trim().isEmpty()) {
                    int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to update the tracking number to \"" + newTrackingNumber + "\"?", "Confirm Update", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    orderTable.setValueAt(newTrackingNumber, row, 7);  
                    updateOrderFile(row, 4, newTrackingNumber);  
                } 
                }else {
                    JOptionPane.showMessageDialog(this, "Tracking number cannot be empty.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select at least one order.");
        }
    }

    
    private void updateOrderFile(int row, int column, String newValue) {
        orderData.get(row)[column] = newValue;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFilePath))) {
            for (String[] order : orderData) {
                writer.write(String.join("|", order));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
  private void updateStockQuantity(String productId) {
  
    String[] product = inventoryData.get(productId);
    if (product != null) {
      
        int stockQuantity = Integer.parseInt(product[9]); 
        int salesQuantity = Integer.parseInt(product[12]);

      
        if (stockQuantity > 0) {
            stockQuantity--; 
            salesQuantity++; 
            
          
            product[9] = String.valueOf(stockQuantity); 
            product[12] = String.valueOf(salesQuantity);
            
          
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFilePath))) {
                for (String[] item : inventoryData.values()) {
                    writer.write(String.join("|", item));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Updated stock for Product ID: " + productId + ", new stock quantity: " + stockQuantity + ", new sales quantity: " + salesQuantity);
        } else {
            System.out.println("Stock for Product ID: " + productId + " is already at zero.");
        }
    } else {
        System.out.println("Product ID: " + productId + " not found in inventory.");
    }
}

private String getProductIdByName(String productName) {
    for (Map.Entry<String, String[]> entry : inventoryData.entrySet()) {
        String[] productDetails = entry.getValue();
        if (productDetails[1].equalsIgnoreCase(productName)) { 
            return entry.getKey(); 
        }
    }
    return null; 
}
    
    private void backToOrderManagementMenu() {
        new OrderManagementMenu(staffID, staffName).setVisible(true);
        dispose();
    }
}