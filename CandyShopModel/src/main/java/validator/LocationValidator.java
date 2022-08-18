package validator;

import domain.location.Location;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocationValidator {
    private static final int MINIMUM_ADDRESS_LENGTH = 10;
    private static final int MINIMUM_CITY_LENGTH = 3;
    private static final int MINIMUM_COUNTRY_LENGTH = 3;

    public String locationCountryValidator(String country) {
        if (country.length() < MINIMUM_COUNTRY_LENGTH)
            return "Invalid country!\n";
        return "";
    }

    public String locationCityValidator(String city) {
        if (city.length() < MINIMUM_CITY_LENGTH)
            return "Invalid city!\n";
        return "";
    }

    public String locationAddressValidator(String address) {
        if (address.length() < MINIMUM_ADDRESS_LENGTH)
            return "Invalid address!\n";
        return "";
    }

    public boolean isValidLocation(Location location) {
        return locationValidation(location).equals("");
    }

    public String locationValidation(Location location) {
        if (location == null) return "Location can not be null!";
        return locationCountryValidator(location.getCountry()) +
                locationCityValidator(location.getCity()) +
                locationAddressValidator(location.getAddress());
    }
}
