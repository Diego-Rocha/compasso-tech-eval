package io.diego.compasso.tech.eval.config;

import io.diego.compasso.tech.eval.model.entity.City;
import io.diego.compasso.tech.eval.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("dev")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DevPayload {

    @Autowired
    private final CityService cityService;

    private final List<City> payload = new ArrayList<City>() {{
        City city = City.builder()
                .name("Florianópolis")
                .state("SC")
                .build();
        add(city);

        city = City.builder()
                .name("Sao José")
                .state("SC")
                .build();
        add(city);

        city = City.builder()
                .name("São Paulo")
                .state("SP")
                .build();
        add(city);

        city = City.builder()
                .name("Rio de Janeiro")
                .state("RJ")
                .build();
        add(city);

    }};

    @Bean
    public ApplicationRunner devApplicationRunner() {
        cityService.deleteAll();
        return arg -> payload.forEach(cityService::save);
    }

}
