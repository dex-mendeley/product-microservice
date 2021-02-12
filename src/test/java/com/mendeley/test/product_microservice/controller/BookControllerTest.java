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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
