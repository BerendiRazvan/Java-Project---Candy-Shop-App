package builder;

import domain.location.Location;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import validator.LocationValidator;

@NoArgsConstructor
public class LocationBuilder {
    public Location build(String country, String city, String address) throws ValidationException {
        Location location = Location.builder()
                .country(country)
                .city(city)
                .address(address)
                .build();
        LocationValidator validator = new LocationValidator();

        if (validator.isValidLocation(location))
            return location;
        else
            throw new ValidationException(validator.validateLocation(location).stream()
                    .reduce("", (result, error) -> result + error));
    }
}
