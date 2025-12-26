package mate.academy.security.mapper;

import mate.academy.security.dto.CartItemDto;
import mate.academy.security.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemDto toCartItemDto(CartItem cartItem);
}
