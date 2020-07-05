package io.diego.compasso.tech.eval.service;

import io.diego.compasso.tech.eval.converter.city.CitySerchDTOConverter;
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

    public Page<City> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<City> findAll(Pageable pageable, CitySearchDTO search) {
        if (search == null) {
            return findAll(pageable);
        }
        City example = CitySerchDTOConverter.convert(search);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", contains().ignoreCase())
                .withMatcher("state", exact().ignoreCase());

        return repository.findAll(Example.of(example, matcher), pageable);
    }

    public Optional<City> findById(String id) {
        return repository.findById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
