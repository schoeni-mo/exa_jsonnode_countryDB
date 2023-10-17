package at.kaindorf.exa_jsonnode.web;


import at.kaindorf.exa_jsonnode.database.CountryCustMockDB;
import at.kaindorf.exa_jsonnode.pojos.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/countries")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class CountryResource {

    private final CountryCustMockDB countryDB;

    @GetMapping("/all")
    public ResponseEntity<Optional<Set<Country>>> getAllCountries() {
        return ResponseEntity.ok(countryDB.getAllCountries());
    }
}
