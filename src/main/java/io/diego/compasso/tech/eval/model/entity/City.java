package io.diego.compasso.tech.eval.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Document(collection = "city")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "state"})
public class City {

    public static final String NAME_IS_REQUIRED = "name is required";
    public static final String STATE_IS_REQUIRED = "state is required";

    @Id
    private String id;

    @NotBlank(message = NAME_IS_REQUIRED)
    private String name;

    @NotBlank(message = STATE_IS_REQUIRED)
    private String state;
}
