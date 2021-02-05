package com.mendeley.test.product_microservice.controller;

import com.mendeley.test.product_microservice.model.Book;
import com.mendeley.test.product_microservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book ){
        ResponseEntity entity;

        Book savedBook = bookRepository.save(book);

        entity = new ResponseEntity(savedBook, HttpStatus.OK);

        return entity;
    }
}
