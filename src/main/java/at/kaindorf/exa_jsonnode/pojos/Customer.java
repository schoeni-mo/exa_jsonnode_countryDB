package at.kaindorf.exa_jsonnode.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @JsonIgnore
    private Long customerId;

    @EqualsAndHashCode.Include
    private String firstname;
    @EqualsAndHashCode.Include
    private String lastname;
    @EqualsAndHashCode.Include
    private char gender;
    @EqualsAndHashCode.Include
    private boolean active;
    @EqualsAndHashCode.Include
    private String email;

    @JsonFormat(pattern = "dd-MMM-yyyy")
    private LocalDate since;

    @JsonIgnore
    private Address address;


    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gender=" + gender +
                ", active=" + active +
                ", email='" + email + '\'' +
                '}';
    }
}
