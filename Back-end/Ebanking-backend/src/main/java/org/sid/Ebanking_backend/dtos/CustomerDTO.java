package org.sid.Ebanking_backend.dtos;


import lombok.*;


@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}
