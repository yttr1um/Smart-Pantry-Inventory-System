import java.time.LocalDate; // Import LocalDate for dates

// This interface says that an object has an expiry date.
public interface Expirable {
    // Return the expiry date of the object.
    LocalDate getExpiryDate();
}
