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
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseGet(() -> createCart(userId));
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }


    @Override
    @Transactional
    public ShoppingCartDto addItemToCart(Long userId, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createCart(userId));
        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id " + cartItemRequestDto.getBookId()
                ));
        CartItem cartItem = cartItemRepository.findByShoppingCartIdAndBookId(
                shoppingCart.getId(), book.getId()).orElseGet(() -> {
            CartItem newCartItem = new CartItem();
            newCartItem.setBook(book);
            newCartItem.setQuantity(0);
            newCartItem.setShoppingCart(shoppingCart);
            return newCartItem;
        });
        cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.getQuantity());
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user id " + userId)
                );
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

    }

    private ShoppingCart createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);

    }
}
