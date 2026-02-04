package mate.academy.security.mapper;

import mate.academy.security.dto.ShoppingCartDto;
import mate.academy.security.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems")
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);
}
