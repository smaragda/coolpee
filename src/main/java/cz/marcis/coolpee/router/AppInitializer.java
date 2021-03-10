package cz.marcis.coolpee.router;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.marcis.coolpee.router.dto.Countries;
import cz.marcis.coolpee.router.dto.Country;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


@Slf4j
@Component
@RequiredArgsConstructor
public class AppInitializer {

    private final Countries countries;

    @SneakyThrows
    @EventListener
    public void loadJson(ApplicationReadyEvent e) {
        log.info("Loading JSON to memory");

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("countries.json");
        String json = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8.name());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Country[] countryArray = objectMapper.readValue(json, Country[].class);

        log.info("size = " + countryArray.length);

        countries.setCountries(Arrays.asList(countryArray));
    }
}
