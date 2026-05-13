package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
public class TicketPurchaseRequestDto {
    @NotNull
    @Positive
    private Integer quantity;
    
    @NotNull
    private BigDecimal price;
    
    @NotNull
    private String currency;
    
    private String paymentMethod;
    private String paymentToken;
    private String billingAddress;
    private String cardLastFour;
}
