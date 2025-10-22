package mate.academy.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.UserRegistrationRequestDto;
import mate.academy.security.dto.UserResponseDto;
import mate.academy.security.exception.RegistrationException;
import mate.academy.security.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    @PostMapping("/registration")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto) throws RegistrationException {
return userService.register(userRegistrationRequestDto);
    }
}
