package naegamaja_server.naegamaja.domain.session.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.service.RedisAuthService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final RedisAuthService redisAuthService;

    public String createSessionId() {
        String sessionId;
        do{
            sessionId = UUID.randomUUID().toString();
        }while(redisAuthService.isValidSessionId(sessionId));
        
        return sessionId;
    }

}
