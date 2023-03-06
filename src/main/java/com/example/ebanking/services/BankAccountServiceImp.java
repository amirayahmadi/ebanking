package com.example.ebanking.services;

import com.example.ebanking.dtos.CustomerDTO;
import com.example.ebanking.entities.*;
import com.example.ebanking.enums.OperationType;
import com.example.ebanking.exceptions.BalanceNotSuffisantException;
import com.example.ebanking.exceptions.BankAccountNotFoundException;
import com.example.ebanking.exceptions.CustomerNotFoundException;
import com.example.ebanking.mappers.BankAccountMapperImpl;
import com.example.ebanking.repositories.AccountOperationRepository;
import com.example.ebanking.repositories.BankAccountRepository;
import com.example.ebanking.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImp implements BankAccountService{

   /* @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private AccountOperationRepository accountOperationRepository;
    */
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    private BankAccountMapperImpl mapper;
   /* public BankAccountServiceImp(CustomerRepository customerRepository,BankAccountRepository bankAccountRepository,AccountOperationRepository accountOperationRepository){
        this.customerRepository=customerRepository;
        this.bankAccountRepository=bankAccountRepository;
        this.accountOperationRepository=accountOperationRepository;
    }*/

    //Logger log = LoggerFactory.getLogger(this.getClass().getName());



    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = mapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }



    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = customerRepository.findById(id).get();
        customer.setEmail(customerDTO.getEmail());
        customer.setName(customerDTO.getName());
        Customer savedCustomer =customerRepository.save(customer);
        return  mapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }


    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, Double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        }
        /*BankAccount bankAccount;
        if(type.equals("current")){
            bankAccount = new CurrentAccount();
        }else{
            bankAccount = new SavingAccount();
        }*/
        CurrentAccount currentAccount= new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);
        return savedCurrentAccount;
    }

    @Override
    public SavingAccount saveSavingCurrentBankAccount(double initialBalance, Double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        }

        SavingAccount savingAccount= new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);
        return savedSavingAccount;
    }


    @Override
    public List<CustomerDTO> listCustomers() {
         List<Customer> customers = customerRepository.findAll();
         /*programmation imperative*/
        /* List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer:customers) {
            CustomerDTO customerDTO= mapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;*/
        /*programmation fonctionnelle*/
         List<CustomerDTO> customerDTOS = customers.stream().map(cust->mapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccount getBankAccount(String id) throws BankAccountNotFoundException {
       BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(()-> new BankAccountNotFoundException("bank not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accoundId, double amount, String description) throws BankAccountNotFoundException,BalanceNotSuffisantException {
        BankAccount bankAccount = getBankAccount(accoundId);
        if(bankAccount.getBalance()<amount){
            throw new BalanceNotSuffisantException("Balance not suffisant");
        }
        AccountOperation accountOperation = new AccountOperation();
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
    public void credit(String accoundId, double amount, String description) throws BankAccountNotFoundException{
        BankAccount bankAccount = getBankAccount(accoundId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accoundIdSource, String accountIdDestination, double amount) throws BalanceNotSuffisantException, BankAccountNotFoundException {
        debit(accoundIdSource,amount,"transfert to"+accountIdDestination);
        credit(accountIdDestination,amount,"transfert from"+accoundIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }



    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
       CustomerDTO customerDTO = mapper.fromCustomer(customer);
       return  customerDTO;
    }
}
