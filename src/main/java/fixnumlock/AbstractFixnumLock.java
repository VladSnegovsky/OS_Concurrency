package fixnumlock;

import java.util.concurrent.locks.Condition;

/**
 * The AbstractFixnumLock class is an abstract class
 * for Lock types
 */
public abstract class AbstractFixnumLock implements FixnumLock {

    /**
     * Stores all registered threads
     */
    volatile protected Thread[] registered;


    /**
     * Initializes lock
     */
    public AbstractFixnumLock(int maxThreadNumber) {
        registered = new Thread[maxThreadNumber];
    }

    /**
     * Find and return thread index in queue
     * @return index of thread or -1 if thread is not registered
     */
    public int getIdByThread(Thread thread) {
        for (int i = 0; i < registered.length; i++) {
            if (registered[i] == thread) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find and return thread by index in queue
     * @return thread with index id or null othrewise
     */
    public Thread getThreadById(int id) {
        return registered[id];
    }

    /**
     * Adds thread to the queue
     * @return id of new added thread or -1 if thread is already in quque or max capacity reached
     */
    public int register(Thread thread) {
        int freePos = -1;
        for (int i = 0; i < registered.length; i++) {
            if ((freePos == -1) && (registered[i] == null)) {
                freePos = i;
            }
            if (registered[i] == thread) {
                return -1;
            }
        }
        if (freePos != -1) {
            registered[freePos] = thread;
            return freePos;
        }
        return -1;
    }

    /**
     * Removes thread from the queue
     */
    public void unregisterByThread(Thread thread) {
        for (int i = 0; i < registered.length; i++) {
            if (registered[i] == thread) {
                registered[i] = null;
                return;
            }
        }
    }

    /**
     * Removes thread from the queue specified by its id
     */
    public void unregisterById(int id) {
        registered[id] = null;
    }

    /**
     * Resets FixnumLock
     */
    public void reset() {
        registered = new Thread[registered.length];
    }

    public Condition newCondition() {
        return null;
    }

    public void lockInterruptibly() {
    }
}