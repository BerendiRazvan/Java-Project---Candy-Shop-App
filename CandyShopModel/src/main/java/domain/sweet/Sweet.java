package domain.sweet;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Column;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SWEETS")
@NamedQueries({
        @NamedQuery(name = Sweet.FIND_ALL, query = "SELECT s FROM Sweet s ORDER BY s.id"),
        @NamedQuery(name = Sweet.FIND_BY_ID, query = "SELECT s FROM Sweet s WHERE s.id = :sweetId ORDER BY s.id")
})
@Access(AccessType.FIELD)
public class Sweet  implements Serializable {
    public static final String FIND_ALL = "Sweet.findAll";
    public static final String FIND_BY_ID = "Sweet.findById";
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Id
    @Column(name = "ID", unique = true)
    private long id;
    @Column(name = "SWEET_TYPE")
    @Enumerated(EnumType.STRING)
    private SweetType sweetType;
    @ManyToMany(targetEntity = Ingredient.class, fetch = FetchType.EAGER)
    @JoinTable(name = "RECIPE_INGREDIENTS")
    private List<Ingredient> ingredientsList;
    @ManyToMany(targetEntity = Ingredient.class, fetch = FetchType.EAGER)
    @JoinTable(name = "EXTRA_INGREDIENTS")
    private List<Ingredient> extraIngredients;
    @Column(name = "PRICE")
    private double price;
    public double getTotalPrice() {
        return getOriginalPrice() + getExtraPrice();
    }

    public double getOriginalPrice() {
        return price;
    }

    public double getExtraPrice() {
        return getExtraIngredients()
                .stream()
                .mapToDouble(Ingredient::getPrice)
                .sum();
    }

    @Override
    public String toString() {
        return "\n\n" + sweetType.getName() +
                "\nPrice: " + df.format(price) + "$ " +
                "\nRecipe:" +
                "\nIngredients: " + ingredientsList +
                "\nExtra ingredients to add: " + extraIngredients;
    }
}
