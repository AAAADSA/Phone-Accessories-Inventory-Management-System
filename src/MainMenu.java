package com.mycompany.phoneaccessories;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class MainMenu extends JFrame {

    private final JPanel mainPanel;



public MainMenu() {
    setTitle("Main Menu");

   
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

   
    showMainMenu();
}


    private void showMainMenu() {
        mainPanel.removeAll(); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

      
        JLabel titleLabel = new JLabel("Main Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

      
        JButton staffButton = createButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(staffButton, gbc);

        
        JButton registerButton = createButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(registerButton, gbc);

   
        JButton quitButton = createButton("Quit");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(quitButton, gbc);

       
        staffButton.addActionListener(e -> loginMenu());
        registerButton.addActionListener(e -> registerMenu());
        quitButton.addActionListener(e -> System.exit(0));

     
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void loginMenu() {
        mainPanel.removeAll(); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        
        JLabel staffIDLabel = new JLabel("Staff ID:");
        staffIDLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(staffIDLabel, gbc);

        JTextField staffIDField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(staffIDField, gbc);

        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(passwordField, gbc);

      
        JButton loginButton = createButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(loginButton, gbc);

   
        JButton backButton = createButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(backButton, gbc);

        
        loginButton.addActionListener(e -> {
            String staffID = staffIDField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String staffName = validateLogin(staffID, password);
            if (staffName!=null) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new StaffMenu(staffID,staffName).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Staff ID or Password!");
            }
        });

      
        backButton.addActionListener(e -> showMainMenu());

   
        mainPanel.revalidate();
        mainPanel.repaint();
    }

  
    private void registerMenu() {
        mainPanel.removeAll(); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);
        
        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(nameField, gbc);
        
       
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.CYAN);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(phoneField, gbc);
        
       
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(positionLabel, gbc);
        
        String[] positions={"Warehouse Manager","Inventory Manager","Order Processor","IT Support"};
        
        JComboBox<String> positionComboBox = new JComboBox<>(positions);
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(positionComboBox, gbc);

        
         
        JLabel staffIDLabel = new JLabel("staffID:");
        staffIDLabel.setForeground(Color.CYAN);
        gbc.gridx = 1;
        gbc.gridy = 5;
        mainPanel.add(staffIDLabel, gbc);

        JTextField staffIDField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        mainPanel.add(staffIDField, gbc);
        
      
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 9;
        mainPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 10;
        mainPanel.add(emailField, gbc);

        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.CYAN);
        gbc.gridx = 1;
        gbc.gridy = 9;
        mainPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 10;
        mainPanel.add(passwordField, gbc);

       
        JButton registerButton = createButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 13;
        mainPanel.add(registerButton, gbc);

       
        JButton backButton = createButton("Back");
        gbc.gridx = 1;
        gbc.gridy = 13;
        mainPanel.add(backButton, gbc);

        
        registerButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String name = nameField.getText().trim(); 
            String phone = phoneField.getText().trim(); 
            String position = (String)positionComboBox.getSelectedItem(); 
            String staffID = staffIDField.getText().trim(); 
            
            if (email.isEmpty() || password.isEmpty()|| name.isEmpty()|| phone.isEmpty()|| position.isEmpty()|| staffID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email and Password cannot be empty!");
            } else if (isstaffIDRegistered(staffID)) {
                JOptionPane.showMessageDialog(this, "Staff ID already registered!");
            } else {
                registerNewUser(email, password, name, phone, position, staffID);
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                showMainMenu();
            }
        });

      
        backButton.addActionListener(e -> showMainMenu());

     
        mainPanel.revalidate();
        mainPanel.repaint();
    }

 
private String validateLogin(String staffID, String password) {
    File file = new File("staff.txt");
    String staffName = null;

    try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                String[] credentials = line.split("\\|");

               
                if (credentials.length >= 3) {
                    String storedStaffID = credentials[0].trim();
                    String storedPassword = credentials[1].trim();
                    staffName = credentials[2].trim();

                    
                    if (storedStaffID.equals(staffID) && storedPassword.equals(password)) {
                        return staffName;
                    }
                }
            }
        }
    } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(this, "Staff file not found!", "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    return null;
}



    
    private boolean isstaffIDRegistered(String staffID) {
        File file = new File("staff.txt");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] credentials = scanner.nextLine().split("\\|");
                String storedStaffID = credentials[0].trim();

                if (storedStaffID.equals(staffID)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Staff file not found!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return false;
    }


    private void registerNewUser(String email, String password, String name,String phone, String role, String staffID) {
        File file = new File("staff.txt");

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(staffID + "|" + password + "|" + name + "|" + phone + "|" + role + "|" + email + "\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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

 
}



