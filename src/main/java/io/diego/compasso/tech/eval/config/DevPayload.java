package io.diego.compasso.tech.eval.config;

import io.diego.compasso.tech.eval.model.entity.City;
import io.diego.compasso.tech.eval.model.entity.Client;
import io.diego.compasso.tech.eval.model.enums.Gender;
import io.diego.compasso.tech.eval.service.CityService;
import io.diego.compasso.tech.eval.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("dev")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DevPayload {

    private final CityService cityService;
    private final ClientService clientService;

    private final List<City> cityPayload = new ArrayList<City>() {{
        City city = City.builder()
                .id("1")
                .name("Florianópolis")
                .state("SC")
                .build();
        add(city);

        city = City.builder()
                .id("2")
                .name("Sao José")
                .state("SC")
                .build();
        add(city);

        city = City.builder()
                .id("3")
                .name("São Paulo")
                .state("SP")
                .build();
        add(city);

        city = City.builder()
                .id("4")
                .name("Rio de Janeiro")
                .state("RJ")
                .build();
        add(city);
    }};

    private final List<Client> clientPayload = new ArrayList<Client>() {{
        Client client = Client.builder()
                .name("John Doe")
                .birthDate(LocalDate.now().minusYears(18).minusDays(175))
                .gender(Gender.MALE)
                .city(City.builder().id("4").build())
                .build();
        add(client);

        client = Client.builder()
                .name("Jane Doe")
                .birthDate(LocalDate.now().minusYears(28).minusDays(67))
                .gender(Gender.FEMALE)
                .city(City.builder().id("1").build())
                .build();
        add(client);

        client = Client.builder()
                .name("Joe Public")
                .birthDate(LocalDate.now().minusYears(37).minusDays(251))
                .gender(Gender.MALE)
                .city(City.builder().id("1").build())
                .build();
        add(client);

        client = Client.builder()
                .name("Uriel")
                .birthDate(LocalDate.now().minusYears(64).minusDays(32))
                .gender(Gender.OTHER)
                .city(City.builder().id("3").build())
                .build();
        add(client);
    }};

    @Bean
    public ApplicationRunner devApplicationRunner() {
        clientService.deleteAll();
        cityService.deleteAll();
        return arg -> {
            cityPayload.forEach(cityService::save);
            clientPayload.forEach(clientService::save);
        };
    }

}
