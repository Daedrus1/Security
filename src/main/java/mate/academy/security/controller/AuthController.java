package mate.academy.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.UserLoginRequestDto;
import mate.academy.security.dto.UserLoginResponseDto;
import mate.academy.security.dto.UserRegistrationRequestDto;
import mate.academy.security.dto.UserResponseDto;
import mate.academy.security.exception.RegistrationException;
import mate.academy.security.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account if the email is not already registered"
    )
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto) throws RegistrationException {
        return userService.register(userRegistrationRequestDto);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticates a user using email and password"
    )
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
