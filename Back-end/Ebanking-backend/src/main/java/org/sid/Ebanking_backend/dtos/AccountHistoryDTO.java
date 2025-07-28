package org.sid.Ebanking_backend.dtos;

import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter @ToString
public class AccountHistoryDTO {
    private String id;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int Pagesize;
    private List<AccountOperationDTO> accountOperationDTOS;
}
