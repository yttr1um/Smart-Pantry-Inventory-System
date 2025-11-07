// A Housekeeper is a specific type of User.
public class Housekeeper extends User {
    // Just use the same fields/behavior as User via inheritance.
    public Housekeeper(String id, String name, String email, String phone, String password) {
        // Call the parent (User) constructor to set everything.
        super(id, name, email, phone, password);
    }
}
