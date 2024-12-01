package naegamaja_server.naegamaja;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.game.GameStatus;
import naegamaja_server.naegamaja.domain.game.Service.BroadcastService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class NaegamajaApplication {
	private final BroadcastService broadcastService;

	public static void main(String[] args) {
		SpringApplication.run(NaegamajaApplication.class, args);
	}

}
