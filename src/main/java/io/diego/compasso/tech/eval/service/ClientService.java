package io.diego.compasso.tech.eval.service;

import io.diego.compasso.tech.eval.converter.client.ClientSearchDTOConverter;
import io.diego.compasso.tech.eval.model.dto.client.ClientSearchDTO;
import io.diego.compasso.tech.eval.model.dto.client.ClientUpdateDTO;
import io.diego.compasso.tech.eval.model.entity.Client;
import io.diego.compasso.tech.eval.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientService {

    private final ClientRepository repository;
    private final ModelMapper modelMapper;

    public Client save(Client client) {
        return repository.save(client);
    }

    public Optional<Client> update(String id, ClientUpdateDTO dto) {
        Optional<Client> optionalClient = findById(id);
        if (!optionalClient.isPresent()) {
            return optionalClient;
        }
        Client client = optionalClient.get();
        modelMapper.map(dto, client);
        return Optional.of(repository.save(client));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Page<Client> findAll(Pageable pageable, ClientSearchDTO search) {
        Client example = ClientSearchDTOConverter.convert(search);
        return repository.findAll(Example.of(example, getExampleMatcher()), pageable);
    }

    public static ExampleMatcher getExampleMatcher() {
        return ExampleMatcher.matching()
                .withMatcher("id", exact())
                .withMatcher("name", contains().ignoreCase());
    }

    public Optional<Client> findById(String id) {
        return repository.findById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
