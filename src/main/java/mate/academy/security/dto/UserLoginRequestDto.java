package mate.academy.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min=8, max=32)
    private String password;

    public @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 8, max = 32) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, max = 32) String password) {
        this.password = password;
    }
}
