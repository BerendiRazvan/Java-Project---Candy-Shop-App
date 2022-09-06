package domain.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LOCATIONS")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "CITY")

    private String city;
    @Column(name = "ADDRESS")

    private String address;

    @Override
    public String toString() {
        return "Address: " +
                country + ", " +
                city + ", " +
                address;
    }
}
