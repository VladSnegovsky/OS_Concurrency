package dekker;

import fixnumlock.AbstractFixnumLock;
import java.util.concurrent.TimeUnit;

public class DekkerLock extends AbstractFixnumLock {
    volatile boolean[] ready;  //says who wants to enter
    volatile int turn = 0;     //stores the id of the priority thread

    public DekkerLock() {
        super(2);  //2 threads
        ready = new boolean[2];
    }

    public void lock() {
        int id = getIdByThread(Thread.currentThread());
        int other = 1 - id;
        ready[id] = true;
        turn = id;
        while (turn == id && ready[other]) { }
        //If the second thread has overwritten the id, but the
        //first one wants it too, then the second one skips it
    }

    public void unlock() {
        int id = getIdByThread(Thread.currentThread());
        ready[id] = false;
    }

    public boolean tryLock() {
        int id = getIdByThread(Thread.currentThread());
        int other = 1 - id;  //if curr ID is 1, then other will be 0, 0 - 1
        ready[id] = true;
        turn = id;
        return !(turn == id && ready[other]);
    }

    public boolean tryLock(long l, TimeUnit u) {
        return false;
    }
}
