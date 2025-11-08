import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginWindow extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginWindow() {
        super("Smart Pantry – Login");   // Set the window title
        Storage.ensureFiles();            // Make sure data files exist
        buildUI();                        // Build the form
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();                           // Size to fit contents
        setLocationRelativeTo(null);      // Center on screen
    }

    // Build the simple login form
    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 6, 6));
        panel.add(new JLabel("Email:"));
        emailField = new JTextField(20);
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        panel.add(passwordField);
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        panel.add(loginBtn);
        panel.add(registerBtn);
        setContentPane(panel);

        // When user clicks Login
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        // When user clicks Register
        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: put comments and fix variable names
                RegisterDialog dlg = new RegisterDialog(LoginWindow.this);
                dlg.setVisible(true);
            }
        });
    }

    // Try to log the user in
    private void doLogin() {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());
        ArrayList<Housekeeper> users = Storage.readUsers();
        Housekeeper found = null;
        for (Housekeeper u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                found = u;
                break;
            }
        }
        if (found == null) {
            JOptionPane.showMessageDialog(this, "User not found");
            return;
        }

        if (!found.getPassword().equals(pass)) {
            JOptionPane.showMessageDialog(this, "Wrong password");
            return;
        }

        DashboardWindow dash = new DashboardWindow(found);
        dash.setVisible(true);
        dispose(); // close current window
    }
}
