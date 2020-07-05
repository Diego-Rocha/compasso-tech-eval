package io.diego.compasso.tech.eval.business;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientBusinessTest {

    @Test
    void resolveAge() {
        ClientBusiness.DATE_NOW = LocalDate.ofYearDay(2020, 1);
        assertEquals(20, ClientBusiness.resolveAge(LocalDate.ofYearDay(2000, 1)));
    }
}
