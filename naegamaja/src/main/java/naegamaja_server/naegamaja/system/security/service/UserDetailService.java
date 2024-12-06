package naegamaja_server.naegamaja.system.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.user.entity.User;
import naegamaja_server.naegamaja.domain.user.repository.UserRepository;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import naegamaja_server.naegamaja.system.security.model.UserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailService {
    private final UserRepository userRepository;

    public UserDetail loadUserByEmail(String email) {
        User found = userRepository.findById(email)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        return UserDetail.from(found);
    }


}
