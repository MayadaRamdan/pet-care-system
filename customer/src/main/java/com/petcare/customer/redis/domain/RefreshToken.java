package com.petcare.customer.redis.domain;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("customer_refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String token; // UUID

    @Indexed
    private Long userId;

    private String deviceInfo; // User-Agent or device identifier

    private String ipAddress;

    private LocalDateTime createdAt;

    @TimeToLive
    private Long ttl; // Time to live in seconds
}
