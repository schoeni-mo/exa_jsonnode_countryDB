package at.kaindorf.exa_jsonnode.pojos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Country {
    @JsonIgnore
    private Long countryId;

    @EqualsAndHashCode.Include
    @JsonAlias("country")
    private String countryName;
    @EqualsAndHashCode.Include

    @JsonAlias("country_code")
    private String countryCode;

    @JsonIgnore
    private List<Address> addresses;

    @Override
    public String toString() {
        return "Country{" +
                "countryId=" + countryId +
                ", countryName='" + countryName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
