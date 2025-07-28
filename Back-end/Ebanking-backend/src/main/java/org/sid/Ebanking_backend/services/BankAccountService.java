package org.sid.Ebanking_backend.services;

import org.sid.Ebanking_backend.Entities.BankAccount;
import org.sid.Ebanking_backend.Entities.CurrentAccount;
import org.sid.Ebanking_backend.Entities.Customer;
import org.sid.Ebanking_backend.Entities.SavingAccount;
import org.sid.Ebanking_backend.dtos.*;
import org.sid.Ebanking_backend.exceptions.BalanceNotSufficentException;
import org.sid.Ebanking_backend.exceptions.BankAccountNotFoundException;
import org.sid.Ebanking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentAccountDTO saveCurrentBankAccount(double initialBalance , double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingBankAccount(double initialBalance , double interestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
    List<BankAccountDTO> bankAccountList();
    List<AccountOperationDTO> accountHistory(String accountId);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);

    void debit(String accountId,double amount , String description ) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId,double amount , String description) throws BankAccountNotFoundException;
    void transfer(String acountIdsource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;


    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
