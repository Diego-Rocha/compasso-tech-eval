package io.diego.compasso.tech.eval.converter.city;

import io.diego.compasso.tech.eval.model.dto.city.CityDTO;
import io.diego.compasso.tech.eval.model.entity.City;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CityConverter {

    public City convert(CityDTO dto) {
        return City.builder()
                .id(dto.getId())
                .name(dto.getName())
                .state(dto.getState())
                .build();
    }

    public CityDTO convert(City entity) {
        return CityDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .state(entity.getState())
                .build();
    }

}
