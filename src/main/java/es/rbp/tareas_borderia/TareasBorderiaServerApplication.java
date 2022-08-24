package es.rbp.tareas_borderia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TareasBorderiaServerApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TareasBorderiaServerApplication.class, args);
	}
}
