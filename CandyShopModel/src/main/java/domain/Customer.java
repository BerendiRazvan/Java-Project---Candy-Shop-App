package domain;

import domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Customer {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Location location;

    @Override
    public String toString() {
        return firstName + " " + lastName +
                "\nEmail: " + email +
                "\nPhone: " + phoneNumber +
                "\n" + location;
    }
}
