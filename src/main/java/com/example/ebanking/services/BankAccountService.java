package com.example.ebanking.services;

import com.example.ebanking.dtos.CustomerDTO;
import com.example.ebanking.entities.BankAccount;
import com.example.ebanking.entities.CurrentAccount;
import com.example.ebanking.entities.Customer;
import com.example.ebanking.entities.SavingAccount;
import com.example.ebanking.exceptions.BalanceNotSuffisantException;
import com.example.ebanking.exceptions.BankAccountNotFoundException;
import com.example.ebanking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);


    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    CurrentAccount saveCurrentBankAccount(double initialBalance, Double overDraft, Long customerId) throws CustomerNotFoundException;
     SavingAccount saveSavingCurrentBankAccount(double initialBalance, Double interestRate, Long customerId) throws CustomerNotFoundException;
    /* List<Customer> listCustomers();*/
    List<CustomerDTO> listCustomers();
     BankAccount getBankAccount(String id) throws BankAccountNotFoundException;
     void debit(String accoundId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSuffisantException;
     void credit(String accoundId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSuffisantException;
     void transfer(String accoundIdSource,String accountIdDestination,double amount) throws BalanceNotSuffisantException, BankAccountNotFoundException;

     List<BankAccount> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
}
