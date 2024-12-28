package naegamaja_server.naegamaja.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.dto.AuthDto;
import naegamaja_server.naegamaja.domain.auth.service.AuthService;
import naegamaja_server.naegamaja.system.security.service.UserDetailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public AuthDto.SessionIdResponse signIn(@RequestBody AuthDto.SignInRequest request) {
        return authService.signIn(request);
    }

    @PostMapping("/sign-up")
    public AuthDto.SessionIdResponse signUp(@RequestBody AuthDto.SignUpRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody AuthDto.LogoutRequest request) {
        authService.logout(request);
    }

}
