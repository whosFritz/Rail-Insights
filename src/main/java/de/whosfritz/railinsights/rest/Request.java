package de.whosfritz.railinsights.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a request object that is used to keep track of the number of requests per minute for a specific
 */
@Getter
@Setter
public class Request {

    String ipAdress;

    long timestamp;

    // equals and hashCode are necessary for the ConcurrentHashMap in RateLimitInterceptor
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return ipAdress.equals(request.ipAdress);
    }

    // equals and hashCode are necessary for the ConcurrentHashMap in RateLimitInterceptor
    public int hashCode() {
        return ipAdress.hashCode();
    }

}
