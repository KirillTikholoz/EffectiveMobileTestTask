package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUserDto {
    @Email
    @JsonProperty("email")
    private String email;
    @NotEmpty
    @JsonProperty("password")
    private String password;
    @NotEmpty
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
