package naegamaja_server.naegamaja.system.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naegamaja_server.naegamaja.domain.auth.service.RedisAuthService;
import naegamaja_server.naegamaja.system.websocket.model.StompPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompChannelInterceptor implements ChannelInterceptor {

    private final RedisAuthService redisAuthService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getFirstNativeHeader("Authorization");

        StompCommand command = accessor.getCommand();

        if(command != null) {
            System.out.println("command: " + command + "로 들어왔다");
            System.out.println("sessionId: " + sessionId);
        }

        if(command == StompCommand.DISCONNECT) {
            return message;
        }

        if(command == null) return message;

        if(!redisAuthService.isValidSessionId(sessionId)) {
            System.out.println("세션아이디가 유효하지 않다");
        }else{
            System.out.println("세션아이디가 유효하다");
        }

        switch (command) {
            case SUBSCRIBE:
                handleSubscribe(accessor, sessionId);
                break;
            case UNSUBSCRIBE:
                handleUnsubscribe(accessor, sessionId);
                break;
            case DISCONNECT:
                handleDisconnect(accessor, sessionId);
                break;
            case CONNECT:
                handleConnect(accessor, sessionId);
                break;
            case SEND:
                break;
        }

        return message;
    }

    private void handleSubscribe(StompHeaderAccessor accessor, String sessionId) {
        System.out.println("최종구독");
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor, String sessionId) {

    }

    private void handleDisconnect(StompHeaderAccessor accessor, String sessionId) {
        redisAuthService.deleteSessionId(sessionId);
    }

    private void handleConnect(StompHeaderAccessor accessor, String sessionId) {
        if (!redisAuthService.isValidSessionId(sessionId)) {
//            throw new RestException(ErrorCode.AUTH_SESSION_EXPIRED);
        }

        accessor.setUser(new StompPrincipal(sessionId));
    }


}
