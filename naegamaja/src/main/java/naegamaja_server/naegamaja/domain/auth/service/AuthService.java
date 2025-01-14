package naegamaja_server.naegamaja.domain.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.dto.AuthDto;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.repository.RedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.service.SessionService;
import naegamaja_server.naegamaja.domain.session.state.State;
import naegamaja_server.naegamaja.domain.user.dto.UserDto;
import naegamaja_server.naegamaja.domain.user.entity.User;
import naegamaja_server.naegamaja.domain.user.repository.UserRepository;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final RedisAuthService redisAuthService;
    private final CustomRedisSessionRepository customRedisSessionRepository;
    private final RedisSessionRepository redisSessionRepository;

    @Transactional
    public AuthDto.SessionIdResponse signIn(AuthDto.SignInRequest request) {
        User found = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RestException(ErrorCode.AUTH_USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), found.getPassword()))
            throw new RestException(ErrorCode.AUTH_BAD_CREDENTIALS);

        UserDto.UserResponse userResponse = UserDto.UserResponse.from(found);

        String sessionId = sessionService.createSessionId();

        if(customRedisSessionRepository.isUserSessionActive(found.getNickname())) {
            return AuthDto.SessionIdResponse.builder()
                    .user(userResponse)
                    .sessionId(customRedisSessionRepository.getSessionIdByNickname(found.getNickname()))
                    .build();
        }

        UserSession userSession = UserSession.builder()
                .nickname(found.getNickname())
                .state(State.LOBBY)
                .sessionId(sessionId)
                .roomId("")
                .isInGame(false)
                .isInRoom(false)
                .build();

        redisAuthService.saveSession(userSession);

        return AuthDto.SessionIdResponse.builder()
                .user(userResponse)
                .sessionId(sessionId)
                .build();

    }

    @Transactional
    public AuthDto.SignUpResponse signUp(AuthDto.SignUpRequest request) {
        Optional<User> found = userRepository.findByEmail(request.getEmail());

        if(found.isPresent()) throw new RestException(ErrorCode.GLOBAL_ALREADY_EXIST);

        found = userRepository.findByNickname(request.getNickname());

        if(found.isPresent()) throw new RestException(ErrorCode.GLOBAL_ALREADY_EXIST);

        User toSave = request.toEntity(passwordEncoder);

        User saved = userRepository.save(toSave);

        return AuthDto.SignUpResponse.builder()
                .user(UserDto.UserResponse.from(saved))
                .build();
    }

    public void logout(String sessionId) {
        redisAuthService.deleteSessionId(sessionId);
    }

}
