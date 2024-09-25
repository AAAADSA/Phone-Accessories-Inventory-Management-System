package com.mycompany.phoneaccessories;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Supplier extends JFrame {

    private JTable productSupplierTable;
    private JButton refreshButton;
    private List<String[]> supplierData = new ArrayList<>();
    private List<String[]> contractData = new ArrayList<>();

    private String supplierFilePath = "supplier.txt"; 
    private String contractFilePath = "contract.txt"; 
    private String staffID;
    private String staffName;

    public Supplier(String staffID, String staffName) {
        this.staffID = staffID;
        this.staffName = staffName;
     
        setTitle("Product Supplier Information");
                
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        

        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        setLocationRelativeTo(null);


        
        loadSupplierData();
        loadContractData();

  
        String[] columnNames = {
                "Supplier ID", "Supplier Name", "Contact", "Email",
                "Contract ID", "Start Date", "End Date", "Terms"
        };
        DefaultTableModel model = new DefaultTableModel(convertProductSupplierData(), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        productSupplierTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(productSupplierTable);

        refreshButton = new JButton("Refresh");
        JButton addButton = new JButton("Add Supplier");
        JButton updateButton = new JButton("Update Supplier");
        JButton updateContractButton = new JButton("Update Contract");
        JButton removeButton = new JButton("Remove Supplier");
        JButton backButton = new JButton("Back to Staff Menu");
   

       refreshButton.setBackground(Color.BLACK);
addButton.setBackground(Color.BLACK);
updateButton.setBackground(Color.BLACK);
updateContractButton.setBackground(Color.BLACK);
removeButton.setBackground(Color.BLACK);
backButton.setBackground(Color.BLACK);

refreshButton.setForeground(Color.CYAN);
addButton.setForeground(Color.CYAN);
updateButton.setForeground(Color.CYAN);
updateContractButton.setForeground(Color.CYAN);
removeButton.setForeground(Color.CYAN);
backButton.setForeground(Color.CYAN);

      refreshButton.addActionListener(e -> refreshData());
        addButton.addActionListener(e -> addSupplier());
        updateButton.addActionListener(e -> updateSupplier());
        updateContractButton.addActionListener(e -> updateContract());
        removeButton.addActionListener(e -> removeSupplier());       
        backButton.addActionListener(e -> backToStaffMenu());

     
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(updateContractButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);

   
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

 private void refreshData() {
    loadSupplierData();
    loadContractData();
    refreshTable();
    JOptionPane.showMessageDialog(this, "Data refreshed successfully!");
}
    private void loadSupplierData() {
        supplierData.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(supplierFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] supplier = line.split("\\|"); 
                if (supplier.length == 4) {
                    supplierData.add(supplier);
                } else {
                    System.out.println("Invalid supplier format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private void loadContractData() {
        contractData.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(contractFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] contract = line.split("\\|");
                if (contract.length == 5) {
                    contractData.add(contract);
                } else {
                    System.out.println("Invalid contract format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  
    private Object[][] convertProductSupplierData() {
        List<Object[]> mergedData = new ArrayList<>();

        for (String[] supplier : supplierData) {
            String supplierId = supplier[0];
            String[] contract = findContractBySupplierId(supplierId);

            if (contract != null) {
                Object[] row = new Object[]{
                        supplierId, 
                        supplier[1], 
                        supplier[2], 
                        supplier[3], 
                        contract[0], 
                        contract[2], 
                        contract[3], 
                        contract[4]  
                };
                mergedData.add(row);
            }
        }

        return mergedData.toArray(new Object[0][]);
    }

    
    private String[] findContractBySupplierId(String supplierId) {
        for (String[] contract : contractData) {
            if (contract[1].equals(supplierId)) {
                return contract;
            }
        }
        return null;
    }

    // Add a new supplier
    private void addSupplier() {
        while (true) { 
          
            JTextField supplierIdField = new JTextField();
            JTextField supplierNameField = new JTextField();
            JTextField contactField = new JTextField();
            JTextField emailField = new JTextField();

           
            JTextField contractIdField = new JTextField();
            JTextField startDateField = new JTextField();
            JTextField endDateField = new JTextField();
            JTextField termsField = new JTextField();

 
            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Supplier ID:"));
            panel.add(supplierIdField);
            panel.add(new JLabel("Supplier Name:"));
            panel.add(supplierNameField);
            panel.add(new JLabel("Contact:"));
            panel.add(contactField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Contract ID:"));
            panel.add(contractIdField);
            panel.add(new JLabel("Start Date (YYYY-MM-DD):"));
            panel.add(startDateField);
            panel.add(new JLabel("End Date (YYYY-MM-DD):"));
            panel.add(endDateField);
            panel.add(new JLabel("Terms:"));
            panel.add(termsField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add Supplier and Contract", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String supplierId = supplierIdField.getText().trim();
                String supplierName = supplierNameField.getText().trim();
                String contact = contactField.getText().trim();
                String email = emailField.getText().trim();

                String contractId = contractIdField.getText().trim();
                String startDate = startDateField.getText().trim();
                String endDate = endDateField.getText().trim();
                String terms = termsField.getText().trim();

               
                if (supplierId.isEmpty() || supplierName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Supplier ID and Name are required.");
                    continue; 
                }
                 
              
                if (!isNumeric(contact)) {
                    JOptionPane.showMessageDialog(this, "Contact must be a numeric value.");
                    continue; 
                }
                
             
                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(this, "Invalid email format.");
                    continue; 
                }

             
                if (!isValidDate(startDate) || !isValidDate(endDate)) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                    continue; 
                }

                
                boolean idExists = false;
                for (String[] supplier : supplierData) {
                    if (supplier[0].equals(supplierId)) {
                        idExists = true;
                        break;
                    }
                }

                if (idExists) {
                    JOptionPane.showMessageDialog(this, "Supplier ID already exists. Please enter a unique ID.");
                    continue; 
                }

                
                boolean contractIdExists = false;
                for (String[] contract : contractData) {
                    if (contract[0].equals(contractId)) {
                        contractIdExists = true;
                        break;
                    }
                }

                if (contractIdExists) {
                    JOptionPane.showMessageDialog(this, "Contract ID already exists. Please enter a unique ID.");
                    continue; 
                } else {
                    String[] newSupplier = {supplierId, supplierName, contact, email}; 
                    supplierData.add(newSupplier);

                  
                    saveSupplierData();
                    saveContractData(contractId, supplierId, startDate, endDate, terms);
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Supplier and Contract added successfully!"); 
                    break; 
                }
            } else {
                break; 
            }
        }
    }

    
    private void updateSupplier() {
        int selectedRow = productSupplierTable.getSelectedRow();
        if (selectedRow != -1) {
            String supplierId = (String) productSupplierTable.getValueAt(selectedRow, 0);

            JTextField supplierIdField = new JTextField(supplierId);
            supplierIdField.setEditable(false);

            JTextField supplierNameField = new JTextField((String) productSupplierTable.getValueAt(selectedRow, 1));
            JTextField contactField = new JTextField((String) productSupplierTable.getValueAt(selectedRow, 2));
            JTextField emailField = new JTextField((String) productSupplierTable.getValueAt(selectedRow, 3));

        
            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Supplier ID:"));
            panel.add(supplierIdField);
            panel.add(new JLabel("Supplier Name:"));
            panel.add(supplierNameField);
            panel.add(new JLabel("Contact:"));
            panel.add(contactField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Supplier Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newSupplierName = supplierNameField.getText().trim();
                String newContact = contactField.getText().trim();
                String newEmail = emailField.getText().trim();

                if (newSupplierName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Supplier Name is required.");
                    return;
                }
                
             
                if (!isNumeric(newContact)) {
                    JOptionPane.showMessageDialog(this, "Contact must be a numeric value.");
                    return; 
                }

                if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(this, "Invalid email format.");
                    return;
                }

             
                for (String[] supplier : supplierData) {
                    if (supplier[0].equals(supplierId)) {
                        supplier[1] = newSupplierName;
                        supplier[2] = newContact;
                        supplier[3] = newEmail;
                        break;
                    }
                }

                
                saveSupplierData();
                refreshTable();
                JOptionPane.showMessageDialog(this, "Supplier updated successfully!"); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "No supplier selected.");
        }
    }

  
    private void removeSupplier() {
        int selectedRow = productSupplierTable.getSelectedRow();
        if (selectedRow != -1) {
            String supplierId = (String) productSupplierTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this supplier?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                supplierData.removeIf(supplier -> supplier[0].equals(supplierId));
                removeContractBySupplierId(supplierId);

               
                saveSupplierData();
                saveContractData();
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No supplier selected.");
        }
    }

   
    private void removeContractBySupplierId(String supplierId) {
        contractData.removeIf(contract -> contract[1].equals(supplierId));
        saveContractData();
    }

    
    private void saveSupplierData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(supplierFilePath))) {
            for (String[] supplier : supplierData) {
                writer.write(String.join("|", supplier));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void saveContractData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(contractFilePath))) {
            for (String[] contract : contractData) {
                writer.write(String.join("|", contract));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private void saveContractData(String contractId, String supplierId, String startDate, String endDate, String terms) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(contractFilePath, true))) {
            writer.write(String.join("|", contractId, supplierId, startDate, endDate, terms));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

private void refreshTable() {
    DefaultTableModel model = (DefaultTableModel) productSupplierTable.getModel();
    
   
    Object[][] newData = convertProductSupplierData();
    

    String[] columnNames = {
        "Supplier ID", "Supplier Name", "Contact", "Email",
        "Contract ID", "Start Date", "End Date", "Terms"
    };
    
  
    if (newData == null || newData.length == 0) {
        newData = new Object[0][columnNames.length];
    }
    
   
    if (model.getColumnCount() != columnNames.length) {
        model.setColumnIdentifiers(columnNames);
    }
    
    
    model.setDataVector(newData, columnNames);
}



    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

 
    private void backToStaffMenu() {
         new StaffMenu(staffID, staffName).setVisible(true);
        dispose();
    }

    
    private boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}"); 
    }

private void updateContract() {
    int selectedRow = productSupplierTable.getSelectedRow();
    if (selectedRow != -1) {
        String contractId = (String) productSupplierTable.getValueAt(selectedRow, 4); 
        String supplierId = (String) productSupplierTable.getValueAt(selectedRow, 0); 

       
        String[] existingContract = findContractBySupplierId(supplierId);

        if (existingContract != null) {
            JTextField contractIdField = new JTextField(existingContract[0]);
            contractIdField.setEditable(false);
            JTextField startDateField = new JTextField(existingContract[2]);
            JTextField endDateField = new JTextField(existingContract[3]);
            JTextField termsField = new JTextField(existingContract[4]);

          
            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Contract ID:"));
            panel.add(contractIdField);
            panel.add(new JLabel("Start Date (YYYY-MM-DD):"));
            panel.add(startDateField);
            panel.add(new JLabel("End Date (YYYY-MM-DD):"));
            panel.add(endDateField);
            panel.add(new JLabel("Terms:"));
            panel.add(termsField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Contract Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newStartDate = startDateField.getText().trim();
                String newEndDate = endDateField.getText().trim();
                String newTerms = termsField.getText().trim();

               
                if (!isValidDate(newStartDate) || !isValidDate(newEndDate)) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                    return;
                }

                for (String[] contract : contractData) {
                    if (contract[0].equals(contractId)) {
                        contract[2] = newStartDate;
                        contract[3] = newEndDate;
                        contract[4] = newTerms;
                        break;
                    }
                }

                
                saveContractData();
                refreshTable();
                JOptionPane.showMessageDialog(this, "Contract updated successfully!"); // Success message
            }
        } else {
            JOptionPane.showMessageDialog(this, "No contract found for the selected supplier.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "No supplier selected.");
    }
}
}
