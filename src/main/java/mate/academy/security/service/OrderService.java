package mate.academy.security.service;

import java.util.List;
import mate.academy.security.dto.CreateOrderRequestDto;
import mate.academy.security.dto.OrderDto;
import mate.academy.security.dto.OrderItemDto;
import mate.academy.security.dto.UpdateOrderStatusRequestDto;
import mate.academy.security.exception.OrderProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
   OrderDto placeOrder(CreateOrderRequestDto requestDto) throws OrderProcessingException;

   Page<OrderDto> getMyOrders(Pageable pageable);

   OrderDto updateStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);

   List<OrderItemDto> getItems(Long orderId);

   OrderItemDto getItem(Long orderId, Long itemId);
}
