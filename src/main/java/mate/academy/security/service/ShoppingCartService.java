package mate.academy.security.service;

import mate.academy.security.dto.CartItemRequestDto;
import mate.academy.security.dto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(String userEmail);

    ShoppingCartDto addItemToCart(String userEmail, CartItemRequestDto cartItemRequestDto);

    void clearCart(Long userId);

    ShoppingCartDto updateQuantity(Long userId, Long cartItemId, int quantity);

    void deleteItem(Long userId, Long cartItemId);
}
