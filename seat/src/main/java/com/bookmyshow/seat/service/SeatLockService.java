package com.bookmyshow.seat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SeatLockService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final long LOCK_DURATION = 10;

    public boolean tryLock(String seatId, String lockToken) {

        String key = "seat-lock:" + seatId;

        Boolean acquired = redisTemplate.opsForValue()
                           .setIfAbsent(key, lockToken, Duration.ofMinutes(LOCK_DURATION));

        return Boolean.TRUE.equals(acquired);
    }

    public void releaseLock(String seatId, String lockToken) {

        String key = "seat-lock:" + seatId;

        String currentToken = redisTemplate.opsForValue().get(key);

        if (lockToken.equals(currentToken)) {
            redisTemplate.delete(key);
        }
    }
}
