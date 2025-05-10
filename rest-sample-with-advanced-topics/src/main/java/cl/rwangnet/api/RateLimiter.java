package cl.rwangnet.api;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Allows to set a treshold on requests processing limits.
 * @author rwangnet <rwangnet@gmail.com>
 */
public class RateLimiter {
    private final int maxRequestsPerSecond;
    private final Semaphore semaphore;
    private final AtomicLong lastReset = new AtomicLong(System.currentTimeMillis());

    public RateLimiter(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
        this.semaphore = new Semaphore(maxRequestsPerSecond);
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        if (now - lastReset.get() >= 1000) {
            semaphore.drainPermits();
            semaphore.release(maxRequestsPerSecond);
            lastReset.set(now);
        }
        return semaphore.tryAcquire();
    }
}