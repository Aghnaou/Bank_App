package org.sid.Ebanking_backend.dtos;

import jakarta.persistence.*;
import lombok.*;
import org.sid.Ebanking_backend.Entities.BankAccount;
import org.sid.Ebanking_backend.enums.OperationType;

import java.util.Date;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter @ToString
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
