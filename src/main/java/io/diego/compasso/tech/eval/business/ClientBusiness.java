package io.diego.compasso.tech.eval.business;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.Period;

@UtilityClass
public class ClientBusiness {

    public LocalDate DATE_NOW = LocalDate.now();

    public int resolveAge(LocalDate birthDate) {
        return Period.between(birthDate, DATE_NOW).getYears();
    }
}
