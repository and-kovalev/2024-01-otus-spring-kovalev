package ru.otus.hw;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMongock
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.printf("Чтобы перейти на страницу сайта открывай: %n%s%n",
				"http://localhost:8080");
	}

}
