package at.kaindorf.exa_jsonnode.database;

import at.kaindorf.exa_jsonnode.pojos.Address;
import at.kaindorf.exa_jsonnode.pojos.Country;
import at.kaindorf.exa_jsonnode.pojos.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.ReflectionUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
@Getter
public class CountryCustMockDB {

    private Long count = 0L;
    Set<Country> countries = new HashSet<>();

    @PostConstruct
    public void init() throws IOException {
        List<Customer> customers = new ArrayList<>();
        List<Address> addresses = new ArrayList<>();


        ObjectMapper mapper = new ObjectMapper();
        InputStream is = this.getClass().getResourceAsStream("/customers.json");
        JsonNode rootNode = mapper.readTree(is);
        if ( rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                count++;
                String country = node.get("country").asText();
                String country_code = node.get("country_code").asText();
                String city = node.get("city").asText();
                Integer postal_code = node.get("postal_code").asInt();
                String streetName = node.get("streetname").asText();
                String streetNumber = node.get("streetnumber").asText();
                String firstName = node.get("firstname").asText();
                String lastName = node.get("lastname").asText();
                char gender = node.get("gender").asText().charAt(0);
                boolean active = node.get("active").asBoolean();
                String email = node.get("email").asText();
                LocalDate since = LocalDate.parse(node.get("since").asText(), DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US));

                Country nodeCountry = new Country(count, country, country_code, new ArrayList<>());
                Address nodeAddress = new Address(count, city, postal_code, streetName, streetNumber, nodeCountry, new ArrayList<>());
                Customer nodeCustomer = new Customer(count, firstName, lastName, gender, active, email, since, nodeAddress);



                countries.add(nodeCountry);
                addresses.add(nodeAddress);
                customers.add(nodeCustomer);
            }

            countries.forEach(c -> {
                addresses.forEach(a -> {
                    if( a.getCountry().equals(c) && !c.getAddresses().contains(a)) {
                        c.getAddresses().add(a);
                    }
                    customers.forEach(cust -> {
                        if ( cust.getAddress().equals(a) && !a.getCustomers().contains(cust)) {
                            a.getCustomers().add(cust);
                        }
                    });
                });
            });
        }
    }

    public Optional<Set<Country>> getAllCountries() {
        return Optional.of(countries);
    }

    public Optional<Customer> getCustomerById(Long customerId) {
        for ( Country c : countries ) {
            for ( Address a : c.getAddresses()) {
                for ( Customer cust : a.getCustomers()) {
                    if ( cust.getCustomerId().equals(customerId)) {
                        return Optional.of(cust);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Customer> postCustomerToAddress(int addressId, Customer cust) {
        count++;
        cust.setCustomerId(count);
        for (Country c : countries ) {
            for (Address a : c.getAddresses()) {
                if ( a.getAddressId() == addressId) {
                    a.getCustomers().add(cust);
                    return Optional.of(cust);
                }
            }
        }
        return Optional.of(cust);
    }

    public Optional<String> deleteCustomer(Long customerId) {
        countries.forEach(c -> {
            c.getAddresses().forEach(a -> {
                a.getCustomers().removeIf(cust -> cust.getCustomerId().equals(customerId));
            });
        });
       return Optional.of("Vllt wurde deleted aber vllt auch nicht, wer weiß");
    }

    public Optional<Customer> patchCustomer(Long customerId, Customer customer) {
        Optional<Customer> optCust = getCustomerById(customerId);
        if ( optCust.isPresent()) {
            Customer cust = optCust.get();
            Field[] fields = cust.getClass().getDeclaredFields();

            for ( Field field : fields) {
                field.setAccessible(true);
                Object obj = ReflectionUtils.getField(field, customer);
                if ( obj != null) {
                    ReflectionUtils.setField(field, cust, obj);
                }
            }
            cust.setCustomerId(customerId);
            return Optional.of(cust);

        }
        return Optional.empty();
    }


    public Optional<List<Customer>> pagination(String countryCode, int pageNo, int pageSize, String sortBy) {
        List<Customer> dataList = new ArrayList<>();
        countries.stream().filter(c -> c.getCountryCode().equals(countryCode)).findFirst().get().getAddresses().forEach(a -> {
            dataList.addAll(a.getCustomers());
        });
        log.warn(dataList.toString());


        int fromIndex = (pageNo -1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, dataList.size());

        if ( fromIndex >= dataList.size()) {
            return Optional.of(Collections.emptyList());
        }

        if ( sortBy.equals("firstname")) { dataList.sort(Comparator.comparing(Customer::getFirstname));}
        if ( sortBy.equals("lastname")) { dataList.sort(Comparator.comparing(Customer::getLastname));}

        return Optional.of(dataList.subList(fromIndex, toIndex));
    }
}
