package mate.academy.security.mapper;

import java.util.Set;
import mate.academy.security.dto.OrderDto;
import mate.academy.security.dto.OrderItemDto;
import mate.academy.security.model.Order;
import mate.academy.security.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);

    Set<OrderDto> toDtoSet(Set<Order> orders);

    OrderItemDto toItemDto(OrderItem orderItem);

    Set<OrderItemDto> toItemDtoSet(Set<OrderItem> orderItems);
}
