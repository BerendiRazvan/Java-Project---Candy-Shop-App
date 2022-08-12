package domain.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Location {
    private @Getter @Setter String country;
    private @Getter @Setter String city;
    private @Getter @Setter String address;

    @Override
    public String toString() {
        return "Address: " +
                country + ", " +
                city + ", " +
                address;
    }
}
