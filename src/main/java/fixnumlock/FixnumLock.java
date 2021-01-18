package fixnumlock;

import java.util.concurrent.locks.Lock;

/**
 * The FixnumLock interface declares requirements for 
 * custom lock types
 */
public interface FixnumLock extends Lock {

    /**
     * Find and return thread index in queue
     * @return index of thread or -1 if thread is not registered
     */
    int getIdByThread(Thread thread);

    /**
     * Find and return thread by index in queue
     * @return thread with index id or null othrewise
     */
    Thread getThreadById(int id);

    /**
     * Adds thread to the queue
     * @return id of new added thread or -1 if thread is already in quque
     */
    int register(Thread thread);

    /**
     * Removes thread from the queue
     */
    void unregisterByThread(Thread thread);

    /**
     * Removes thread from the queue specified by its id
     */
    void unregisterById(int id);

    /**
     * Resets FixnumLock
     */
    void reset();
}