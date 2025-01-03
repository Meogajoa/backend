package naegamaja_server.naegamaja.domain.room.repository;

import naegamaja_server.naegamaja.domain.room.domain.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisRoomRepository extends CrudRepository<Room, String> {

}
