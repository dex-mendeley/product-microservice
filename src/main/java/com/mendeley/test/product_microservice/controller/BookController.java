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
    public ResponseEntity<Book> updatedBook(@RequestBody Book book) {
        ResponseEntity entity;

        Optional<Book> opt = bookRepository.findById(book.getIsbn());
        if(opt.isPresent()) {
            Book updatedBook = new Book();
            Book savedBook = opt.get();

            updatedBook.setIsbn(book.getIsbn());

            if(book.getTitle() != null) {
                updatedBook.setTitle(book.getTitle());
            } else {
                updatedBook.setTitle(savedBook.getTitle());
            }

            if(book.getAuthor_first() != null) {
                updatedBook.setAuthor_first(book.getAuthor_first());
            } else {
                updatedBook.setAuthor_first(savedBook.getAuthor_first());
            }

            if(book.getAuthor_last() != null) {
                updatedBook.setAuthor_last(book.getAuthor_last());
            } else {
                updatedBook.setAuthor_last(savedBook.getAuthor_last());
            }

            return new ResponseEntity(updatedBook, HttpStatus.OK);
        } else {
            return null;
        }
    }
}
