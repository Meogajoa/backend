package naegamaja_server.naegamaja.system.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naegamaja_server.naegamaja.domain.redis.service.RedisService;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompChannelInterceptor implements ChannelInterceptor {

    private final RedisService redisService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getFirstNativeHeader("DdingjiSessionId");

        StompCommand command = accessor.getCommand();
        System.out.println("command: " + command + "로 들어왔다");

        if(!redisService.isValidSessionId(sessionId))
            throw new RestException(ErrorCode.GLOBAL_UNAUTHORIZED);

        if(command == null) return message;

        switch (command) {
            case SUBSCRIBE:
                handleSubscribe(accessor);
                break;
            case UNSUBSCRIBE:
                handleUnsubscribe(accessor);
                break;
            case DISCONNECT:
                handleDisconnect(accessor);
                break;
            case CONNECT:
                handleConnect(accessor);
        }

        return message;
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {

        String destination = accessor.getDestination();

        System.out.println("구독함");
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        String destination = accessor.getFirstNativeHeader("DdingjiSessionId");

        if(!redisService.isValidSessionId(sessionId))
            throw new RestException(ErrorCode.GLOBAL_UNAUTHORIZED);

        System.out.println("sessionId: " + sessionId);
        System.out.println("destination: " + destination);
        System.out.println("구독해제함");




    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        redisService.deleteSessionId(sessionId);
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        if(!StringUtils.hasText(sessionId))
            throw new RestException(ErrorCode.GLOBAL_BAD_REQUEST);
    }
}
