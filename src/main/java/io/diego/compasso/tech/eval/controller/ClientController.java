package io.diego.compasso.tech.eval.controller;

import io.diego.compasso.tech.eval.converter.client.ClientConverter;
import io.diego.compasso.tech.eval.model.dto.client.ClientDTO;
import io.diego.compasso.tech.eval.model.dto.client.ClientSearchDTO;
import io.diego.compasso.tech.eval.model.dto.client.ClientUpdateDTO;
import io.diego.compasso.tech.eval.model.entity.Client;
import io.diego.compasso.tech.eval.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/client")
public class ClientController {

    private final ClientService service;

    @PostMapping
    public String save(@Valid @RequestBody ClientDTO dto) {
        Client client = ClientConverter.convert(dto);
        client = service.save(client);
        return client.getId();
    }

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> list(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                ClientSearchDTO search
    ) {
        Page<Client> pageEntity = service.findAll(PageRequest.of(page, size), search);
        Page<ClientDTO> dtoPage = pageEntity.map(ClientConverter::convert);
        return ResponseEntity.ok(dtoPage);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody ClientUpdateDTO dto) {
        Optional<Client> entity = service.update(id,dto);
        if (!entity.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    //Extras

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> view(@PathVariable String id) {
        Optional<Client> entity = service.findById(id);
        if (!entity.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ClientDTO dto = ClientConverter.convert(entity.get());
        return ResponseEntity.ok(dto);
    }
}
