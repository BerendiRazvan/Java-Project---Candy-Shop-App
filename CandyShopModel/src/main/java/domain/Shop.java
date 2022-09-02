package domain;

import domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SHOPS")
public class Shop implements Serializable {
    @Id
    @Column(name = "ID", unique = true)
    private long id;
    @Column(name = "NAME")
    private String name;

    @OneToOne
    private Location location;

    @Override
    public String toString() {
        return "\n\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + name +
                "\n" + "-".repeat(100) + "\n" +
                "\t".repeat(5) + location +
                "\n" + "-".repeat(100) + "\n";
    }
}
