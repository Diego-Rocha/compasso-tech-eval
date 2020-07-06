package io.diego.compasso.tech.eval.controller;

import io.diego.compasso.tech.eval.converter.city.CityConverter;
import io.diego.compasso.tech.eval.model.dto.city.CityDTO;
import io.diego.compasso.tech.eval.model.dto.city.CitySearchDTO;
import io.diego.compasso.tech.eval.model.entity.City;
import io.diego.compasso.tech.eval.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/city")
public class CityController {

    private final CityService service;

    @PostMapping
    public String save(@Valid @RequestBody CityDTO dto) {
        City city = CityConverter.convert(dto);
        city = service.save(city);
        return city.getId();
    }

    @GetMapping
    public ResponseEntity<Page<CityDTO>> list(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              CitySearchDTO search
    ) {
        Page<City> pageEntity = service.findAll(PageRequest.of(page, size), search);
        Page<CityDTO> dtoPage = pageEntity.map(CityConverter::convert);
        return ResponseEntity.ok(dtoPage);
    }

    //Extras

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> view(@PathVariable String id) {
        Optional<City> entity = service.findById(id);
        if (!entity.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CityDTO dto = CityConverter.convert(entity.get());
        return ResponseEntity.ok(dto);
    }
}
