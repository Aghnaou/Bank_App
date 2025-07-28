package org.sid.Ebanking_backend.repositories;

import org.sid.Ebanking_backend.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomarRepository extends JpaRepository<Customer,Long> {
    List<Customer> findByNameContains(String keyword);
}