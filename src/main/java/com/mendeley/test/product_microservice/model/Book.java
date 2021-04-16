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
public class Book {

    @Id
    private String isbn;

    private String title;
    private String author_first;
    private String author_last;
    private Double price;
    private String img_url;

    public Book(String title, String author_first, String author_last, Double price, String img_url) {
        this.title = title;
        this.author_first = author_first;
        this.author_last = author_last;
        this.price = price;
        this.img_url = img_url;
    }
}
