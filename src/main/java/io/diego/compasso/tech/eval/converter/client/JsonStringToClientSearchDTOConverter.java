package io.diego.compasso.tech.eval.converter.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.diego.compasso.tech.eval.model.dto.client.ClientSearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JsonStringToClientSearchDTOConverter implements Converter<String, ClientSearchDTO> {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public ClientSearchDTO convert(String json) {
        return objectMapper.readValue(json, ClientSearchDTO.class);
    }

}
