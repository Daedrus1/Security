package mate.academy.security.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShoppingCart {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new LinkedHashSet<>();
}
