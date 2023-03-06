package com.example.ebanking;

import com.example.ebanking.dtos.CustomerDTO;
import com.example.ebanking.entities.*;
import com.example.ebanking.enums.AccountStatus;
import com.example.ebanking.enums.OperationType;
import com.example.ebanking.exceptions.BalanceNotSuffisantException;
import com.example.ebanking.exceptions.BankAccountNotFoundException;
import com.example.ebanking.exceptions.CustomerNotFoundException;
import com.example.ebanking.repositories.AccountOperationRepository;
import com.example.ebanking.repositories.BankAccountRepository;
import com.example.ebanking.repositories.CustomerRepository;
import com.example.ebanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
		return args -> {
			Stream.of("hasan","imane","mohamed").forEach(name->{
				CustomerDTO customer=new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomers().forEach(customer->{
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*9000,9000.0, customer.getId());
					bankAccountService.saveSavingCurrentBankAccount(Math.random()*120000,5.5, customer.getId());
					List<BankAccount> bankAccountList= bankAccountService.bankAccountList();
					for (BankAccount account :bankAccountList) {
						for (int i=0;i<10;i++){
							bankAccountService.credit(account.getId(), 10000+Math.random()*120000,"Crediter");
							bankAccountService.debit(account.getId(), 10000+Math.random()*9000,"debiter");

						}
				}

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				} catch (BalanceNotSuffisantException | BankAccountNotFoundException e) {
					e.printStackTrace();
				}
			});
		};
	}









	//consultation
	/*@Bean
	CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository){
		return args -> {
			BankAccount bankAccount= bankAccountRepository.findById("9a6c2c6b-dc9d-4f22-9fe1-6254df4c32a7").orElse(null);
			if(bankAccount != null){
				System.out.println("************************************************************");
				System.out.println(bankAccount.getId());
				System.out.println(bankAccount.getBalance());
				System.out.println(bankAccount.getStatus());
				System.out.println(bankAccount.getCreatedAt());
				System.out.println(bankAccount.getCustomer().getName());
				System.out.println(bankAccount.getClass().getSimpleName());
				if (bankAccount instanceof CurrentAccount){
					System.out.println("over draft => "+((CurrentAccount)bankAccount).getOverDraft());
				}else if(bankAccount instanceof  SavingAccount){
					System.out.println("rateInterested => "+((SavingAccount)bankAccount).getInterestRate());
				}

				bankAccount.getAccountOperations().forEach(op->{
					System.out.println("===========================");
					System.out.println(op.getId()+"\t"+op.getType()+"\t"+op.getOperationDate()+"\t"+op.getAmount());

				});

			}//end if (banckAccount != null)

		};
	}*/

	/*9a6c2c6b-dc9d-4f22-9fe1-6254df4c32a7
59.109225057474404
CREATED
2023-03-05 15:25:26.0
hassan
CurrentAccount
over draft => 9000.0
===========================
1	DEBIT	2023-03-05 15:25:26.0	500.0
===========================
2	DEBIT	2023-03-05 15:25:26.0	500.0
===========================
3	DEBIT	2023-03-05 15:25:26.0	500.0
===========================
4	DEBIT	2023-03-05 15:25:26.0	500.0
===========================
5	DEBIT	2023-03-05 15:25:26.0	500.0
===========================
6	CREDIT	2023-03-05 15:25:26.0	500.0
===========================
7	CREDIT	2023-03-05 15:25:26.0	500.0
===========================
8	DEBIT	2023-03-05 15:25:26.0	500.0
===========================
9	DEBIT	2023-03-05 15:25:26.0	500.0
===========================
10	CREDIT	2023-03-05 15:25:26.0	500.0*/
/*	@Bean
	CommandLineRunner start (CustomerRepository customerRepository,
						BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return  args->{
			Stream.of("hassan","yassmine","aicha").forEach(name->{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(cust->{
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);




				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*9000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);


			});

			bankAccountRepository.findAll().forEach(acc->{
				for (int i=0;i<10;i++){
					AccountOperation accountOperation=new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(500);
					accountOperation.setType(Math.random()>0.5?OperationType.DEBIT:OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});


		};

	}*/






}//end of class EbankingApplication
