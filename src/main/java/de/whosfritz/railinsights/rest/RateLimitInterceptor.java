package de.whosfritz.railinsights.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * This class is an interceptor that is used to rate limit the number of requests per minute for a specific IP address.
 */
@Component
@Log4j2
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final long MAX_REQUESTS_PER_MINUTE = 60; // maximum number of requests per minute
    private static final long ONE_MINUTE_MILLIS = TimeUnit.MINUTES.toMillis(1); // 1 minute in milliseconds
    private final ConcurrentHashMap<Request, Long> requestCounts = new ConcurrentHashMap<>(); // ConcurrentHashMap is used to avoid concurrent modification exceptions

    /**
     * This method is called before the actual handler method is called. It returns true if the request should be
     *
     * @param request  the request object
     * @param response the response object
     * @param handler  the handler object
     * @return true if the request should be allowed, false otherwise
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIpAddress = getClientIpAddress(request);
        long currentTimestamp = System.currentTimeMillis();

        long requestCount = requestCounts.entrySet().stream()
                .filter(entry -> currentTimestamp - entry.getKey().getTimestamp() < ONE_MINUTE_MILLIS)
                .mapToLong(entry -> entry.getValue())
                .sum();
        log.info("Request count for {}: {}", clientIpAddress, requestCount);
        if (requestCount >= MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        log.info("Request not rate limited for {}", clientIpAddress + " because request count is " + requestCount);

        // build the request object
        Request requestObject = new Request();
        requestObject.setIpAdress(clientIpAddress);
        requestObject.setTimestamp(currentTimestamp);

        // check if the ip address is already in the map
        if (requestCounts.containsKey(requestObject)) {
            requestCounts.put(requestObject, requestCounts.get(requestObject) + 1);
        } else {
            requestCounts.put(requestObject, 1L);
        }

        cleanupExpiredRequests(currentTimestamp);
        return true;
    }

    /**
     * This method is called after the handler method is called. It is used to clean up the requestCounts map.
     *
     * @param request the request object
     * @return true
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        return (xForwardedForHeader != null) ? xForwardedForHeader.split(",")[0] : request.getRemoteAddr();
    }

    /**
     * This method is called after the handler method is called. It is used to clean up the requestCounts map.
     *
     * @param currentTimestamp the current timestamp
     */
    private void cleanupExpiredRequests(long currentTimestamp) {
        requestCounts.entrySet().removeIf(entry -> currentTimestamp - entry.getKey().getTimestamp() > ONE_MINUTE_MILLIS);
    }
}
