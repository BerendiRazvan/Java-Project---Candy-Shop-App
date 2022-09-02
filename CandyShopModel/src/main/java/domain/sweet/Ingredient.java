package domain.sweet;

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
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INGREDIENTS")
@NamedQueries({
        @NamedQuery(name = Ingredient.FIND_ALL, query = "SELECT i FROM Ingredient i ORDER BY i.id"),
        @NamedQuery(name = Ingredient.FIND_BY_ID, query = "SELECT i FROM Ingredient i WHERE i.id = :ingredientId ORDER BY i.id"),
        @NamedQuery(name = Ingredient.FIND_BY_NAME, query = "SELECT i FROM Ingredient i WHERE i.name = :ingredientName ORDER BY i.id")
})
@Access(AccessType.FIELD)
public class Ingredient implements Serializable {
    public static final String FIND_ALL = "Ingredient.findAll";
    public static final String FIND_BY_ID = "Ingredient.findById";
    public static final String FIND_BY_NAME = "Ingredient.findByName";
    @Id
    @Column(name = "ID", unique = true)
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PRICE")
    private double price;

    @Column(name = "AMOUNT")
    private int amount;

    @Override
    public String toString() {
        return "(Id:" + id + ") " + name + " - " + price + "$ ";
    }
}
