package mk.ukim.finki.wp.magacin.models;

import lombok.Data;
import mk.ukim.finki.wp.magacin.models.enumerations.OrderStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "MagacinOrder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User user;

    @ManyToMany
    List<ShoppingCartItem> items;

    String firstName;

    String lastName;

    String email;

    String address;

    String country;

    String city;

    String zipCode;

    OrderStatus status;

    public Order(User user, List<ShoppingCartItem> items, String firstName, String lastName, String email, String address, String country, String city, String zipCode) {
        this.user = user;
        this.items = items;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.status = OrderStatus.PENDING;
    }

    public Order() {
    }
}
