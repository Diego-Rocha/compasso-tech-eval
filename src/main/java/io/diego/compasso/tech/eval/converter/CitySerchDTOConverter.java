package io.diego.compasso.tech.eval.converter;

import io.diego.compasso.tech.eval.model.dto.CitySearchDTO;
import io.diego.compasso.tech.eval.model.entity.City;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CitySerchDTOConverter {

    public City convert(CitySearchDTO dto) {
        return City.builder()
                .name(dto.getName())
                .state(dto.getState())
                .build();
    }

}
