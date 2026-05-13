package com.alsaineybarry.tickets.domain.entities;

import com.alsaineybarry.tickets.domain.entities.TicketValidationMethod;
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
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ticket_validations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketValidation {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "validator_id", nullable = false)
//    private User validator;

    @Column(name = "validation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketValidationStatusEnum validationStatus;

    @Column(name = "validation_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketValidationMethod validationMethod;

//    @Column(name = "validation_timestamp", nullable = false)
//    private LocalDateTime validationTimestamp;
//
//    @Column(name = "notes")
//    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketValidation that = (TicketValidation) o;
        return Objects.equals(id, that.id) && validationStatus == that.validationStatus && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, validationStatus, createdAt, updatedAt);
    }
}
