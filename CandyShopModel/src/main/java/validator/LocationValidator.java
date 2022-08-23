package validator;

import domain.location.Location;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocationValidator {
    private static final int MINIMUM_ADDRESS_LENGTH = 10;
    private static final int MINIMUM_CITY_LENGTH = 3;
    private static final int MINIMUM_COUNTRY_LENGTH = 3;

    public String validateLocationCountry(String country) {
        if (country.length() < MINIMUM_COUNTRY_LENGTH)
            return "Invalid country!\n";
        return "";
    }

    public String validateLocationCity(String city) {
        if (city.length() < MINIMUM_CITY_LENGTH)
            return "Invalid city!\n";
        return "";
    }

    public String validateLocationAddress(String address) {
        if (address.length() < MINIMUM_ADDRESS_LENGTH)
            return "Invalid address!\n";
        return "";
    }

    public boolean isValidLocation(Location location) {
        return validateLocation(location).equals("");
    }

    public String validateLocation(Location location) {
        if (location == null) return "Location can not be null!";
        return validateLocationCountry(location.getCountry()) +
                validateLocationCity(location.getCity()) +
                validateLocationAddress(location.getAddress());
    }
}
