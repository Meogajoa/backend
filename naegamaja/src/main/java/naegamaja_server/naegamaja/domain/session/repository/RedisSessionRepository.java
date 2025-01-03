package naegamaja_server.naegamaja.domain.session.repository;

import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisSessionRepository extends CrudRepository<UserSession, String> {

}
