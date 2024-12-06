package naegamaja_server.naegamaja.domain.user.repository;

import naegamaja_server.naegamaja.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
