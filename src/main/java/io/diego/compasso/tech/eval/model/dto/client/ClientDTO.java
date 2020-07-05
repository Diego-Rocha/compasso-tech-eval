package io.diego.compasso.tech.eval.model.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.diego.compasso.tech.eval.business.ClientBusiness;
import io.diego.compasso.tech.eval.model.enums.Gender;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    public static final String NAME_IS_REQUIRED = "name is required";
    public static final String GENDER_IS_REQUIRED = "gender is required";
    public static final String BIRTH_DATE_IS_REQUIRED = "birthDate is required";
    public static final String CITY_IS_REQUIRED = "city id is required";

    private String id;

    @NotBlank(message = NAME_IS_REQUIRED)
    private String name;

    @NotNull(message = GENDER_IS_REQUIRED)
    private Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = BIRTH_DATE_IS_REQUIRED)
    private LocalDate birthDate;

    @NotNull(message = CITY_IS_REQUIRED)
    private String cityId;

    public int getAge(){
       return ClientBusiness.resolveAge(getBirthDate());
    }

}
