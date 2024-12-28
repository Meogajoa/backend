package naegamaja_server.naegamaja.domain.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.dto.AuthDto;
import naegamaja_server.naegamaja.domain.redis.service.RedisService;
import naegamaja_server.naegamaja.domain.session.service.SessionService;
import naegamaja_server.naegamaja.domain.user.dto.UserDto;
import naegamaja_server.naegamaja.domain.user.entity.User;
import naegamaja_server.naegamaja.domain.user.repository.UserRepository;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import naegamaja_server.naegamaja.system.security.filter.SessionIdAuthenticationFilter;
import naegamaja_server.naegamaja.system.security.provider.SessionIdAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final RedisService redisService;

    @Transactional
    public AuthDto.SessionIdResponse signIn(AuthDto.SignInRequest request) {
        User found = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RestException(ErrorCode.AUTH_USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), found.getPassword()))
            throw new RestException(ErrorCode.AUTH_BAD_CREDENTIALS);

        UserDto.UserResponse userResponse = UserDto.UserResponse.from(found);

        String sessionId = sessionService.createSessionId();

        redisService.saveSessionId(sessionId, found.getEmail());

        return AuthDto.SessionIdResponse.builder()
                .user(userResponse)
                .sessionId(sessionId)
                .build();

    }

    @Transactional
    public AuthDto.SessionIdResponse signUp(AuthDto.SignUpRequest request) {
        Optional<User> found = userRepository.findById(request.getEmail());

        if(found.isPresent()) throw new RestException(ErrorCode.GLOBAL_ALREADY_EXIST);

        User toSave = request.toEntity(passwordEncoder);

        User saved = userRepository.save(toSave);

        String sessionId = sessionService.createSessionId();

        redisService.saveSessionId(sessionId, saved.getEmail());

        return AuthDto.SessionIdResponse.of(saved, sessionId);
    }

    @Transactional
    public void logout(AuthDto.LogoutRequest request) {
        redisService.deleteSessionId(request.getSessionId());
    }

}
