package org.aryanoor.view;

import org.aryanoor.model.IAM;
import org.aryanoor.model.OpenRouterChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

public class SignUpForm extends JFrame {
    private final JPasswordField passwordField;
    private final JTextField usernameField;

    public SignUpForm(OpenRouterChat chatBot) {
        super("Sign Up");

        usernameField = createPlaceholderTextField();
        passwordField = createPlaceholderPasswordField("Password");
        JPasswordField confirmPasswordField = createPlaceholderPasswordField("Confirm Password");

        JButton signUpButton = new JButton("Sign Up");
        JLabel messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setOpaque(true);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        // Username with label
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createLabeledField("Username:", usernameField), gbc);

        // Password with label and toggle
        gbc.gridy++;
        add(createPasswordFieldWithToggle(), gbc);

        // Confirm Password
        gbc.gridy++;
        add(createLabeledField("Confirm:", confirmPasswordField), gbc);

        // Sign Up Button
        gbc.gridy++;
        add(signUpButton, gbc);

        // Message label
        gbc.gridy++;
        add(messageLabel, gbc);

        // Button Action
        signUpButton.addActionListener(_ -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("❗ Please fill in all fields.");
            } else if (!password.equals(confirmPassword)) {
                messageLabel.setText("❗ Passwords do not match.");
            } else {
                messageLabel.setText("✅ Signed up successfully!");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                IAM newUser = new IAM(username, password);
                try {
                    newUser.signUp();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                new ChatView(chatBot);
                dispose();
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 300);
        setLocationRelativeTo(null); // وسط صفحه
        setVisible(true);
    }

    private JTextField createPlaceholderTextField() {
        JTextField field = new JTextField("Username");
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals("Username")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText("Username");
                }
            }
        });

        return field;
    }

    private JPasswordField createPlaceholderPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);

        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                String current = new String(field.getPassword());
                if (current.equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('●');
                }
            }

            public void focusLost(FocusEvent e) {
                String current = new String(field.getPassword());
                if (current.isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                    field.setEchoChar((char) 0);
                }
            }
        });

        return field;
    }

    private JPanel createLabeledField(String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(70, 25));
        field.setPreferredSize(new Dimension(220, 25));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.add(label);
        panel.add(field);

        return panel;
    }

    private JPanel createPasswordFieldWithToggle() {
        JLabel label = new JLabel("Password:");
        label.setPreferredSize(new Dimension(70, 25));

        JPasswordField field = passwordField;
        JButton toggleButton = new JButton("Show");
        toggleButton.setPreferredSize(new Dimension(70, 25));

        final boolean[] isVisible = {true};

        toggleButton.addActionListener(_ -> {
            String current = new String(field.getPassword());
            if (current.equals("Password")) return;

            if (isVisible[0]) {
                field.setEchoChar('●');
                toggleButton.setText("Show");
            } else {
                field.setEchoChar((char) 0);
                toggleButton.setText("Hide");
            }
            isVisible[0] = !isVisible[0];
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        field.setPreferredSize(new Dimension(180, 25));
        panel.add(label);
        panel.add(field);
        panel.add(toggleButton);

        return panel;
    }
}
