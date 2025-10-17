package mate.academy.security.service;

import mate.academy.security.dto.UserRegistrationRequestDto;
import mate.academy.security.dto.UserResponseDto;
import mate.academy.security.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto) throws RegistrationException;
}
