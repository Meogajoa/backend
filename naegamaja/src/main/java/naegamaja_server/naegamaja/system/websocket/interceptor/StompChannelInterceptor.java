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

        if(command != null) {
            System.out.println("command: " + command + "로 들어왔다");
            System.out.println("sessionId: " + sessionId);
        }

        if(command == StompCommand.CONNECT) {
            return message;
        }

        if(command == StompCommand.DISCONNECT) {
            return message;
        }

        if(command == null) return message;

//        if(!redisService.isValidSessionId(sessionId)) {
//            System.out.println("세션아이디가 유효하지 않다");
//        }else{
//            System.out.println("세션아이디가 유효하다");
//        }

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
                break;
            case SEND:
                break;
        }

        return message;
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        System.out.println("최종구독");
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        String destination = accessor.getFirstNativeHeader("DdingjiSessionId");

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
