// A basic user type that implements Identifiable.
public class User implements Identifiable {
    // Private fields store user information.
    private String id;          // User ID
    private String name;        // User name
    private String email;       // User email
    private String phone;       // User phone
    private String password;    // User password (kept simple here)

    // Constructor to set values when creating a User.
    public User(String id, String name, String email, String phone, String password) {
        this.id = id;           // Save the id
        this.name = name;       // Save the name
        this.email = email;     // Save the email
        this.phone = phone;     // Save the phone
        this.password = password; // Save the password
    }

    // Return the user's unique id.
    public String getId() { return id; }

    // Return the user's name.
    public String getName() { return name; }

    // Return the user's email.
    public String getEmail() { return email; }

    // Return the user's phone.
    public String getPhone() { return phone; }

    // Return the user's password (for demo simplicity only).
    public String getPassword() { return password; }
}
