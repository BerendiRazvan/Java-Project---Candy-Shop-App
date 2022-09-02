package domain.order;

import domain.Customer;
import domain.Shop;
import domain.sweet.Ingredient;
import domain.sweet.Sweet;
import domain.utils.LocalDateTimeConverter;
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
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Convert;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
@NamedQueries({
        @NamedQuery(name = Order.FIND_ALL, query = "SELECT o FROM Order o ORDER BY o.id"),
        @NamedQuery(name = Order.FIND_BY_ID, query = "SELECT o FROM Order o WHERE o.id = :orderId ORDER BY o.id")
})
@Access(AccessType.FIELD)
public class Order implements Serializable {
    public static final String FIND_ALL = "Order.findAll";
    public static final String FIND_BY_ID = "Order.findById";
    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Id
    @Column(name = "ID", unique = true)
    private long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Sweet, Integer> orderedSweets;
    @OneToOne
    private Customer customer;
    @OneToOne
    private Shop shop;
    @Column(name = "ORDER_DATE_TIME", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime orderDateTime;
    @Column(name = "ORDER_TYPE")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Column(name = "WAITING_TIME", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime waitingTime;

    @Override
    public String toString() {
        return "\n" + "-".repeat(100) + "\n" +
                "\t".repeat(10) + "Order no." + id + "\t" + orderDateTime.format(DateTimeFormatter
                .ofPattern("EEE dd.MM.yyyy HH:mm")) +
                "\n" + "-".repeat(100) + "\n" +
                "Customer: " + customer.getFirstName() + " " + customer.getLastName() + " | " +
                customer.getPhoneNumber() + " | " + customer.getEmail() +
                "\n" + customer.getLocation() +
                "\n\nCandy Shop: " + shop.getName() +
                "\n" + shop.getLocation() +
                "\n" + "-".repeat(100) + "\n" +
                "Ordered:\n" + formatForPrintingOrderedSweets() +
                "\n" + "-".repeat(100) + "\n" +
                "YOUR ORDER WILL READY FOR " + orderType.toString() + " AT: " +
                waitingTime.format(DateTimeFormatter.ofPattern("EEE dd.MM.yyyy HH:mm")) +
                "\n" + "-".repeat(100) + "\n";
    }

    private String formatForPrintingOrderedSweets() {
        StringBuilder orderedSweetsInfo = new StringBuilder();
        for (Sweet sweet : orderedSweets.keySet())
            orderedSweetsInfo.append(formatForPrintingSweet(sweet));
        return orderedSweetsInfo.toString();
    }

    private String formatForPrintingSweet(Sweet sweet) {
        return "\n(Id:" + sweet.getId() + ")" +
                " " + sweet.getSweetType().getName() +
                " - quantity: " +
                orderedSweets.get(sweet) +
                " - price: " +
                orderedSweets.get(sweet) +
                "*" +
                df.format(sweet.getTotalPrice()) +
                "$=" +
                df.format(orderedSweets.get(sweet) * sweet.getTotalPrice()) +
                "$" +
                "\nRecipe:" +
                formatForPrintingIngredients(sweet.getIngredientsList()) +
                "\nExtra:" +
                sweet.getExtraIngredients() +
                "\n";
    }

    private String formatForPrintingIngredients(List<Ingredient> ingredientsList) {
        return ingredientsList
                .stream()
                .map(ingredient -> "(Id:" + ingredient.getId() + ") " + ingredient.getName())
                .collect(Collectors.toList())
                .toString();
    }

}
