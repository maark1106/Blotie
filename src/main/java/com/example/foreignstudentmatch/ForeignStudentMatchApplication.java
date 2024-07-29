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

@Component
@Profile("dev")
class DevEnvLoader {
	public DevEnvLoader() {
		Dotenv dotenv = Dotenv.configure().directory("/home/ec2-user/Blotie/.env").load();

		for (DotenvEntry entry : dotenv.entries()) {
			System.setProperty(entry.getKey(), entry.getValue());
			System.out.println("Setting environment variable: " + entry.getKey() + "=" + entry.getValue());
		}
	}
}
