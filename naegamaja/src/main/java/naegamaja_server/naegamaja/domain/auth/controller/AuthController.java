package naegamaja_server.naegamaja.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.dto.AuthDto;
import naegamaja_server.naegamaja.domain.auth.service.AuthService;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.repository.RedisSessionRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CustomRedisSessionRepository customRedisSessionRepository;

    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public AuthDto.SessionIdResponse signIn(
            @RequestParam String email,
            @RequestParam String password) {
        AuthDto.SignInRequest request = AuthDto.SignInRequest.of(email, password);
        return authService.signIn(request);
    }

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public AuthDto.SignUpResponse signUp(@RequestParam String email, @RequestParam String nickname, @RequestParam String password) {
        AuthDto.SignUpRequest request = AuthDto.SignUpRequest.of(email, nickname, password);
        return authService.signUp(request);
    }

    @PostMapping("/sign-out")
    public void signOut(@RequestHeader String authorization) {
        authService.logout(authorization);
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestHeader String authorization) {
        if(customRedisSessionRepository.isValidSessionId(authorization)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
