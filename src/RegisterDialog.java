import javax.swing.*;            // For Swing GUI
import java.awt.*;               // For layouts
import java.awt.event.*;         // For events
import java.util.ArrayList;      // For lists

// A dialog that lets the user register a new account.
public class RegisterDialog extends JDialog {
    public RegisterDialog(JFrame owner) {
        super(owner, "Register", true); // Modal dialog with a title
        buildUI(owner);                  // Build form
        pack();                          // Size to fit
        setLocationRelativeTo(owner);    // Center over the parent window
    }

    // Build the form controls
    private void buildUI(JFrame owner) {
        JPanel p = new JPanel(new GridLayout(5, 2, 6, 6));
        JTextField name = new JTextField(20);
        JTextField email = new JTextField(20);
        JTextField phone = new JTextField(20);
        JPasswordField pass = new JPasswordField(20);
        JButton ok = new JButton("Create");

        p.add(new JLabel("Name:")); p.add(name);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Phone:")); p.add(phone);
        p.add(new JLabel("Password:")); p.add(pass);
        p.add(new JLabel("")); p.add(ok);
        setContentPane(p);

        // When Create is clicked, save a new user
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String n = name.getText().trim();
                String em = email.getText().trim();
                String ph = phone.getText().trim();
                String pw = new String(pass.getPassword());
                if (n.length()==0 || em.length()==0 || pw.length()==0) {
                    JOptionPane.showMessageDialog(RegisterDialog.this, "Please fill all required fields");
                    return;
                }
                ArrayList<Housekeeper> users = Storage.readUsers();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getEmail().equalsIgnoreCase(em)) {
                        JOptionPane.showMessageDialog(RegisterDialog.this, "Email already registered");
                        return;
                    }
                }
                String id = Storage.makeId("U");
                Housekeeper u = new Housekeeper(id, n, em, ph, pw);
                users.add(u);
                Storage.writeUsers(users);
                JOptionPane.showMessageDialog(RegisterDialog.this, "Registration successful. You can log in now.");
                dispose();
            }
        });
    }
}