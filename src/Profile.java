package com.mycompany.phoneaccessories;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Profile extends JFrame {

    private String staffID;
    private String staffName;
    private JLabel labelID;
    private JLabel labelName;
    private JLabel labelPhone;
    private JLabel labelPosition;
    private JLabel labelEmail;

    
    public Profile(String staffID, String staffName) {
        this.staffID = staffID;
        this.staffName = staffName;
        displayProfile();  
    }

    
public void displayProfile() {
    String staffData = findStaffByID(staffID);

    if (staffData != null) {
        String[] staffDetails = staffData.split("\\|");

      
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

      
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);

        
        JFrame frame = new JFrame("Staff Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);  
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); 

        
             labelID = new JLabel("Staff ID: " + staffDetails[0]);
             labelName = new JLabel("Name: " + staffDetails[2]);
             labelPhone = new JLabel("Phone Number: " + staffDetails[3]);
             labelPosition = new JLabel("Position: " + staffDetails[4]);
             labelEmail = new JLabel("Email: " + staffDetails[5]);

         
            Font font = new Font("Arial", Font.BOLD, 16);
            labelID.setFont(font);
            labelName.setFont(font);
            labelPhone.setFont(font);
            labelPosition.setFont(font);
            labelEmail.setFont(font);

        
            labelID.setForeground(Color.CYAN);
            labelName.setForeground(Color.CYAN);
            labelPhone.setForeground(Color.CYAN);
            labelPosition.setForeground(Color.CYAN);
            labelEmail.setForeground(Color.CYAN);

           
            JPanel panel = new JPanel(new GridLayout(3, 2, 2, 2));
            panel.setBackground(Color.BLACK);
            panel.add(labelID);
            panel.add(labelName);
            panel.add(labelPhone);
            panel.add(labelPosition);
            panel.add(labelEmail);

           
            frame.add(panel, BorderLayout.CENTER);

          
            JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 4, 3));
            buttonPanel.setBackground(Color.BLACK);

         
            JButton editButton = new JButton("Edit Information");
            editButton.setBackground(Color.LIGHT_GRAY);
            editButton.setForeground(Color.BLACK);
            editButton.setFont(new Font("Arial", Font.BOLD, 14));
            editButton.setFocusPainted(false);
            editButton.addActionListener(e -> editInformation());

          
            JButton changePasswordButton = new JButton("Change Password");
            changePasswordButton.setBackground(Color.LIGHT_GRAY);
            changePasswordButton.setForeground(Color.BLACK);
            changePasswordButton.setFont(new Font("Arial", Font.BOLD, 14));
            changePasswordButton.setFocusPainted(false);
            changePasswordButton.addActionListener(e -> changePassword());

       
            JButton backButton = new JButton("Back");
            backButton.setBackground(Color.LIGHT_GRAY);
            backButton.setForeground(Color.BLACK);
            backButton.setFont(new Font("Arial", Font.BOLD, 14));
            backButton.setFocusPainted(false);
            backButton.addActionListener(e -> { new StaffMenu(staffID,staffName).setVisible(true);
    dispose();
            });
          
       
            buttonPanel.add(editButton);
            buttonPanel.add(changePasswordButton);
            buttonPanel.add(backButton);

           
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setLocationRelativeTo(null); 
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Staff ID not found.");
        }
    }


    
    private String findStaffByID(String staffID) {
        File file = new File("staff.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split("\\|");
                if (details[0].equals(staffID)) {
                    return line; 
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: File not found.");
        }
        return null; 
    }
    
  
private void editInformation() {
    
    String staffData = findStaffByID(staffID);
    if (staffData == null) {
        JOptionPane.showMessageDialog(null, "Staff ID not found.");
        return;
    }

    
    String[] staffDetails = staffData.split("\\|");

  
    JDialog editDialog = new JDialog(this, "Edit Information", true);
    editDialog.setLayout(new BorderLayout(10, 10));
    editDialog.setSize(450, 300);

  
    JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    
    JTextField nameField = new JTextField(staffDetails[2]);
    JTextField phoneField = new JTextField(staffDetails[3]);
    JTextField emailField = new JTextField(staffDetails[5]);

 
    formPanel.add(new JLabel("Name:"));
    formPanel.add(nameField);
    formPanel.add(new JLabel("Phone Number:"));
    formPanel.add(phoneField);
    formPanel.add(new JLabel("Email:"));
    formPanel.add(emailField);

 
    editDialog.add(formPanel, BorderLayout.CENTER);

   
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

  
    JButton saveButton = new JButton("Save");
    saveButton.setBackground(Color.BLACK);
    saveButton.setForeground(Color.CYAN);
    saveButton.setFont(new Font("Arial", Font.BOLD, 14));
    saveButton.setFocusPainted(false);
    saveButton.addActionListener(e -> {
        
        staffDetails[2] = nameField.getText();
        staffDetails[3] = phoneField.getText();
        staffDetails[5] = emailField.getText();
        
    
        updateStaffData(staffID, staffDetails);
        
        
    labelName.setText("Name: " + staffDetails[2]);
    labelPhone.setText("Phone Number: " + staffDetails[3]);
    labelEmail.setText("Email: " + staffDetails[5]);
        
     
        editDialog.dispose();
        

    });

   
    JButton cancelButton = new JButton("Cancel");
    cancelButton.setBackground(Color.BLACK);
    cancelButton.setForeground(Color.RED);
    cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
    cancelButton.setFocusPainted(false);
    cancelButton.addActionListener(e -> editDialog.dispose());

 
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

  
    editDialog.add(buttonPanel, BorderLayout.SOUTH);

 
    editDialog.setLocationRelativeTo(null);
    editDialog.setVisible(true);
}

private void updateStaffData(String staffID, String[] updatedDetails) {
    File file = new File("staff.txt");
    StringBuilder updatedData = new StringBuilder();
    try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] details = line.split("\\|");

            if (details[0].equals(staffID)) {
                updatedData.append(String.join("|", updatedDetails)).append("\n");
            } else {
                updatedData.append(line).append("\n");
            }
        }
    } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(null, "Error: File not found.");
        return;
    }

   
    try (PrintWriter writer = new PrintWriter(file)) {
        writer.write(updatedData.toString());
        JOptionPane.showMessageDialog(null, "Information updated successfully.");
    } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(null, "Error: Could not write to file.");
    }
}


private void changePassword() {
   
    String staffData = findStaffByID(staffID);
    if (staffData == null) {
        JOptionPane.showMessageDialog(null, "Staff ID not found.");
        return;
    }

  
    String[] staffDetails = staffData.split("\\|");

   
    JDialog passwordDialog = new JDialog(this, "Change Password", true);
    passwordDialog.setLayout(new BorderLayout(10, 10));
    passwordDialog.setSize(400, 300);


    JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

   
    JPasswordField currentPasswordField = new JPasswordField();
    JPasswordField newPasswordField = new JPasswordField();
    JPasswordField confirmPasswordField = new JPasswordField();

  
    formPanel.add(new JLabel("Current Password:"));
    formPanel.add(currentPasswordField);
    formPanel.add(new JLabel("New Password:"));
    formPanel.add(newPasswordField);
    formPanel.add(new JLabel("Confirm Password:"));
    formPanel.add(confirmPasswordField);

   
    passwordDialog.add(formPanel, BorderLayout.CENTER);

    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

    
    JButton saveButton = new JButton("Save");
    saveButton.setBackground(Color.BLACK);
    saveButton.setForeground(Color.CYAN);
    saveButton.setFont(new Font("Arial", Font.BOLD, 14));
    saveButton.setFocusPainted(false);
    saveButton.addActionListener(e -> {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        
        if (!staffDetails[1].equals(currentPassword)) {
            JOptionPane.showMessageDialog(null, "Current password is incorrect.");
            return;
        }

       
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "New passwords do not match.");
            return;
        }

     
        staffDetails[1] = newPassword;

    
        updateStaffData(staffID, staffDetails);

     
        passwordDialog.dispose();

        
        JOptionPane.showMessageDialog(null, "Password updated successfully.");
    });


    JButton cancelButton = new JButton("Cancel");
    cancelButton.setBackground(Color.BLACK);
    cancelButton.setForeground(Color.RED);
    cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
    cancelButton.setFocusPainted(false);
    cancelButton.addActionListener(e -> passwordDialog.dispose());

    
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

  
    passwordDialog.add(buttonPanel, BorderLayout.SOUTH);


    passwordDialog.setLocationRelativeTo(null);
    passwordDialog.setVisible(true);
   
}

}
