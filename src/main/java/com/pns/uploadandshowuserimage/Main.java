package com.pns.uploadandshowuserimage;

import com.pns.uploadandshowuserimage.model.Customer;
import com.pns.uploadandshowuserimage.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            createRandomCustomer(customerRepository);
        };
    }

    private void createRandomCustomer(CustomerRepository customerRepository) {
        Customer customer = new Customer(1,"George", null);
        customerRepository.save(customer);
    }
}
