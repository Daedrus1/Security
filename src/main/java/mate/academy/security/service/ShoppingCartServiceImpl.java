package mate.academy.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.CartItemRequestDto;
import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.mapper.ShoppingCartMapper;
import mate.academy.security.model.Book;
import mate.academy.security.model.CartItem;
import mate.academy.security.model.ShoppingCart;
import mate.academy.security.model.User;
import mate.academy.security.repository.BookRepository;
import mate.academy.security.repository.CartItemRepository;
import mate.academy.security.repository.ShoppingCartRepository;
import mate.academy.security.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto getShoppingCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userEmail));
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id " + user.getId()
                ));
        return shoppingCartMapper.toShoppingCartDto(cart);
    }


    @Override
    public ShoppingCartDto addItemToCart(String userEmail, CartItemRequestDto cartItemRequestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userEmail));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id " + user.getId()
                ));
        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id " + cartItemRequestDto.getBookId()
                ));
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setBook(book);
                    newCartItem.setQuantity(cartItemRequestDto.getQuantity());
                    newCartItem.setShoppingCart(shoppingCart);
                    shoppingCart.getCartItems().add(newCartItem);
                    return newCartItem;
                });
        cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.getQuantity());
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public void clearCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user id " + userId)
                );
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

    }

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

    private ShoppingCart createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);

    }
}
