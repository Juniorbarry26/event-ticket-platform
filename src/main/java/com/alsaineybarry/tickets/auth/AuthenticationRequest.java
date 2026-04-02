package com.alsaineybarry.tickets.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(builder = AuthenticationRequest.AuthenticationRequestBuilder.class)
public class AuthenticationRequest {
    @Email(message = "Email must be valid")
    @NotNull(message = "Email Is Mandatory")
    @NotEmpty(message = "Email Is Mandatory")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
