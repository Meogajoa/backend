package naegamaja_server.naegamaja.domain.room.repository;

import naegamaja_server.naegamaja.domain.room.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRoomRepository extends CrudRepository<Room, String> {

}
