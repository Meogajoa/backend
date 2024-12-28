package naegamaja_server.naegamaja.domain.user.repository;

import naegamaja_server.naegamaja.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndPassword(String email, String encodedPassword);
}
