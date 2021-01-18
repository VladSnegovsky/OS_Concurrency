package lampord;

import fixnumlock.AbstractFixnumLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LampordLock extends AbstractFixnumLock {
    volatile int[] tickets;              //all tickets
    volatile AtomicBoolean[] choose;     //current thread in section
    volatile AtomicInteger last = new AtomicInteger(0);  //last ticket

    public LampordLock(int tCount) {
        super(tCount);
        tickets = new int[tCount];
        choose = new AtomicBoolean[tCount];
        for (int i = 0; i < choose.length; i++) {
            choose[i] = new AtomicBoolean(false);
        }
    }

    public void lock() {
        int id = getIdByThread(Thread.currentThread());
        
        choose[id].set(true);
        tickets[id] = findMax() + 1;
        choose[id].set(false);

        for (int i = 0; i < tickets.length; i++) {
            if (i == id) { continue; }
            while (choose[i].get()) { }  //wait till other do something

            while ((tickets[i] != 0) && ((tickets[i] < tickets[id]) || ((tickets[i] == tickets[id]) && (i < id)))) {
            //wait until someone has a smaller ticket or has the same ticket but arrived earlier
            }
        }
    }

    public void unlock() {
        int id = getIdByThread(Thread.currentThread());
        // System.out.println("unlock " + id);
        tickets[id] = 0;
    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long l, TimeUnit u) {
        return false;
    }

    private int findMax() {
        int max = tickets[0];

		for (int i = 1; i < tickets.length; i++) {
			if (tickets[i] > max) {
                max = tickets[i];
            }
		}
		return max;
    }
}
