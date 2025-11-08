public class PantryItem implements Identifiable {
    // TODO: Fix this. change to private and put setters
    // Protected fields so child classes can access.
    protected String id;
    protected String name;
    protected String category;
    protected int quantity;
    protected String unit;
    protected int threshold;

    public PantryItem(String id, String name, String category, int quantity, String unit, int threshold) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.threshold = threshold;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public int getThreshold() {
        return threshold;
    }

    // Reduce quantity by a certain amount.
    public void consume(int amount) {
        // Check for invalid negative amount.
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        // Check not consuming more than we have.
        if (amount > quantity) {
            throw new IllegalArgumentException("Not enough stock to consume");
        }
        // Subtract amount from quantity.
        quantity -= amount;
    }

    // Increase quantity by a certain amount.
    public void restock(int amount) {
        // Check for invalid negative amount.
        if (amount < 0) { throw new IllegalArgumentException("Amount cannot be negative"); }
        // Add amount to quantity.
        quantity += amount;
    }

    // Check if stock is below threshold.
    public boolean isLowStock() {
        return quantity < threshold;
    }
}
