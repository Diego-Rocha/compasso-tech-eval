package io.diego.compasso.tech.eval.converter.city;

import io.diego.compasso.tech.eval.model.dto.city.CitySearchDTO;
import io.diego.compasso.tech.eval.model.entity.City;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CitySearchDTOConverter {

    public City convert(CitySearchDTO dto) {
        return City.builder()
                .name(dto.getName())
                .state(dto.getState())
                .build();
    }

}
