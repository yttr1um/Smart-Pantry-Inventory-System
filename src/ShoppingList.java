import java.time.LocalDate;          // For dates
import java.util.ArrayList;          // For list of entries

// A shopping list with multiple entries.
public class ShoppingList implements Identifiable {
    // One entry (row) in a shopping list.
    public static class Entry {
        String itemName;  // Item name
        int quantity;     // Quantity to buy
        String unit;      // Unit (e.g., pcs)
        String status;    // Status (NEW or PURCHASED)
        // Constructor to set values.
        public Entry(String itemName, int quantity, String unit) {
            this.itemName = itemName; // Save name
            this.quantity = quantity; // Save quantity
            this.unit = unit;         // Save unit
            this.status = "NEW";     // Default status
        }
    }

    private String id;                     // List ID
    private LocalDate dateCreated;         // When it was created
    private ArrayList<Entry> entries;      // All entries in the list

    // Constructor for ShoppingList.
    public ShoppingList(String id, LocalDate dateCreated) {
        this.id = id;                      // Save id
        this.dateCreated = dateCreated;    // Save date
        this.entries = new ArrayList<Entry>(); // Start with empty entries
    }

    // Return id
    public String getId() { return id; }
    // Return date created
    public LocalDate getDateCreated() { return dateCreated; }
    // Return entries list
    public ArrayList<Entry> getEntries() { return entries; }
}
