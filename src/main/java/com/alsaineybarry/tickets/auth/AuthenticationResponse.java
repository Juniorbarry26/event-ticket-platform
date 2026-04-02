package com.alsaineybarry.tickets.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthenticationResponse {
    private String token;
    private UserAuthResponse user;
}
