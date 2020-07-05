package io.diego.compasso.tech.eval.converter.client;

import io.diego.compasso.tech.eval.model.dto.client.ClientDTO;
import io.diego.compasso.tech.eval.model.entity.City;
import io.diego.compasso.tech.eval.model.entity.Client;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientConverter {

    public ClientDTO convert(Client entity) {
        return ClientDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .gender(entity.getGender())
                .birthDate(entity.getBirthDate())
                .cityId(entity.getCity().getId())
                .build();
    }

    public Client convert(ClientDTO dto) {
        return Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .city(City.builder().id(dto.getId()).build())
                .build();
    }

}
