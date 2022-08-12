package domain.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Location {
    private String country;
    private String city;
    private String address;

    @Override
    public String toString() {
        return "Address: " +
                country + ", " +
                city + ", " +
                address;
    }
}
