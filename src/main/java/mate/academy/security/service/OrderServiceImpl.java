package mate.academy.security.service;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.CreateOrderRequestDto;
import mate.academy.security.dto.OrderDto;
import mate.academy.security.dto.OrderItemDto;
import mate.academy.security.dto.UpdateOrderStatusRequestDto;
import mate.academy.security.exception.OrderProcessingException;
import mate.academy.security.mapper.OrderMapper;
import mate.academy.security.model.*;
import mate.academy.security.repository.*;
import mate.academy.security.security.status.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto placeOrder(CreateOrderRequestDto requestDto) throws OrderProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for user id: " + user.getId()));

        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new OrderProcessingException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(requestDto.getShippingAddress());

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());

            orderItems.add(orderItem);

            BigDecimal itemTotal = orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setOrderItems(orderItems);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Page<OrderDto> getMyOrders(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
        return orderRepository.findAllByUserId(user.getId(), pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateStatus(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }
    @Override
    public List<OrderItemDto> getItems(Long orderId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with email: " + email
                ));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));


        List<OrderItemDto> result = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            result.add(orderMapper.toItemDto(orderItem));
        }
        return result;
    }

    @Override
    public OrderItemDto getItem(Long orderId, Long itemId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found: " + itemId + " for order: " + orderId));

        return orderMapper.toItemDto(orderItem);
    }

}
