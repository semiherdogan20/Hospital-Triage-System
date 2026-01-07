package erosproje.com.erosProje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ErosProjeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErosProjeApplication.class, args);
	}

}
