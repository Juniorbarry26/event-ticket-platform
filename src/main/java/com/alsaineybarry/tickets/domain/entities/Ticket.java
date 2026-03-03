package com.alsaineybarry.tickets.domain.entities;

import com.alsaineybarry.tickets.domain.enums.QrCodeStatusEnum;
import com.alsaineybarry.tickets.domain.enums.TIcketStatusEnum;
import com.alsaineybarry.tickets.domain.enums.TicketValidationMethodEnum;
import com.alsaineybarry.tickets.domain.enums.TicketValidationStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TIcketStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaser_id", nullable = false)
    private User purchaser;

    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    @Column(name = "validations")
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketValidation> validations = new ArrayList<>();

    @Column(name = "qr_codes")
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<QrCode> qrCodes = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && status == ticket.status && Objects.equals(purchaseDate, ticket.purchaseDate) && Objects.equals(createdAt, ticket.createdAt) && Objects.equals(updatedAt, ticket.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, purchaseDate, createdAt, updatedAt);
    }
}


