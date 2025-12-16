package mate.academy.security.service;

import mate.academy.security.dto.CreateOrderRequestDto;
import mate.academy.security.dto.OrderDto;
import mate.academy.security.dto.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
   OrderDto placeOrder(CreateOrderRequestDto requestDto);

   Page<OrderDto> getMyOrders(Pageable pageable);

   OrderDto updateStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);
}
