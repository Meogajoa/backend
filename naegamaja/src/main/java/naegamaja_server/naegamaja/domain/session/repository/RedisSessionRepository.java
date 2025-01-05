package naegamaja_server.naegamaja.domain.session.repository;

import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RedisSessionRepository extends CrudRepository<UserSession, String> {

    Optional<UserSession> findByNickname(String nickname);
}
