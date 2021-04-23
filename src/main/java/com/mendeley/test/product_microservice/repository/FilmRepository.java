package com.mendeley.test.product_microservice.repository;

import com.mendeley.test.product_microservice.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends MongoRepository<Film, String> {
}
