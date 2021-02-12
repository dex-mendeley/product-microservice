package com.mendeley.test.product_microservice.controller;

import com.mendeley.test.product_microservice.model.Book;
import com.mendeley.test.product_microservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @PostMapping()
    public ResponseEntity<Book> addBook(@RequestBody Book book ){
        ResponseEntity entity;
        Optional<Book> opt = Optional.ofNullable(book);

        try {
            if (opt.isPresent()) {
                Book savedBook = bookRepository.save(opt.get());
                entity = new ResponseEntity(savedBook, HttpStatus.OK);
            } else {
                entity = new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            entity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return entity;
    }

}
