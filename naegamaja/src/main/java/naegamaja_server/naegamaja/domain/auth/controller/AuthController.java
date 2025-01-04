package naegamaja_server.naegamaja.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.dto.AuthDto;
import naegamaja_server.naegamaja.domain.auth.service.AuthService;
import org.springframework.http.MediaType;
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
    public AuthDto.SignUpResponse signUp(@RequestParam String email, @RequestParam String nickname, @RequestParam String password) {
        AuthDto.SignUpRequest request = AuthDto.SignUpRequest.of(email, nickname, password);
        return authService.signUp(request);
    }

    @PostMapping("/sign-out")
    public void signOut(@RequestHeader String authorization) {
        authService.logout(authorization);
    }

}
