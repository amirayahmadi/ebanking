package com.example.ebanking.mappers;

import com.example.ebanking.dtos.CustomerDTO;
import com.example.ebanking.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

//MapStruct est un framework fait le mapping
@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO= new CustomerDTO();
        /*customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());*/
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
       Customer customer =new Customer();
       BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }



}
