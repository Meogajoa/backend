package naegamaja_server.naegamaja.system.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.redis.service.RedisPubSubSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.*;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public Executor virtualThreadExecutor() {
        return Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("redis-listener-%d").factory()
        );
    }


    @Bean
    @Primary
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
            RedisConnectionFactory connectionFactory) {

        StringRedisSerializer serializer = new StringRedisSerializer();

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .executor(virtualThreadExecutor())
                        .pollTimeout(Duration.ofSeconds(2))
                        .serializer(serializer)
                        .build();


        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(connectionFactory, options);
        container.start();
        return container;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter RoomChatListenerAdapter,
            MessageListenerAdapter GameChatListenerAdapter,
            MessageListenerAdapter RoomInfoListenerAdaptor,
            MessageListenerAdapter GameStartListenerAdapter,
            MessageListenerAdapter UserInfoListenerAdapter,
            MessageListenerAdapter GameDayOrNightListenerAdapter,
            MessageListenerAdapter MiniGameNoticeListenerAdapter,
            MessageListenerAdapter ButtonGameStatusListenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(RoomChatListenerAdapter, new ChannelTopic("pubsub:roomChat"));
        container.addMessageListener(GameChatListenerAdapter, new ChannelTopic("pubsub:gameChat"));
        container.addMessageListener(RoomInfoListenerAdaptor, new ChannelTopic("pubsub:roomInfo"));
        container.addMessageListener(GameStartListenerAdapter, new ChannelTopic("pubsub:gameStart"));
        container.addMessageListener(UserInfoListenerAdapter, new ChannelTopic("pubsub:userInfo"));
        container.addMessageListener(GameDayOrNightListenerAdapter, new ChannelTopic("pubsub:gameDayOrNight"));
        container.addMessageListener(MiniGameNoticeListenerAdapter, new ChannelTopic("pubsub:miniGameNotice"));
        container.addMessageListener(ButtonGameStatusListenerAdapter, new ChannelTopic("pubsub:buttonGameStatus"));

        return container;
    }

    @Bean
    public MessageListenerAdapter RoomChatListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "roomChat");
    }

    @Bean
    public MessageListenerAdapter GameChatListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "gameChat");
    }

    @Bean
    public MessageListenerAdapter RoomInfoListenerAdaptor(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "roomInfo");
    }

    @Bean
    public MessageListenerAdapter GameStartListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "gameStart");
    }

    @Bean
    public MessageListenerAdapter UserInfoListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "userInfo");
    }

    @Bean
    public MessageListenerAdapter GameDayOrNightListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "gameDayOrNight");
    }

    @Bean
    public MessageListenerAdapter MiniGameNoticeListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "miniGameNotice");
    }

    @Bean
    public MessageListenerAdapter ButtonGameStatusListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "buttonGameStatus");
    }








}
