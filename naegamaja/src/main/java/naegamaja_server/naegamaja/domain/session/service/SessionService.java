package naegamaja_server.naegamaja.domain.session.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.service.RedisAuthService;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final RedisAuthService redisAuthService;
    private final CustomRedisSessionRepository customRedisSessionRepository;

    public String createSessionId() {
        String sessionId;
        do{
            sessionId = UUID.randomUUID().toString();
        }while(redisAuthService.isValidSessionId(sessionId));

        return sessionId;
    }

    public String getNicknameBySessionId(String sessionId) {
        return customRedisSessionRepository.getNicknameBySessionId(sessionId);
    }

}
