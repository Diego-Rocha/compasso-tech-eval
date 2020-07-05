package io.diego.compasso.tech.eval.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.diego.compasso.tech.eval.model.dto.CitySearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JsonStringToCitySearchDTOConverter implements Converter<String, CitySearchDTO> {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public CitySearchDTO convert(String json) {
        return objectMapper.readValue(json,CitySearchDTO.class);
    }

}
