package com.mendeley.test.product_microservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendeley.test.product_microservice.model.Book;
import com.mendeley.test.product_microservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    Book testBook = new Book("mock_isbn", "test_title", "test_firstName", "test_lastName");
    Book testBook2 = new Book("mock_isbn_2", "Second title", "Brown", "Dan");
    Book noISBN = new Book("test_title", "test_firstName", "test_lastName");

    @Test
    public void testAddBook() throws Exception {
        when(repository.save(noISBN)).thenReturn(testBook);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noISBN))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(testBook)));
    }

    @Test
    public void testDatabaseError() throws Exception {
        when(repository.save(noISBN)).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noISBN))
        ).andExpect(status().isInternalServerError());

    }

    @Test
    public void testBadRequest() throws Exception {
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getAllBooks() throws Exception {
        when(repository.findAll()).thenReturn(List.of(testBook, testBook2));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(testBook, testBook2))));
    }

    @Test
    public void testServerError() throws Exception {
        when(repository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/books"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteBook() throws Exception {
        when(repository.findById("foobar")).thenReturn(Optional.ofNullable(testBook));

        mockMvc.perform(delete("/books/foobar"))
                .andExpect(status().isOk());
    }

    // 404
    @Test
    public void deleteUnknownBook() throws Exception {
        when(repository.findById("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/books/unknown"))
                .andExpect(status().isNotFound());
    }

    // 500
    @Test
    public void serverError() throws Exception {
        when(repository.findById(anyString())).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(delete("/books/unknown"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateBook() throws Exception {
        Book updatedBook = new Book("mock_isbn", "updated_title", "test_firstName", "test_lastName");
        Book update = new Book("mock_isbn", "updated_title", null, null);

        when(repository.findById("mock_isbn")).thenReturn(Optional.ofNullable(updatedBook));


        mockMvc.perform(patch("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedBook)));

    }


}
