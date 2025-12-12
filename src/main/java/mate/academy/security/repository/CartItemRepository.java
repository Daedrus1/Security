package mate.academy.security.repository;

import java.util.Optional;
import mate.academy.security.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartIdAndBookId(Long cartId, Long bookId);
}
