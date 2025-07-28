package org.sid.Ebanking_backend.mappers;

import org.sid.Ebanking_backend.Entities.*;
import org.sid.Ebanking_backend.dtos.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO=new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
//        customerDTO.setId(customer.getId());
//        customerDTO.setName(customer.getName());
//        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }
    public  Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount){
        SavingAccountDTO savingBankAccountDTO=new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    public SavingAccount fromSavingAccountDTO(SavingAccountDTO savingBankAccountDTO){
       SavingAccount savingAccount=new SavingAccount();
       BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
       savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
       return savingAccount;
    }

    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount){
       CurrentAccountDTO currentAccountDTO=new CurrentAccountDTO();
       BeanUtils.copyProperties(currentAccount,currentAccountDTO);
       currentAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
       currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
       return currentAccountDTO;
    }

    public CurrentAccount fromCurrentAccountDTO(CurrentAccountDTO currentBankAccountDTO){
      CurrentAccount currentAccount=new CurrentAccount();
      BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
      currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
      return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation fromAccountOperationDTO(AccountOperationDTO accountOperationDTO){
        AccountOperation accountOperation=new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO,accountOperation);
        return accountOperation;
    }

    public AccountHistoryDTO fromBankAccount(BankAccount bankAccount){
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        BeanUtils.copyProperties(bankAccount,accountHistoryDTO);
        return accountHistoryDTO;
    }
}
