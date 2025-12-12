package mate.academy.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.CartItemRequestDto;
import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Carts", description = "Endpoints for managing Shopping Carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get current user's shopping cart")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userEmail = user.getUsername();
        return shoppingCartService.getShoppingCart(userEmail);
    }

    @PostMapping
    @Operation(summary = "Add a book to the user's shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartDto addItem(
            Authentication authentication,
            @Valid @RequestBody CartItemRequestDto requestDto
    ) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userEmail = user.getUsername();
        return shoppingCartService.addItemToCart(userEmail, requestDto);
    }

    @DeleteMapping
    @Operation(summary = "Clear all items from the shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear(@RequestParam Long userId) {
        shoppingCartService.clearCart(userId);
    }

}
