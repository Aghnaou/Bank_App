package org.sid.Ebanking_backend.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.sid.Ebanking_backend.enums.OperationType;

import java.util.Date;

@Entity @AllArgsConstructor @NoArgsConstructor @Setter @Getter @ToString
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne
    private BankAccount bankAccount;
    private String description;
}
