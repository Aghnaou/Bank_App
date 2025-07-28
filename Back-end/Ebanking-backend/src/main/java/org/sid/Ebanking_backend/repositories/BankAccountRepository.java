package org.sid.Ebanking_backend.repositories;

import org.sid.Ebanking_backend.Entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
