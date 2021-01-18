package fixnumlock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

class FixnumLockMock extends AbstractFixnumLock {
    public FixnumLockMock(int maxThreads) {
        super(maxThreads);
    }

    public void lock() {
    }

    public Condition newCondition() {
        return null;
    }

    public void lockInterruptibly() {
    }

    public void unlock() {
    }

    public boolean tryLock(long t, TimeUnit tu) {
        return false;
    }

    public boolean tryLock() {
        return false;
    }

}

class FixnumLockTest {
    @Test
    void FixnumLockConstructorTest() {
        int maxThreads = 10;
        FixnumLock m = new FixnumLockMock(maxThreads);
        for (int i = 0; i < maxThreads; i++) {
            assertEquals(null, m.getThreadById(i));
        }

        Thread t1 = new Thread();
        Thread t2 = new Thread();
        Thread t3 = new Thread();

        assertEquals(0, m.register(t1));
        assertEquals(1, m.register(t2));
        assertEquals(2, m.register(t3));

        m.unregisterById(1);

        assertEquals(t1, m.getThreadById(0));
        assertEquals(null, m.getThreadById(1));
        assertEquals(t3, m.getThreadById(2));
        assertEquals(null, m.getThreadById(3));

        assertEquals(0, m.getIdByThread(t1));
        assertEquals(-1, m.getIdByThread(t2));

        assertEquals(1, m.register(t2));
    }
}