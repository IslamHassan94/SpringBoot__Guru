package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    public Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder().id(UUID.randomUUID()).customerName("Islam Hassan").version(1).createdDate(LocalDateTime.now()).lastModifiedDate(LocalDateTime.now()).build();

        CustomerDTO customer2 = CustomerDTO.builder().id(UUID.randomUUID()).customerName("Amr Hassan").version(1).createdDate(LocalDateTime.now()).lastModifiedDate(LocalDateTime.now()).build();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMap.get(id));
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder().customerName(customer.getCustomerName()).createdDate(LocalDateTime.now()).lastModifiedDate(LocalDateTime.now()).build();
        customerMap.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        CustomerDTO updatedCustomer = customerMap.get(customerId);
        updatedCustomer.setCustomerName(customer.getCustomerName());
        return Optional.of(updatedCustomer);
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerPatchById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);

        if (StringUtils.hasText(customer.getCustomerName())) {
            existing.setCustomerName(customer.getCustomerName());
        }

        if (customer.getVersion() != null) {
            existing.setVersion(customer.getVersion());
        }
        return Optional.of(existing);
    }
}
