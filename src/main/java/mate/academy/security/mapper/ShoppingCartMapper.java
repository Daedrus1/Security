package mate.academy.security.mapper;

import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);
}
