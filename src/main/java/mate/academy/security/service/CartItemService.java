package mate.academy.security.service;

import mate.academy.security.dto.CartItemDto;
import mate.academy.security.dto.CartItemRequestDto;
import mate.academy.security.dto.ShoppingCartDto;

public interface CartItemService {
    ShoppingCartDto updateQuantity(Long userId, Long cartItemId, int quantity);
    void deleteItem(Long userId, Long cartItemId);
}
