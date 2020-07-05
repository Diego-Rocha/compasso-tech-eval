package io.diego.compasso.tech.eval.converter.client;

import io.diego.compasso.tech.eval.model.dto.client.ClientSearchDTO;
import io.diego.compasso.tech.eval.model.entity.Client;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientSearchDTOConverter {

    public Client convert(ClientSearchDTO dto) {
        return Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

}
