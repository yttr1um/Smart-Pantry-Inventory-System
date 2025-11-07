import java.time.LocalDate; // Import LocalDate for expiry

// A pantry item that also has an expiry date.
public class PerishableItem extends PantryItem implements Expirable {
    private LocalDate expiryDate; // The expiry date

    // Constructor calls the parent and sets expiry date.
    public PerishableItem(String id, String name, String category, int quantity, String unit, int threshold, LocalDate expiryDate) {
        super(id, name, category, quantity, unit, threshold); // Call parent constructor
        this.expiryDate = expiryDate; // Save expiry date
    }

    // Return the expiry date
    public LocalDate getExpiryDate() { return expiryDate; }

    // Check if the item expires within a certain number of days.
    public boolean isAboutToExpire(int days) {
        // If no expiry date, cannot be about to expire.
        if (expiryDate == null) { return false; }
        // Today and the future limit date.
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(days);
        // True only if expiry is between today and the limit.
        return !expiryDate.isBefore(today) && !expiryDate.isAfter(limit);
    }
}
