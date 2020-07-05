package io.diego.compasso.tech.eval.model.entity;

import io.diego.compasso.tech.eval.model.enums.Gender;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "client")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "birthDate", "city"})
public class Client {

    @Id
    private String id;

    @Indexed
    private String name;
    private Gender gender;
    private LocalDate birthDate;

    @Reference
    private City city;

}
