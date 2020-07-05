package io.diego.compasso.tech.eval.repository;

import io.diego.compasso.tech.eval.model.entity.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends MongoRepository<City, String> {

}
