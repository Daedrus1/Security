package mate.academy.security.service;

import mate.academy.security.dto.CartItemRequestDto;
import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.model.ShoppingCart;
import mate.academy.security.model.User;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addItemToCart(Long userId, CartItemRequestDto cartItemRequestDto);

    void clearCart(Long userId);

    ShoppingCartDto updateQuantity(Long userId, Long cartItemId, int quantity);

    void deleteItem(Long userId, Long cartItemId);

    ShoppingCart createCart(User user);
}
