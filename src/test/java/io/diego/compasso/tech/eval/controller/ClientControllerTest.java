package io.diego.compasso.tech.eval.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.diego.compasso.tech.eval.converter.client.ClientConverter;
import io.diego.compasso.tech.eval.model.dto.client.ClientDTO;
import io.diego.compasso.tech.eval.model.entity.Client;
import io.diego.compasso.tech.eval.repository.ClientRepository;
import io.diego.compasso.tech.eval.service.ClientService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientRepository repository;

    Map<String, String> validJsonMap = new HashMap<>();
    private static final int mockSize = 2;

    @PostConstruct
    public void init() throws Exception {
        LongStream.rangeClosed(1, mockSize).forEach(id -> {
            try {
                File payload = ResourceUtils.getFile("classpath:payload/client/valid/" + id + ".json");
                validJsonMap.put(String.valueOf(id), new String(Files.readAllBytes(payload.toPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        initMocks();
    }

    private void initMocks() throws IOException {
        List<Client> savedCLients = new ArrayList<>();
        for (Map.Entry<String, String> validJson : validJsonMap.entrySet()) {
            ClientDTO dto = objectMapper.readValue(validJson.getValue(), ClientDTO.class);

            Client clientToSave = ClientConverter.convert(dto);
            Client clientSaved = ClientConverter.convert(dto);
            clientSaved.setId(validJson.getKey());
            savedCLients.add(clientToSave);
            when(repository.save(clientToSave)).thenReturn(clientSaved);
            when(repository.findById(validJson.getKey())).thenReturn(Optional.of(clientSaved));
        }

        ClientDTO dto = objectMapper.readValue(validJsonMap.get("1"), ClientDTO.class);
        Client clientToSave = ClientConverter.convert(dto);
        clientToSave.setName("John Stuart");
        when(repository.save(clientToSave)).thenReturn(clientToSave);

        when(repository.findById("99")).thenReturn(Optional.empty());

        PageRequest pageRequestDefault = PageRequest.of(0, 10);
        PageRequest pageRequestCustom = PageRequest.of(0, 1);
        PageRequest pageRequestOutOfRange = PageRequest.of(99, 1);

        Example<Client> noSearchExample = Example.of(Client.builder().build(), ClientService.getExampleMatcher());
        Example<Client> searchExample = Example.of(Client.builder().name("john").build(), ClientService.getExampleMatcher());

        when(repository.findAll(noSearchExample,pageRequestDefault)).thenReturn(new PageImpl<>(savedCLients, pageRequestDefault, mockSize));
        when(repository.findAll(noSearchExample,pageRequestCustom)).thenReturn(new PageImpl<>(savedCLients.subList(0, 1), pageRequestCustom, mockSize));
        when(repository.findAll(noSearchExample,pageRequestOutOfRange)).thenReturn(new PageImpl<>(Collections.emptyList(), pageRequestOutOfRange, mockSize));
        when(repository.findAll(searchExample, pageRequestDefault)).thenReturn(new PageImpl<>(savedCLients.subList(0, 1), pageRequestDefault, mockSize));


    }

    @Test
    public void whenPost_withValid_thenOk() throws Exception {
        for (Map.Entry<String, String> validJson : validJsonMap.entrySet()) {
            mockMvc.perform(MockMvcRequestBuilders.post("/client")
                    .content(validJson.getValue())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(validJson.getKey()));
        }
    }

    @Test
    public void whenPost_withEmptyJson_thenError() throws Exception {
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/client")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(ClientDTO.NAME_IS_REQUIRED));
        assertTrue(response.contains(ClientDTO.GENDER_IS_REQUIRED));
        assertTrue(response.contains(ClientDTO.BIRTH_DATE_IS_REQUIRED));
        assertTrue(response.contains(ClientDTO.CITY_IS_REQUIRED));
    }

    @Test
    public void whenPost_withEmptyName_thenError() throws Exception {
        File payload = ResourceUtils.getFile("classpath:payload/client/invalid/empty_name.json");
        String content = new String(Files.readAllBytes(payload.toPath()));
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/client")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(ClientDTO.NAME_IS_REQUIRED));
        assertFalse(response.contains(ClientDTO.GENDER_IS_REQUIRED));
        assertFalse(response.contains(ClientDTO.BIRTH_DATE_IS_REQUIRED));
        assertFalse(response.contains(ClientDTO.CITY_IS_REQUIRED));
    }

    @Test
    public void whenDelete_withId_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/client/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetPaginate_withNoParams_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsString("\"gender\"")))
                .andExpect(content().string(containsString("\"totalElements\":" + mockSize)));
    }

    @Test
    public void whenGetPaginate_withCustomSizeAndPage_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client")
                .queryParam("page", "0")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsString("\"gender\"")))
                .andExpect(content().string(containsString("\"size\":1")));

    }

    @Test
    public void whenGetPaginate_withSearch_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client")
                .queryParam("name", "john")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsStringIgnoringCase("john marino")))
                .andExpect(content().string(containsString("\"gender\"")))
                .andExpect(content().string(containsString("\"size\":1")));
    }

    @Test
    public void whenGetPaginate_withSearchById_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client")
                .queryParam("page", "0")
                .queryParam("size", "10")
                .queryParam("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsStringIgnoringCase("john marino")))
                .andExpect(content().string(containsString("\"gender\"")))
                .andExpect(content().string(containsString("\"size\":1")));
    }

    @Test
    public void whenGetPaginate_withOutOfRange_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client")
                .queryParam("page", "99")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.not(containsString("\"gender\""))))
                .andExpect(content().string(containsString("\"numberOfElements\":0")));
    }

    @Test
    public void whenGet_withId_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\"")))
                .andExpect(content().string(containsString("\"gender\"")))
                .andExpect(content().string(CoreMatchers.not(containsString("\"totalElements\":" + mockSize))));
    }

    @Test
    public void whenGet_withInvalidId_thenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteAll_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/client").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPatch_withANewName_thenOk() throws Exception {
        String rightName = "John Stuart";
        String jsonContent = String.format("{'name':'%s'}", rightName).replace("'", "\"");
        mockMvc.perform(MockMvcRequestBuilders.patch("/client/1")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.get("/client/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(rightName)));

    }

    @Test
    public void whenPatch_withWrongId_thenNotFound() throws Exception {
        String rightName = "Rick Blame";
        String jsonContent = String.format("{'name':'%s'}", rightName).replace("'", "\"");
        mockMvc.perform(MockMvcRequestBuilders.patch("/client/99")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}