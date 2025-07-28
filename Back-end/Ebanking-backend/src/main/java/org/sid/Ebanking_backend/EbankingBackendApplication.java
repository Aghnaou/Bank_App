package org.sid.Ebanking_backend;

import org.sid.Ebanking_backend.Entities.*;
import org.sid.Ebanking_backend.dtos.BankAccountDTO;
import org.sid.Ebanking_backend.dtos.CurrentAccountDTO;
import org.sid.Ebanking_backend.dtos.CustomerDTO;
import org.sid.Ebanking_backend.dtos.SavingAccountDTO;
import org.sid.Ebanking_backend.enums.AccountStatus;
import org.sid.Ebanking_backend.enums.OperationType;
import org.sid.Ebanking_backend.exceptions.BalanceNotSufficentException;
import org.sid.Ebanking_backend.exceptions.BankAccountNotFoundException;
import org.sid.Ebanking_backend.exceptions.CustomerNotFoundException;
import org.sid.Ebanking_backend.repositories.AccountOperationRepository;
import org.sid.Ebanking_backend.repositories.BankAccountRepository;
import org.sid.Ebanking_backend.repositories.CustomarRepository;
import org.sid.Ebanking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}

	//@Bean
	CommandLineRunner start(BankAccountService bankAccountService){
		return args -> {
          Stream.of("Hassan","Iman","Mohamed").forEach(
				  name->{
					  CustomerDTO customer=new CustomerDTO();
					  customer.setName(name);
					  customer.setEmail(name+"@gmail.com");
					  bankAccountService.saveCustomer(customer);
				  }
		  );
		  bankAccountService.listCustomers().forEach(customer -> {
              try {
                  bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000, customer.getId());
				  bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5, customer.getId());

              } catch (CustomerNotFoundException e) {
                  e.printStackTrace();
              }
          });
		  List<BankAccountDTO>bankAccounts =bankAccountService.bankAccountList();
			for (BankAccountDTO bankAccount : bankAccounts){
				for (int i=0;i<10;i++){
					String accoundId;
					if(bankAccount instanceof SavingAccountDTO){
						accoundId=((SavingAccountDTO) bankAccount).getId();
					}
					else {
						accoundId=((CurrentAccountDTO)bankAccount).getId();
					}
					bankAccountService.credit(accoundId, 10000+Math.random()*120000,"credit");
					bankAccountService.debit(accoundId, 1000+Math.random()*9000,"debit");
				}
			}
		};
	}

	//@Bean
//	CommandLineRunner start(BankAccountRepository bankAccountRepository){
//		return args -> {
//			BankAccount bankAccount=bankAccountRepository.findById("bcf39331-f882-4963-8a85-66489b9d5a0a").orElse(null);
//			if(bankAccount!=null){
//				System.out.println("********************************");
//				System.out.println(bankAccount.getId());
//				System.out.println(bankAccount.getBalance());
//				System.out.println(bankAccount.getStatus());
//				System.out.println(bankAccount.getCreatedAt());
//				System.out.println(bankAccount.getCustomer().getName());
//				System.out.println(bankAccount.getClass().getSimpleName());
//				if(bankAccount instanceof CurrentAccount){
//					System.out.println("Overdraft =>"+((CurrentAccount)bankAccount).getOverDraft());
//				} else if (bankAccount instanceof SavingAccount) {
//					System.out.println("Rate =>"+((SavingAccount)bankAccount).getInterestRate());
//				}
//
//				bankAccount.getAccountOperations().forEach(
//						accountOperation -> {
//							System.out.println(accountOperation.getType()+"\t"+accountOperation.getOperationDate() + "\t"+accountOperation.getAmount());
//						}
//				);
//
//			}
//		};
//	}

	//@Bean
	CommandLineRunner start(CustomarRepository customarRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("Hassan","yassin","Aisha").forEach(
					name->{
						Customer customer=new Customer();
						customer.setName(name);
						customer.setEmail(name+"@gmail.com");
						customarRepository.save(customer);
					}
			);

			customarRepository.findAll().forEach(
					customer -> {
						CurrentAccount currentAccount=new CurrentAccount();
						currentAccount.setId(UUID.randomUUID().toString());
						currentAccount.setBalance(Math.random()*90000);
						currentAccount.setCreatedAt(new Date());
						currentAccount.setStatus(AccountStatus.CREATED);
						currentAccount.setCustomer(customer);
						currentAccount.setOverDraft(9000);
						bankAccountRepository.save(currentAccount);

						SavingAccount savingAccount=new SavingAccount();
						savingAccount.setId(UUID.randomUUID().toString());
						savingAccount.setBalance(Math.random()*90000);
						savingAccount.setCreatedAt(new Date());
						savingAccount.setStatus(AccountStatus.CREATED);
						savingAccount.setCustomer(customer);
						savingAccount.setInterestRate(5.5);
						bankAccountRepository.save(savingAccount);


					}
			);
			bankAccountRepository.findAll().forEach(
					bankAccount -> {
						for(int i=0;i<10;i++){
							AccountOperation accountOperation=new AccountOperation();
							accountOperation.setOperationDate(new Date());
							accountOperation.setAmount(Math.random()*12000);
							accountOperation.setType(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
							accountOperation.setBankAccount(bankAccount);
							accountOperationRepository.save(accountOperation);

						}
					}
			);
		};
	}

}
