package naegamaja_server.naegamaja;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class NaegamajaApplication implements CommandLineRunner {

	private final StringRedisTemplate redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(NaegamajaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		addAvailableRooms();
	}

	private void addAvailableRooms() {
		ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
		String key = "availableRoomList:";

		redisTemplate.delete(key);

		for (int i = 1; i <= 10; i++) {
			String roomNumberStr = String.valueOf(i);
			zSetOps.add(key, roomNumberStr, i); // roomNumber를 String으로 저장
		}

		System.out.println("availableRoomList ZSET에 1부터 10까지의 숫자가 추가되었습니다.");
	}
}
