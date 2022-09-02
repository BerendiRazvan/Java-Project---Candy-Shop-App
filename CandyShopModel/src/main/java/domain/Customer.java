package domain;

import domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Column;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMERS")
@NamedQueries({
        @NamedQuery(name = Customer.FIND_ALL, query = "SELECT c FROM Customer c ORDER BY c.id"),
        @NamedQuery(name = Customer.FIND_BY_ID, query = "SELECT c FROM Customer c WHERE c.id = :customerId ORDER BY c.id"),
        @NamedQuery(name = Customer.FIND_BY_EMAIL, query = "SELECT c FROM Customer c WHERE c.email = :customerEmail ORDER BY c.id")
})
@Access(AccessType.FIELD)
public class Customer implements Serializable {
    public static final String FIND_ALL = "Customer.findAll";
    public static final String FIND_BY_ID = "Customer.findById";
    public static final String FIND_BY_EMAIL = "Customer.findByEmail";

    @Id
    @Column(name = "ID", unique = true)
    private long id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @OneToOne
    private Location location;

    @Override
    public String toString() {
        return firstName + " " + lastName +
                "\nEmail: " + email +
                "\nPhone: " + phoneNumber +
                "\n" + location;
    }
}
