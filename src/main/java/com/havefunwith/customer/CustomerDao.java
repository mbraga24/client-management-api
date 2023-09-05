package com.havefunwith.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

     List<Customer> selectAllCustomers();

     Optional<Customer> selectCustomerById(Long id);

     void insertCustomer(Customer customer);

     void deleteCustomer(Long id);

     boolean existsPersonWithEmail(String email);

     boolean existsPersonById(Long id);

}
