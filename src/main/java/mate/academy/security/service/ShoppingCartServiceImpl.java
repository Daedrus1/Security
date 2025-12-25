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
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id " + userId
                ));
        return shoppingCartMapper.toShoppingCartDto(cart);
    }

    @Override
    public ShoppingCartDto addItemToCart(Long userId, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id " + userId
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
                    newCartItem.setQuantity(0);
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
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id " + userId
                ));

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with id " + cartItemId + " in cart " + cart.getId()
                ));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toShoppingCartDto(cart);
    }

    @Override
    public void deleteItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException(
                "Shopping cart not found for user id " + userId
        ));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with id " + cartItemId + " in cart " + shoppingCart.getId()
                ));

        cartItemRepository.delete(cartItem);

    }

    @Override
    public ShoppingCart createCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return shoppingCartRepository.save(cart);
    }
}
