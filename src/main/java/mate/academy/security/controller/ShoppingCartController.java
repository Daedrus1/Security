package mate.academy.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.CartItemRequestDto;
import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.model.User;
import mate.academy.security.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Carts", description = "Endpoints for managing Shopping Carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get current user's shopping cart")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @Operation(summary = "Add a book to the user's shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartDto addItem(
            Authentication authentication,
            @Valid @RequestBody CartItemRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addItemToCart(user.getId(), requestDto);
    }

    @Operation(summary = "Update the quantity of a specific cart item")
    @PutMapping("/{cartItemId}")
    public ShoppingCartDto updateQuantity(Authentication authentication,
                                          @PathVariable @Positive Long cartItemId,
                                          @RequestParam @Positive int quantity) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateQuantity(user.getId(), cartItemId, quantity);
    }

    @Operation(summary = "Remove a specific item from the shopping cart")
    @DeleteMapping("/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication,
                       @PathVariable @Positive Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteItem(user.getId(), cartItemId);
    }

}
