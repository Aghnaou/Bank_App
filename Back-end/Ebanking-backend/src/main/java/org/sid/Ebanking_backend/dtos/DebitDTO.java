package org.sid.Ebanking_backend.dtos;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter @ToString
public class DebitDTO {
    public String id;
    public double amount;
    public String description;
}
