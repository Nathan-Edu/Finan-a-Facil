package alves.ransani.ifpr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IfprApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfprApplication.class, args);
	}

}
