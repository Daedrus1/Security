package mate.academy.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart/items")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @Operation(summary = "Update the quantity of a specific cart item")
    @PutMapping("/{cartItemId}")
    public ShoppingCartDto updateQuantity(@RequestParam Long userId,
                                          @PathVariable Long cartItemId,
                                          @RequestParam @Min(1) int quantity) {
        return cartItemService.updateQuantity(userId, cartItemId, quantity);
    }

    @Operation(summary = "Remove a specific item from the shopping cart")
    @DeleteMapping("/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long userId,
                       @PathVariable Long cartItemId) {
        cartItemService.deleteItem(userId, cartItemId);
    }
}
