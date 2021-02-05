package com.mendeley.test.product_microservice;

import com.mendeley.test.product_microservice.model.Book;
import com.mendeley.test.product_microservice.repository.BookRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductMicroserviceApplication.class, args);
	}

}
