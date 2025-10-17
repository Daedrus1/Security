package mate.academy.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequestDto {
    @Email
    private String email;
    @Size(min=8, max=32)
    private String password;
    @Size(min=8, max=32)
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
