package org.sid.Ebanking_backend.dtos;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @ToString
public class TransferRequestDTO {
    private String accountSource;
    private String accountDestination;
    private double amount;
    private String description;
}
