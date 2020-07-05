package io.diego.compasso.tech.eval.model.dto.client;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ClientUpdateDTO {

    public static final String NAME_IS_REQUIRED = "name is required";

    @NotBlank(message = NAME_IS_REQUIRED)
    public String name;
}
