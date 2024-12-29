package naegamaja_server.naegamaja;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.repository.RedisRoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class NaegamajaApplication implements CommandLineRunner {

	private final RedisTemplate<String, Object> redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(NaegamajaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		addAvailableRooms();
	}

	private void addAvailableRooms() {
		ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
		String key = "availableRoomList:";

		redisTemplate.delete(key);

		for (int i = 1; i <= 1000; i++) {
			zSetOps.add(key, i, i); // roomNumber를 Integer로 저장
		}

		System.out.println("availableRoomList ZSET에 1부터 1000까지의 숫자가 추가되었습니다.");
	}
}
