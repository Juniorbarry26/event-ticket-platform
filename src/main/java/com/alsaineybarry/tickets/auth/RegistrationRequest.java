package com.alsaineybarry.tickets.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(builder = RegistrationRequest.RegistrationRequestBuilder.class)
public class RegistrationRequest {
    @NotBlank(message = "First name is mandatory")
    @NotEmpty(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    @NotEmpty(message = "Last name is mandatory")
    private String lastName;
    @NotNull(message = "Email is mandatory")
    @NotEmpty(message = "Email is mandatory")
    private String email;
    @NotNull(message = "Password is mandatory")
    @NotEmpty(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
