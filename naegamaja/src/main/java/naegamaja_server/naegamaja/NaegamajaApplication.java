package naegamaja_server.naegamaja;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class NaegamajaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaegamajaApplication.class, args);
	}

}
