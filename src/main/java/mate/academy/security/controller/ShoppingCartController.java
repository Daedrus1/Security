package mate.academy.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.CartItemRequestDto;
import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Carts", description = "Endpoints for managing Shopping Carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get current user's shopping cart")
    public ShoppingCartDto getShoppingCart(@RequestParam Long userId) {
        return shoppingCartService.getShoppingCart(userId);
    }

    @PostMapping
    @Operation(summary = "Add a book to the user's shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartDto addItem(
            @RequestParam Long userId,
            @Valid @RequestBody CartItemRequestDto requestDto
    ) {
        return shoppingCartService.addItemToCart(userId, requestDto);
    }

    @DeleteMapping
    @Operation(summary = "Clear all items from the shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear(@RequestParam Long userId) {
        shoppingCartService.clearCart(userId);
    }
}
