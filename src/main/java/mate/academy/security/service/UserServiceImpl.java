package mate.academy.security.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.UserRegistrationRequestDto;
import mate.academy.security.dto.UserResponseDto;
import mate.academy.security.exception.RegistrationException;
import mate.academy.security.mapper.UserMapper;
import mate.academy.security.model.Role;
import mate.academy.security.model.User;
import mate.academy.security.repository.RoleRepository;
import mate.academy.security.repository.UserRepository;
import mate.academy.security.security.role.RoleName;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto dto) throws RegistrationException {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setShippingAddress(dto.getShippingAddress());

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RegistrationException("ROLE_USER not found"));
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        shoppingCartService.createCart(savedUser);

        return userMapper.toDto(savedUser);
    }
}
