package com.mendeley.test.product_microservice.controller;

import com.mendeley.test.product_microservice.model.Book;
import com.mendeley.test.product_microservice.model.Film;
import com.mendeley.test.product_microservice.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8090")
@RequestMapping("/films")
public class FilmController {

    @Autowired
    FilmRepository filmRepository;

    @PostMapping()
    public ResponseEntity<Film> addFilm(@RequestBody Film film ){
        ResponseEntity entity;
        Optional<Film> opt = Optional.ofNullable(film);

        try {
            if (opt.isPresent()) {
                Film savedFilm = filmRepository.save(opt.get());
                entity = new ResponseEntity(savedFilm, HttpStatus.OK);
            } else {
                entity = new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            entity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return entity;
    }

    @GetMapping()
    public ResponseEntity<Film> getAll() {
        ResponseEntity entity;

        try {
            List<Film> books = filmRepository.findAll();
            entity = new ResponseEntity(books, HttpStatus.OK);
        } catch (Exception e) {
            entity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return entity;
    }

    @DeleteMapping("/{film_id}")
    public ResponseEntity<Book> deletefilm(@PathVariable String isbn) {
        ResponseEntity entity;

        try {
            Optional<Film> opt = filmRepository.findById(isbn);

            if (opt.isPresent()) {
                filmRepository.deleteById(isbn);
                entity = new ResponseEntity(HttpStatus.OK);
            } else {
                entity = new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            ;
            return entity;

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        @PatchMapping()
        public ResponseEntity<Film> updatdeFilm(@RequestBody Film filmUpdate) {
            ResponseEntity entity;

            Optional<Film> opt = filmRepository.findById(filmUpdate.getFilm_id());
            if(opt.isPresent()) {
                Film savedFilm  = opt.get();
                if(filmUpdate.getTitle() != null) {
                    savedFilm.setTitle(filmUpdate.getTitle());
                }

                if(filmUpdate.getDirector() != null) {
                    savedFilm.setDirector(filmUpdate.getDirector());
                }

                if(filmUpdate.getGenre() != null) {
                    savedFilm.setGenre(filmUpdate.getGenre());
                }

                if(filmUpdate.getPrice() != null) {
                    savedFilm.setPrice(filmUpdate.getPrice());
                }

                if(filmUpdate.getMediaType()!= null) {
                    savedFilm.setMediaType(filmUpdate.getMediaType());
                }

                filmRepository.save(savedFilm);
                return new ResponseEntity(savedFilm, HttpStatus.OK);
            } else {
                return null;
            }
    }
}
