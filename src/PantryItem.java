// A pantry item that can be identified by an ID.
public class PantryItem implements Identifiable {
    // Protected fields so child classes can access.
    protected String id;        // Item ID
    protected String name;      // Item name
    protected String category;  // Category (e.g., Snacks)
    protected int quantity;     // Quantity on hand
    protected String unit;      // Unit (e.g., pcs, kg)
    protected int threshold;    // Minimum desired stock level

    // Constructor to set all fields.
    public PantryItem(String id, String name, String category, int quantity, String unit, int threshold) {
        this.id = id;           // Save id
        this.name = name;       // Save name
        this.category = category; // Save category
        this.quantity = quantity; // Save quantity
        this.unit = unit;       // Save unit
        this.threshold = threshold; // Save threshold
    }

    // Return id
    public String getId() { return id; }
    // Return name
    public String getName() { return name; }
    // Return category
    public String getCategory() { return category; }
    // Return quantity
    public int getQuantity() { return quantity; }
    // Return unit
    public String getUnit() { return unit; }
    // Return threshold
    public int getThreshold() { return threshold; }

    // Reduce quantity by a certain amount.
    public void consume(int amount) {
        // Check for invalid negative amount.
        if (amount < 0) { throw new IllegalArgumentException("Amount cannot be negative"); }
        // Check not consuming more than we have.
        if (amount > quantity) { throw new IllegalArgumentException("Not enough stock to consume"); }
        // Subtract amount from quantity.
        quantity = quantity - amount;
    }

    // Increase quantity by a certain amount.
    public void restock(int amount) {
        // Check for invalid negative amount.
        if (amount < 0) { throw new IllegalArgumentException("Amount cannot be negative"); }
        // Add amount to quantity.
        quantity = quantity + amount;
    }

    // Check if stock is below threshold.
    public boolean isLowStock() { return quantity < threshold; }
}
