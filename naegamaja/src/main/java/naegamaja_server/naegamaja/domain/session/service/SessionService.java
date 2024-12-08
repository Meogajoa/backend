package naegamaja_server.naegamaja.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    public String createSessionId() {
        return UUID.randomUUID().toString();
    }

}
