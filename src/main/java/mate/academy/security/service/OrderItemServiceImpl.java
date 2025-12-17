package mate.academy.security.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.OrderItemDto;
import mate.academy.security.mapper.OrderMapper;
import mate.academy.security.model.Order;
import mate.academy.security.model.OrderItem;
import mate.academy.security.model.User;
import mate.academy.security.repository.OrderItemRepository;
import mate.academy.security.repository.OrderRepository;
import mate.academy.security.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    @Override
    public List<OrderItemDto> getItems(Long orderId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

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
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new RuntimeException(
                        "Order item not found: " + itemId + " for order: " + orderId));

        return orderMapper.toItemDto(orderItem);
    }
}
