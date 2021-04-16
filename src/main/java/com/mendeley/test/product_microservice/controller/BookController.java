package com.mendeley.test.product_microservice.controller;

import com.mendeley.test.product_microservice.model.Book;
import com.mendeley.test.product_microservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8090")
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

    @GetMapping()
    public ResponseEntity<Book> getAllBooks() {
        ResponseEntity entity;

        try {
            List<Book> books = bookRepository.findAll();
            entity = new ResponseEntity(books, HttpStatus.OK);
        } catch (Exception e) {
            entity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return entity;
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Book> deleteBook(@PathVariable String isbn) {
        ResponseEntity entity;

        try {
            Optional<Book> opt = bookRepository.findById(isbn);

            if (opt.isPresent()) {
                bookRepository.deleteById(isbn);
                entity = new ResponseEntity(HttpStatus.OK);
            } else {
                entity = new ResponseEntity(HttpStatus.NOT_FOUND);
            };
            return entity;

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping()
    public ResponseEntity<Book> updatedBook(@RequestBody Book bookUpdate) {
        ResponseEntity entity;

        Optional<Book> opt = bookRepository.findById(bookUpdate.getIsbn());
        if(opt.isPresent()) {
            Book savedBook = opt.get();
            if(bookUpdate.getTitle() != null) {
                savedBook.setTitle(bookUpdate.getTitle());
            }

            if(bookUpdate.getAuthor_first() != null) {
                savedBook.setAuthor_first(bookUpdate.getAuthor_first());
            }

            if(bookUpdate.getAuthor_last() != null) {
                savedBook.setAuthor_last(bookUpdate.getAuthor_last());
            }

            if(bookUpdate.getPrice() != null) {
                savedBook.setPrice(bookUpdate.getPrice());
            }

            if(bookUpdate.getImg_url() != null) {
                savedBook.setImg_url(bookUpdate.getImg_url());
            }

            bookRepository.save(savedBook);
            return new ResponseEntity(savedBook, HttpStatus.OK);
        } else {
            return null;
        }
    }
}
