package at.kaindorf.exa_jsonnode.web;

import at.kaindorf.exa_jsonnode.database.CountryCustMockDB;
import at.kaindorf.exa_jsonnode.pojos.Customer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerResource {

    private final CountryCustMockDB countryDB;

    @GetMapping("/{customerId}")
    public ResponseEntity<Optional<Customer>> getCustomerById (@PathVariable Long customerId) {
        return ResponseEntity.ok(countryDB.getCustomerById(customerId));
    }

    @PostMapping("")
    public ResponseEntity<Optional<Customer>> postCustomerToAddress(@RequestParam("addressId") int addressId, @RequestBody Customer cust) {
        return ResponseEntity.ok(countryDB.postCustomerToAddress(addressId, cust));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Optional<String>> deleteCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(countryDB.deleteCustomer(customerId));
    }

    @GetMapping("/all/{countryCode}")
    public ResponseEntity<Optional<List<Customer>>> pagination(@PathVariable String countryCode, @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize, @RequestParam("sortBy") String sortBy) {
        return ResponseEntity.ok(countryDB.pagination(countryCode, pageNo, pageSize, sortBy));
    }



    @PatchMapping("/{customerId}")
    public ResponseEntity<Optional<Customer>> patchCustomer(@PathVariable Long customerId, @RequestBody Customer customer) {
        Optional<Customer> cust = countryDB.patchCustomer(customerId, customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/").buildAndExpand(cust.get().getCustomerId()).toUri();
        log.warn(String.valueOf(location));
        return ResponseEntity.created(location).build();
    }
}
