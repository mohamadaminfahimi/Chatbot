package org.aryanoor.view;

import org.aryanoor.model.IAM;
import org.aryanoor.model.OpenRouterChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame(OpenRouterChat chatBot) {
        super("Login to system");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setOpaque(true);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("User Name : "), gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password : "), gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(loginButton, gbc);

        gbc.gridy = 3;
        add(messageLabel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleLogin(chatBot);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin(OpenRouterChat chatBot) throws IOException {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        IAM newUser = new IAM(user, pass);
        if (newUser.login(user, pass)) {
            messageLabel.setText("✅Successful login !");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            new ChatView(chatBot);
            dispose();
        } else {
            messageLabel.setText("❌The username or password is incorrect.");

        }
    }
}
