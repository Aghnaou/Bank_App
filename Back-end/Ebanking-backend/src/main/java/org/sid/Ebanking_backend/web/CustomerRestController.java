package org.sid.Ebanking_backend.web;


import org.sid.Ebanking_backend.Entities.Customer;
import org.sid.Ebanking_backend.dtos.CustomerDTO;
import org.sid.Ebanking_backend.exceptions.CustomerNotFoundException;
import org.sid.Ebanking_backend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class CustomerRestController {
   private BankAccountService bankAccountService;

    public CustomerRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return bankAccountService.searchCustomers(keyword);
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
      return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
      return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{id}")
    public CustomerDTO uptadeCustomer(@PathVariable(name = "id") Long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        CustomerDTO customerUpdated= bankAccountService.updateCustomer(customerDTO);
        return customerUpdated;
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long id){
        bankAccountService.deleteCustomer(id);
    }



}
