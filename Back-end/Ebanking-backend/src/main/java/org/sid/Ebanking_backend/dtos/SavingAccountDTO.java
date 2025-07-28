package org.sid.Ebanking_backend.dtos;


import lombok.*;
import org.sid.Ebanking_backend.enums.AccountStatus;
import java.util.Date;


@AllArgsConstructor @NoArgsConstructor @Setter @Getter @ToString
public class SavingAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}

