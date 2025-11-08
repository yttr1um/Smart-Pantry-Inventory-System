import java.time.LocalDate;

// This interface says that an object has an expiry date.
public interface Expirable {
    LocalDate getExpiryDate();
}
