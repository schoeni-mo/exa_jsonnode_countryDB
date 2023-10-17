package at.kaindorf.exa_jsonnode.pojos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Address {

    @JsonIgnore
    private Long addressId;

    @JsonAlias("streetname")
    @EqualsAndHashCode.Include
    private String streetName;
    @EqualsAndHashCode.Include
    @JsonAlias("streetnumber")
    private Integer streetNumber;
    @EqualsAndHashCode.Include
    @JsonAlias("postal_code")
    private String postalCode;
    @EqualsAndHashCode.Include
    private String city;
    @JsonIgnore
    private Country country;

    @JsonIgnore
    private List<Customer> customers;

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", streetName='" + streetName + '\'' +
                ", streetNumber=" + streetNumber +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
