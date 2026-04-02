package com.alsaineybarry.tickets.auth;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter
public class UserAuthResponse {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedDate;
}