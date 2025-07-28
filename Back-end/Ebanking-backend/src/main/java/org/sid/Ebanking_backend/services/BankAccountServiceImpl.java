package org.sid.Ebanking_backend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.Ebanking_backend.Entities.*;
import org.sid.Ebanking_backend.dtos.*;
import org.sid.Ebanking_backend.enums.OperationType;
import org.sid.Ebanking_backend.exceptions.BalanceNotSufficentException;
import org.sid.Ebanking_backend.exceptions.BankAccountNotFoundException;
import org.sid.Ebanking_backend.exceptions.CustomerNotFoundException;
import org.sid.Ebanking_backend.mappers.BankAccountMapperImpl;
import org.sid.Ebanking_backend.repositories.AccountOperationRepository;
import org.sid.Ebanking_backend.repositories.BankAccountRepository;
import org.sid.Ebanking_backend.repositories.CustomarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private CustomarRepository customarRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;

//    public BankAccountServiceImpl(CustomarRepository customarRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository, BankAccountMapperImpl bankAccountMapper) {
//        this.customarRepository = customarRepository;
//        this.bankAccountRepository = bankAccountRepository;
//        this.accountOperationRepository = accountOperationRepository;
//        this.bankAccountMapper = bankAccountMapper;
//    }

    //Pour faire la journalisation on a utlisé l'API SLF4J ---> on a créer un objet logger
    //Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    // MAIS cette etape peut etre faite a  l'aide de lombok sans avoir écrir cette ligne de code !!!!!



    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving a new customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customarRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer=customarRepository.findById(customerId).orElse(null);
        if(customer==null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount bankAccount=new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCustomer(customer);
        bankAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount =bankAccountRepository.save(bankAccount);
        CurrentAccountDTO savedBankAccountDTO=bankAccountMapper.fromCurrentAccount(savedBankAccount);

        return savedBankAccountDTO;
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customarRepository.findById(customerId).orElse(null);
        if(customer==null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount =bankAccountRepository.save(savingAccount);
        SavingAccountDTO savedBankAccountDTO=bankAccountMapper.fromSavingAccount(savedBankAccount);

        return savedBankAccountDTO;
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers= customarRepository.findAll();
        List<CustomerDTO> customerDTOS=customers.stream()
                .map(cust->bankAccountMapper.fromCustomer(cust))
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account Not Found"));

        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            SavingAccountDTO savingAccountDTO=bankAccountMapper.fromSavingAccount(savingAccount);
            return savingAccountDTO;
        }
        else {
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            CurrentAccountDTO currentAccountDTO=bankAccountMapper.fromCurrentAccount(currentAccount);
            return  currentAccountDTO;
        }
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving a new customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customarRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
       customarRepository.deleteById(customerId);
    }

    @Override
    public void debit(String accountId, double amount, String description)  throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Account not found"));
       if(bankAccount.getBalance()<amount){
           throw new BalanceNotSufficentException("Balance not sufficient");
       }
       AccountOperation accountOperation=new AccountOperation();
       accountOperation.setType(OperationType.DEBIT);
       accountOperation.setAmount(amount);
       accountOperation.setDescription(description);
       accountOperation.setOperationDate(new Date());
       accountOperation.setBankAccount(bankAccount);
       accountOperationRepository.save(accountOperation);
       bankAccount.setBalance(bankAccount.getBalance()-amount);
       bankAccountRepository.save(bankAccount);
    }


    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Accoun Not Found !!!!"));
       AccountOperation accountOperation=new AccountOperation();
       accountOperation.setType(OperationType.CREDIT);
       accountOperation.setAmount(amount);
       accountOperation.setOperationDate(new Date());
       accountOperation.setDescription(description);
       accountOperation.setBankAccount(bankAccount);
       accountOperationRepository.save(accountOperation);
       bankAccount.setBalance(bankAccount.getBalance()+amount);
       bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdsource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdsource,amount,"transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"transfer from "+accountIdsource);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null){
            throw  new BankAccountNotFoundException("Account not found");
        }
        Page<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO=bankAccountMapper.fromBankAccount(bankAccount);
        List<AccountOperationDTO> accountOperationDTOS=accountOperations.stream().map(accountOperation -> {
           return bankAccountMapper.fromAccountOperation(accountOperation);
        }).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
//        accountHistoryDTO.setId(bankAccount.getId());
//        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPagesize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=customarRepository.findByNameContains(keyword);
        List<CustomerDTO> customerDTOS= customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts=bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS= bankAccounts.stream().map(bankAccount -> {
           if(bankAccount instanceof SavingAccount){
               SavingAccount savingAccount=(SavingAccount) bankAccount;
               SavingAccountDTO savingAccountDTO=bankAccountMapper.fromSavingAccount(savingAccount);
               return savingAccountDTO;
           } else {
               CurrentAccount currentAccount=(CurrentAccount) bankAccount;
               CurrentAccountDTO currentAccountDTO=bankAccountMapper.fromCurrentAccount(currentAccount);
               return  currentAccountDTO;
           }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
         List<AccountOperation>accountOperations=accountOperationRepository.findByBankAccountId(accountId);
         List<AccountOperationDTO> accountOperationDTOS=accountOperations.stream()
                 .map(accountOperation -> {
                     return bankAccountMapper.fromAccountOperation(accountOperation);
         }).collect(Collectors.toList());
         return accountOperationDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer=customarRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("Customer Not found !!! "));
        return bankAccountMapper.fromCustomer(customer);
    }

}
