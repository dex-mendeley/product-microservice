package com.mendeley.test.product_microservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendeley.test.product_microservice.model.Film;
import com.mendeley.test.product_microservice.model.MediaType;
import com.mendeley.test.product_microservice.repository.FilmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FilmRepository repository;

    @Autowired
    ObjectMapper objectMapper;


    Film noIdFilm = new Film("Martin Scorcese", 1.90, "horror", MediaType.BLU_RAY, "Frankenstein");
    Film testFilm = new Film("mock_film_Id", "Martin Scorcese", 1.90, "horror", MediaType.BLU_RAY, "Frankenstein");
    Film testFilm2 = new Film("mock_film_Id_2", "Quentin Tarantino", 1.90, "Action", MediaType.BLU_RAY, "John Wick 10");


    @Test
    public void testAddFilm() throws Exception {
        when(repository.save(noIdFilm)).thenReturn(testFilm);

        mockMvc.perform(post("/films")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noIdFilm))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(testFilm)));
    }

    @Test
    public void testDatabaseError() throws Exception {
        when(repository.save(noIdFilm)).thenThrow(new RuntimeException("Something went wrong"));
        mockMvc.perform(post("/films")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noIdFilm))
        ).andExpect(status().isInternalServerError());

    }

    @Test
    public void testBadRequest() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getAllBooks() throws Exception {
        when(repository.findAll()).thenReturn(List.of(testFilm, testFilm2));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(testFilm, testFilm2))));
    }

    @Test
    public void testServerError() throws Exception {
        when(repository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/films"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteFilm() throws Exception {
        when(repository.findById("mock_film_id")).thenReturn(Optional.ofNullable(testFilm));

        mockMvc.perform(delete("/films/mock_film_id"))
                .andExpect(status().isOk());
    }

    // 404
    @Test
    public void deleteUnknownFilm() throws Exception {
        when(repository.findById("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/films/unknown"))
                .andExpect(status().isNotFound());
    }

    // 500
    @Test
    public void serverError() throws Exception {
        when(repository.findById(anyString())).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(delete("/films/unknown"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateBook() throws Exception {
        Film testFilm = new Film("mock_film_Id", "Martin Scorcese", 1.90, "horror", MediaType.BLU_RAY, "Frankenstein");
        Film testFilm2 = new Film("mock_film_Id", "Quentin Tarantino", 1.90, "Action", MediaType.BLU_RAY, "John Wick 10");

        when(repository.findById("mock_film_Id")).thenReturn(Optional.ofNullable(testFilm));


        mockMvc.perform(patch("/films")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFilm2)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(testFilm2)));

    }

}
