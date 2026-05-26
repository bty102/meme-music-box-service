package com.bty.karaoke.mememusicboxservice.websocket.interceptor;

import com.bty.karaoke.mememusicboxservice.config.CustomJwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor
        implements ChannelInterceptor {

    private final CustomJwtDecoder jwtDecoder;

    private final JwtAuthenticationConverter
            jwtAuthenticationConverter;

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader =
                    accessor.getFirstNativeHeader(
                            "Authorization"
                    );

            if (authHeader == null
                    || !authHeader.startsWith("Bearer ")) {

                throw new RuntimeException("Missing token");
            }

            String token =
                    authHeader.substring(7);

            Jwt jwt = jwtDecoder.decode(token);

            Authentication authentication =
                    jwtAuthenticationConverter.convert(jwt);

            accessor.setUser(authentication);
//            System.out.println(authentication.getName());
            accessor.setLeaveMutable(true);

            return MessageBuilder
                    .createMessage(message.getPayload(), accessor.getMessageHeaders());
        }

        return message;
    }
}
