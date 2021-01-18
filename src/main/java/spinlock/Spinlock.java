package spinlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import fixnumlock.AbstractFixnumLock;
import java.util.concurrent.locks.Condition;


public class Spinlock extends AbstractFixnumLock {
    protected AtomicReference<Thread> owner;

    public Spinlock(int threadsCount) {
        super(threadsCount);
        owner = new AtomicReference<Thread>();
    }
 
    public void lock() { 
        Thread thread = Thread.currentThread(); 
        while (!owner.compareAndSet(null, thread)) { 
        } 
    } 
 
    public void unlock() { 
        Thread thread = Thread.currentThread();
        owner.compareAndSet(thread, null); 
    } 

    public boolean tryLock() {
        Thread thread = Thread.currentThread();
        return owner.compareAndSet(null, thread);
    }

    public void lockInterruptibly() {
        Thread thread = Thread.currentThread();
        while (!owner.compareAndSet(null, thread)) { 
            if (thread.isInterrupted()) {
                return;
            }
        } 
    }

    public boolean tryLock(long time, TimeUnit unit) {
        Thread thread = Thread.currentThread(); 
        long maxDuration = unit.toMillis(time);
        long beginTimeStamp = System.currentTimeMillis();

        while (!owner.compareAndSet(null, thread)) {
            long endTimeStamp = System.currentTimeMillis();
            if (endTimeStamp - beginTimeStamp >= maxDuration) {
                return false;
            }
        }
        return true;
    }

    public Condition newCondition() {
        return null;
    }
}
