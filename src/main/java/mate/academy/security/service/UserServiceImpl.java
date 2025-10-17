package mate.academy.security.service;

import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.UserRegistrationRequestDto;
import mate.academy.security.dto.UserResponseDto;
import mate.academy.security.exception.RegistrationException;
import mate.academy.security.mapper.UserMapper;
import mate.academy.security.model.User;
import mate.academy.security.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto) throws RegistrationException {
        if (userRepository.findByEmail(userRegistrationRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can t register user");
        }
        User user = new User();
        user.setEmail(userRegistrationRequestDto.getEmail());
        user.setPassword(userRegistrationRequestDto.getPassword());
        user.setFirstName(userRegistrationRequestDto.getFirstName());
        user.setLastName(userRegistrationRequestDto.getLastName());
        user.setShippingAddress(userRegistrationRequestDto.getShippingAddress());
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
