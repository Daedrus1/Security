package mate.academy.security.service;

import mate.academy.security.dto.CartItemRequestDto;
import mate.academy.security.dto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addItemToCart(Long userId, CartItemRequestDto cartItemRequestDto);

    void clearCart(Long userId);
}
