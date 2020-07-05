package io.diego.compasso.tech.eval.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {

    public static final String NAME_IS_REQUIRED = "name is required";
    public static final String STATE_IS_REQUIRED = "state is required";
    public static final String STATE_SIZE = "state need to be 2 letters";

    private String id;

    @NotBlank(message = NAME_IS_REQUIRED)
    private String name;

    @NotBlank(message = STATE_IS_REQUIRED)
    @Size(min = 2, max = 2,message = STATE_SIZE)
    private String state;

}
