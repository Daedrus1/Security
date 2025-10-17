package mate.academy.security.mapper;

import mate.academy.security.dto.UserRegistrationRequestDto;
import mate.academy.security.dto.UserResponseDto;
import mate.academy.security.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegistrationRequestDto dto);

    UserResponseDto toDto(User user);
}
