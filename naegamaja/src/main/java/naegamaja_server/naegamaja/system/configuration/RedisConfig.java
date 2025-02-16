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
import org.springframework.data.redis.connection.Message;
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
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter RoomChatListenerAdapter,
            MessageListenerAdapter GameChatListenerAdapter,
            MessageListenerAdapter RoomInfoListenerAdaptor,
            MessageListenerAdapter GameStartListenerAdapter,
            MessageListenerAdapter UserInfoListenerAdapter,
            MessageListenerAdapter GameDayOrNightListenerAdapter,
            MessageListenerAdapter MiniGameNoticeListenerAdapter,
            MessageListenerAdapter ButtonGameStatusListenerAdapter,
            MessageListenerAdapter GameEndListenerAdapter,
            MessageListenerAdapter GameChatToUserListenerAdapter,
            MessageListenerAdapter BlackChatListenerAdapter,
            MessageListenerAdapter WhiteChatListenerAdapter,
            MessageListenerAdapter EliminatedChatListenerAdapter,
            MessageListenerAdapter GameUserInfoPersonalListenerAdapter,
            MessageListenerAdapter GameUserListInfoListenerAdapter,
            MessageListenerAdapter GameChatListListenerAdapter,
            MessageListenerAdapter BlackChatListListenerAdapter,
            MessageListenerAdapter WhiteChatListListenerAdapter,
            MessageListenerAdapter EliminatedChatListListenerAdapter,
            MessageListenerAdapter PersonalChatListListenerAdapter,
            MessageListenerAdapter RedChatListenerAdapter,
            MessageListenerAdapter RedChatListListenerAdapter,
            MessageListenerAdapter VoteGameStatusListenerAdapter,
            MessageListenerAdapter VoteResultListenerAdapter,
            MessageListenerAdapter eliminatedUserListenerAdapter
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
        container.addMessageListener(GameEndListenerAdapter, new ChannelTopic("pubsub:gameEnd"));
        container.addMessageListener(GameChatToUserListenerAdapter, new ChannelTopic("pubsub:gameChatToUser"));
        container.addMessageListener(BlackChatListenerAdapter, new ChannelTopic("pubsub:blackChat"));
        container.addMessageListener(WhiteChatListenerAdapter, new ChannelTopic("pubsub:whiteChat"));
        container.addMessageListener(EliminatedChatListenerAdapter, new ChannelTopic("pubsub:eliminatedChat"));
        container.addMessageListener(GameUserInfoPersonalListenerAdapter, new ChannelTopic("pubsub:gameUserInfoPersonal"));
        container.addMessageListener(GameUserListInfoListenerAdapter, new ChannelTopic("pubsub:gameUserListInfo"));
        container.addMessageListener(GameChatListListenerAdapter, new ChannelTopic("pubsub:gameChatList"));
        container.addMessageListener(BlackChatListListenerAdapter, new ChannelTopic("pubsub:blackChatList"));
        container.addMessageListener(WhiteChatListListenerAdapter, new ChannelTopic("pubsub:whiteChatList"));
        container.addMessageListener(EliminatedChatListListenerAdapter, new ChannelTopic("pubsub:eliminatedChatList"));
        container.addMessageListener(PersonalChatListListenerAdapter, new ChannelTopic("pubsub:personalChatList"));
        container.addMessageListener(RedChatListenerAdapter, new ChannelTopic("pubsub:redChat"));
        container.addMessageListener(RedChatListListenerAdapter, new ChannelTopic("pubsub:redChatList"));
        container.addMessageListener(VoteGameStatusListenerAdapter, new ChannelTopic("pubsub:voteGameStatus"));
        container.addMessageListener(VoteResultListenerAdapter, new ChannelTopic("pubsub:voteResult"));
        container.addMessageListener(eliminatedUserListenerAdapter, new ChannelTopic("pubsub:eliminatedUser"));


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

    @Bean
    public MessageListenerAdapter GameEndListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "gameEnd");
    }

    @Bean
    public MessageListenerAdapter GameChatToUserListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "gameChatToUser");
    }

    @Bean
    public MessageListenerAdapter BlackChatListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "blackChat");
    }

    @Bean
    public MessageListenerAdapter WhiteChatListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "whiteChat");
    }

    @Bean
    public MessageListenerAdapter EliminatedChatListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "eliminatedChat");
    }

    @Bean
    public MessageListenerAdapter GameUserInfoPersonalListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "gameUserInfoPersonal");
    }

    @Bean
    public MessageListenerAdapter GameUserListInfoListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "gameUserListInfo");
    }

    @Bean
    public MessageListenerAdapter GameChatListListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "gameChatList");
    }

    @Bean
    public MessageListenerAdapter BlackChatListListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "blackChatList");
    }

    @Bean
    public MessageListenerAdapter WhiteChatListListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "whiteChatList");
    }

    @Bean
    public MessageListenerAdapter RedChatListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "redChat");
    }

    @Bean
    public MessageListenerAdapter RedChatListListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "redChatList");
    }




    @Bean
    public MessageListenerAdapter EliminatedChatListListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "eliminatedChatList");
    }

    @Bean
    public MessageListenerAdapter PersonalChatListListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "personalChatList");
    }

    @Bean
    public MessageListenerAdapter VoteGameStatusListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "voteGameStatus");
    }

    @Bean
    public MessageListenerAdapter VoteResultListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "voteResult");
    }

    @Bean
    public MessageListenerAdapter eliminatedUserListenerAdapter(RedisPubSubSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "eliminatedUser");
    }









}
