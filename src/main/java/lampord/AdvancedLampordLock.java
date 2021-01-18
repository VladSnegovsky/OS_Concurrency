package lampord;

import fixnumlock.AbstractFixnumLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AdvancedLampordLock extends AbstractFixnumLock {
    volatile AtomicInteger currentTicket;
    volatile AtomicInteger lastTicket;
    int MAX_TICKET;

    public AdvancedLampordLock(int tCount) {
        super(tCount);
        currentTicket = new AtomicInteger(0);
        lastTicket = new AtomicInteger(0);
        MAX_TICKET = tCount + 2;
    }

    public void lock() {
        int threadTicket;
        do {
            // currentTicket.compareAndSet(MAX_TICKET + 1, 0);
            threadTicket = currentTicket.get();
            currentTicket.compareAndSet(MAX_TICKET + 1, 0);
        } while (!currentTicket.compareAndSet(threadTicket, threadTicket + 1));

        while (!lastTicket.compareAndSet(threadTicket, threadTicket)) {
        }
    }

    public void unlock() {
        if (!lastTicket.compareAndSet(MAX_TICKET, 0)) {
            lastTicket.incrementAndGet();
        }
    }

    public boolean tryLock() {
        int threadTicket = currentTicket.incrementAndGet();
        return lastTicket.compareAndSet(threadTicket, threadTicket);
    }

    public boolean tryLock(long l, TimeUnit u) {
        return false;
    }
}
