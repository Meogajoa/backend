//package naegamaja_server.naegamaja.system.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.stream.MapRecord;
//import org.springframework.data.redis.stream.StreamMessageListenerContainer;
//import java.time.Duration;
//
//@Configuration
//public class CustomRedisStreamConfig {
//
//    @Bean
//    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
//            RedisConnectionFactory redisConnectionFactory) {
//
//        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
//                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
//                        .pollTimeout(Duration.ofMillis(100))
//                        .build();
//
//        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
//                StreamMessageListenerContainer.create(redisConnectionFactory, options);
//
//        container.start();
//        return container;
//    }
//}
