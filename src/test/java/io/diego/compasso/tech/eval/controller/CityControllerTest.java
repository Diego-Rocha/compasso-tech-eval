package io.diego.compasso.tech.eval.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.diego.compasso.tech.eval.converter.city.CityConverter;
import io.diego.compasso.tech.eval.model.dto.city.CityDTO;
import io.diego.compasso.tech.eval.model.entity.City;
import io.diego.compasso.tech.eval.repository.CityRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.LongStream;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CityRepository repository;

    Map<String, String> validJsonMap = new HashMap<>();
    private static final int mockSize = 8;

    @PostConstruct
    public void init() throws Exception {
        LongStream.rangeClosed(1, mockSize).forEach(id -> {
            try {
                File payload = ResourceUtils.getFile("classpath:payload/city/valid/" + id + ".json");
                validJsonMap.put(String.valueOf(id), new String(Files.readAllBytes(payload.toPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        initMocks();
    }

    private void initMocks() throws IOException {
        List<City> savedCities = new ArrayList<>();
        for (Map.Entry<String, String> validJson : validJsonMap.entrySet()) {

            CityDTO dto = objectMapper.readValue(validJson.getValue(), CityDTO.class);

            City cityToSave = CityConverter.convert(dto);
            City citySaved = CityConverter.convert(dto);
            citySaved.setId(validJson.getKey());
            savedCities.add(cityToSave);
            when(repository.save(cityToSave)).thenReturn(citySaved);
            when(repository.findById(validJson.getKey())).thenReturn(Optional.of(citySaved));
        }

        when(repository.findById("99")).thenReturn(Optional.empty());

        PageRequest pageRequestDefault = PageRequest.of(0, 10);
        when(repository.findAll(pageRequestDefault)).thenReturn(new PageImpl<>(savedCities, pageRequestDefault, mockSize));

        PageRequest pageRequestCustom = PageRequest.of(0, 1);
        when(repository.findAll(pageRequestCustom)).thenReturn(new PageImpl<>(savedCities.subList(0, 1), pageRequestCustom, mockSize));

        PageRequest pageRequestOutOfRange = PageRequest.of(99, 1);
        when(repository.findAll(pageRequestOutOfRange)).thenReturn(new PageImpl<>(Collections.emptyList(), pageRequestOutOfRange, mockSize));


        when(repository.findAll(any(), eq(pageRequestDefault))).thenReturn(new PageImpl<>(savedCities.subList(0, 1), pageRequestDefault, mockSize));

    }

    @Test
    public void whenPost_withValid_thenOk() throws Exception {
        for (Map.Entry<String, String> validJson : validJsonMap.entrySet()) {
            mockMvc.perform(MockMvcRequestBuilders.post("/city")
                    .content(validJson.getValue())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(validJson.getKey()));
        }
    }

    @Test
    public void whenPost_withEmptyJson_thenError() throws Exception {
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/city")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(CityDTO.NAME_IS_REQUIRED));
        assertTrue(response.contains(CityDTO.STATE_IS_REQUIRED));
    }

    @Test
    public void whenPost_withEmptyCity_thenError() throws Exception {
        File payload = ResourceUtils.getFile("classpath:payload/city/invalid/empty_city.json");
        String content = new String(Files.readAllBytes(payload.toPath()));
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/city")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(CityDTO.NAME_IS_REQUIRED));
        assertFalse(response.contains(CityDTO.STATE_IS_REQUIRED));
    }

    @Test
    public void whenPost_withEmptyState_thenError() throws Exception {
        File payload = ResourceUtils.getFile("classpath:payload/city/invalid/empty_state.json");
        String content = new String(Files.readAllBytes(payload.toPath()));
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/city")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(CityDTO.STATE_IS_REQUIRED));
        assertFalse(response.contains(CityDTO.NAME_IS_REQUIRED));
    }

    @Test
    public void whenPost_withNoCity_thenError() throws Exception {
        File payload = ResourceUtils.getFile("classpath:payload/city/invalid/no_city.json");
        String content = new String(Files.readAllBytes(payload.toPath()));
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/city")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(CityDTO.NAME_IS_REQUIRED));
        assertFalse(response.contains(CityDTO.STATE_IS_REQUIRED));
    }

    @Test
    public void whenPost_withNoState_thenError() throws Exception {
        File payload = ResourceUtils.getFile("classpath:payload/city/invalid/no_state.json");
        String content = new String(Files.readAllBytes(payload.toPath()));
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/city")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(CityDTO.STATE_IS_REQUIRED));
        assertFalse(response.contains(CityDTO.NAME_IS_REQUIRED));
    }

    @Test
    public void whenPost_withWrongStateSize_thenError() throws Exception {
        File payload = ResourceUtils.getFile("classpath:payload/city/invalid/state_size.json");
        String content = new String(Files.readAllBytes(payload.toPath()));
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/city")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(CityDTO.STATE_SIZE));
    }


    @Test
    public void whenDelete_withId_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/city/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteAll_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/city").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetPaginate_withNoParams_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/city")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsString("\"state\"")))
                .andExpect(content().string(containsString("\"totalElements\":" + mockSize)));

    }

    @Test
    public void whenGetPaginate_withCustomSizeAndPage_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/city")
                .queryParam("page", "0")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsString("\"state\"")))
                .andExpect(content().string(containsString("\"size\":1")));

    }

    @Test
    public void whenGetPaginate_withSearch_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/city")
                .queryParam("page", "0")
                .queryParam("size", "10")
                .queryParam("search", "{'name':'oia'}".replace("'", "\""))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsStringIgnoringCase("\"oiapoque\"")))
                .andExpect(content().string(containsString("\"state\"")))
                .andExpect(content().string(containsString("\"size\":1")));
    }

    @Test
    public void whenGetPaginate_withOutOfRange_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/city")
                .queryParam("page", "99")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.not(containsString("\"state\""))))
                .andExpect(content().string(containsString("\"numberOfElements\":0")));
    }

    @Test
    public void whenGet_withId_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/city/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsString("\"state\"")))
                .andExpect(content().string(CoreMatchers.not(containsString("\"totalElements\":" + mockSize))));
    }

    @Test
    public void whenGet_withInvalidId_thenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/city/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
