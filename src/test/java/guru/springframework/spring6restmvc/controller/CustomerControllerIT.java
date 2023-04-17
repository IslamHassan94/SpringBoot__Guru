package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;


    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteById() {
        CustomerDTO customerDTO = customerController.listCustomers().get(0);

        ResponseEntity responseEntity = customerController.deleteById(customerDTO.getId());

        assertThat(customerRepository.findById(customerDTO.getId()).isEmpty());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        String updatedName = "UPDATED Customer";
        customerDTO.setCustomerName(updatedName);

        ResponseEntity responseEntity = customerController.updateById(customer.getId(), customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
    }

    @Test
    void testSaveCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("New Customer Name")
                .build();
        ResponseEntity responseEntity = customerController.handlePost(customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locations = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locations[4]);

        Customer savedCustomer = customerRepository.findById(savedUUID).get();

        assertThat(savedCustomer).isNotNull();
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testListCustomersEmpty() {
        customerRepository.deleteAll();

        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(0);
    }
}