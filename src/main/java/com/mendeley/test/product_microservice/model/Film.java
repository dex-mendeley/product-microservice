package com.mendeley.test.product_microservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @Id
    private String film_id;
    private String director;
    private Double price;
    private String genre;
    private MediaType mediaType;
    private String title;

    public Film(String director, Double price, String genre, MediaType mediaType, String title) {
        this.director = director;
        this.price = price;
        this.genre = genre;
        this.mediaType = mediaType;
        this.title = title;
    }
}
