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

    public boolean tryLock(String seatId,String showId, String lockToken) {

        String key = "seat-lock:" + seatId + "-showid:" + showId.toLowerCase();

        Boolean acquired = redisTemplate.opsForValue()
                           .setIfAbsent(key, lockToken, Duration.ofMinutes(LOCK_DURATION));

        return Boolean.TRUE.equals(acquired);
    }

    public boolean validateLock(String seatId, String showId, String userId) {
        String key = "seat-lock:" + seatId + "-showid:" + showId.toLowerCase();
        String owner = redisTemplate.opsForValue().get(key);
        return userId.equals(owner);
    }

    public void releaseLock(String seatId,String showId, String lockToken) {

        String key = "seat-lock:" + seatId + "-showid:" + showId.toLowerCase();

        String currentToken = redisTemplate.opsForValue().get(key);

        if (lockToken.equals(currentToken)) {
            redisTemplate.delete(key);
        }
    }
}
