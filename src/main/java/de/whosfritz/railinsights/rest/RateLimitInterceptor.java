package de.whosfritz.railinsights.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final long MAX_REQUESTS_PER_MINUTE = 4;
    private static final long ONE_MINUTE_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private ConcurrentHashMap<String, Long> requestCounts = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIpAddress = getClientIpAddress(request);
        long currentTimestamp = System.currentTimeMillis();

        long requestCount = requestCounts.getOrDefault(clientIpAddress, 0L);
        log.info("Request count for {}: {}", clientIpAddress, requestCount);
        if (requestCount >= MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        log.info("Request not rate limited for {}", clientIpAddress + " because request count is " + requestCount);

        requestCounts.put(clientIpAddress, requestCount + 1);
        cleanupExpiredRequests(currentTimestamp);
        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        return (xForwardedForHeader != null) ? xForwardedForHeader.split(",")[0] : request.getRemoteAddr();
    }

    private void cleanupExpiredRequests(long currentTimestamp) {
        requestCounts.entrySet().removeIf(entry -> currentTimestamp - entry.getValue() > ONE_MINUTE_MILLIS);
    }
}
