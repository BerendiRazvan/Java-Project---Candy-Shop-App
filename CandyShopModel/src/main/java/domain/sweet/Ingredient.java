package domain.sweet;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Column;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INGREDIENTS")
@NamedQueries({
        @NamedQuery(name = Ingredient.FIND_ALL, query = "SELECT i FROM Ingredient i ORDER BY i.id"),
        @NamedQuery(name = Ingredient.FIND_BY_ID, query = "SELECT i FROM Ingredient i WHERE i.id = :ingredientId "),
        @NamedQuery(name = Ingredient.FIND_BY_NAME, query = "SELECT i FROM Ingredient i WHERE UPPER(i.name) = UPPER(:ingredientName)")
})
@Access(AccessType.FIELD)
public class Ingredient {
    public static final String FIND_ALL = "Ingredient.findAll";
    public static final String FIND_BY_ID = "Ingredient.findById";
    public static final String FIND_BY_NAME = "Ingredient.findByName";
    @Id
    @Column(name = "ID", unique = true)
    @EqualsAndHashCode.Include
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
