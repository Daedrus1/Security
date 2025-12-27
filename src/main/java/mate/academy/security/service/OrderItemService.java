package mate.academy.security.service;

import java.util.List;
import mate.academy.security.dto.OrderItemDto;

public interface OrderItemService {
    List<OrderItemDto> getItems(Long orderId);

    OrderItemDto getItem(Long orderId, Long itemId);
}
