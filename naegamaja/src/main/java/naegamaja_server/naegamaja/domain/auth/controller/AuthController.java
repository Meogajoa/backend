package naegamaja_server.naegamaja.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.dto.AuthDto;
import naegamaja_server.naegamaja.domain.auth.service.AuthService;
import naegamaja_server.naegamaja.system.security.service.UserDetailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public AuthDto.SessionIdResponse signIn(
            @RequestParam String email,
            @RequestParam String password) {
        AuthDto.SignInRequest request = AuthDto.SignInRequest.of(email, password);
        return authService.signIn(request);
    }

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public AuthDto.SessionIdResponse signUp(@RequestParam String email, @RequestParam String nickName, @RequestParam String password) {
        AuthDto.SignUpRequest request = AuthDto.SignUpRequest.of(email, nickName, password);
        return authService.signUp(request);
    }

    @PostMapping("/sign-out")
    public void signOut(@RequestBody AuthDto.LogoutRequest request) {
        authService.logout(request);
    }

}
