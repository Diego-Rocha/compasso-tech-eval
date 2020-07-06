package io.diego.compasso.tech.eval.service;

import io.diego.compasso.tech.eval.converter.city.CitySearchDTOConverter;
import io.diego.compasso.tech.eval.model.dto.city.CitySearchDTO;
import io.diego.compasso.tech.eval.model.entity.City;
import io.diego.compasso.tech.eval.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CityService {

    private final CityRepository repository;

    public City save(City city) {
        return repository.save(city);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Page<City> findAll(Pageable pageable, CitySearchDTO search) {
        City example = CitySearchDTOConverter.convert(search);
        return repository.findAll(Example.of(example, getExampleMatcher()), pageable);
    }

    public static ExampleMatcher getExampleMatcher() {
        return ExampleMatcher.matching()
                    .withMatcher("name", contains().ignoreCase())
                    .withMatcher("state", exact().ignoreCase());
    }

    public Optional<City> findById(String id) {
        return repository.findById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
