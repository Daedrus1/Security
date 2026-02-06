package mate.academy.security.mapper;

import java.util.Set;
import mate.academy.security.dto.OrderDto;
import mate.academy.security.dto.OrderItemDto;
import mate.academy.security.model.Order;
import mate.academy.security.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderDto toDto(Order order);

    Set<OrderDto> toDtoSet(Set<Order> orders);
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toItemDto(OrderItem orderItem);

    Set<OrderItemDto> toItemDtoSet(Set<OrderItem> orderItems);
}
