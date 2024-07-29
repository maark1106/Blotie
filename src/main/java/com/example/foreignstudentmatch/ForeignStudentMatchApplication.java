package com.example.foreignstudentmatch;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ForeignStudentMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForeignStudentMatchApplication.class, args);
	}

}