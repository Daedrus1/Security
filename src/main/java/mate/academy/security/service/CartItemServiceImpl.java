package mate.academy.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.mapper.ShoppingCartMapper;
import mate.academy.security.model.CartItem;
import mate.academy.security.repository.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto updateQuantity(Long userId, Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new EntityNotFoundException(
                "Cart item not found with id " + cartItemId
        ));
        if (!cartItem.getShoppingCart().getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot modify another user's cart item");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toShoppingCartDto(cartItem.getShoppingCart());
    }

    @Override
    public void deleteItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart item not found with id " + cartItemId)
                );

        if (!cartItem.getShoppingCart().getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot delete another user's cart item");
        }

        cartItemRepository.delete(cartItem);
    }
}
