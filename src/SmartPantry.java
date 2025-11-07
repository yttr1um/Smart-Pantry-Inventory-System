import javax.swing.SwingUtilities; // For starting GUI on the Event Dispatch Thread

// Program entry point that starts the app.
public class SmartPantry {
    public static void main(String[] args) {
        // Start Swing on the correct thread.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginWindow win = new LoginWindow(); // Create login window
                win.setVisible(true);                 // Show it
            }
        });
    }
}