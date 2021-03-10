package cz.marcis.coolpee.router.dto;

import cz.marcis.coolpee.router.service.NoSuchCountryException;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Component
public class Countries {

    private List<Country> countries;

    public Optional<Country> findByName(String name) {
        return countries
                .stream()
                .filter(country -> country.getCca3().equals(name))
                .findFirst();
    }

    public List<Country> getBordersFor(String name) {
        Country base = findByName(name).orElseThrow(NoSuchCountryException::new);
        return base
                .getBorders()
                .stream()
                .map(this::findByName)
                .map(country -> country.orElseThrow(NoSuchCountryException::new))
                .collect(Collectors.toList());
    }

}
