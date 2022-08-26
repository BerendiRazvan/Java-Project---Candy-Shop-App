package validator;

import domain.location.Location;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class LocationValidator {
    private static final int MINIMUM_ADDRESS_LENGTH = 10;
    private static final int MINIMUM_CITY_LENGTH = 3;
    private static final int MINIMUM_COUNTRY_LENGTH = 3;

    public boolean isValidLocation(Location location) {
        return validateLocation(location).isEmpty();
    }

    public List<String> validateLocation(Location location) {
        if (location == null) return List.of("Location can not be null!");
        List<String> errors = new ArrayList<>();

        String error = validateLocationCountry(location.getCountry());
        if (!error.matches(""))
            errors.add(error);

        error = validateLocationCity(location.getCity());
        if (!error.matches(""))
            errors.add(error);

        error = validateLocationAddress(location.getAddress());
        if (!error.matches(""))
            errors.add(error);

        return errors;
    }

    private String validateLocationCountry(String country) {
        if (country.length() < MINIMUM_COUNTRY_LENGTH)
            return "Invalid country!\n";
        return "";
    }

    private String validateLocationCity(String city) {
        if (city.length() < MINIMUM_CITY_LENGTH)
            return "Invalid city!\n";
        return "";
    }

    private String validateLocationAddress(String address) {
        if (address.length() < MINIMUM_ADDRESS_LENGTH)
            return "Invalid address!\n";
        return "";
    }
}
