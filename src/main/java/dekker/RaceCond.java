package dekker;

import fixnumlock.FixnumLock;

class Counter {
    int counter = 0;
    public void increment() {
        counter++;
    }

    public void set(int c) {
        counter = c;
    }

    public int get() {
        return counter;
    }
}

//Manipulator without lock
//Threads won't wait each other
class Manipulator implements Runnable {
    volatile Counter counter;
    public Manipulator(Counter _counter) {
        counter = _counter;
    }

    public void run() {
        for (int i = 0; i < 10000; i++) {
            counter.increment();
//            System.out.println(counter.get());
        }
    }
}

//Manipulator with Lock
//Here threads will wait it's turn
class LockManipulator implements Runnable {
    volatile Counter counter;
    volatile FixnumLock l;

    public LockManipulator(Counter _counter, FixnumLock _l) {
        counter = _counter;
        l = _l;
    }

    public void run() {
        for (int i = 0; i < 10000; i++) {
            l.lock();                //lock critical section
            counter.increment();
            System.out.println(counter.get());
            l.unlock();              //unlock critical section
        }
    }
}

public class RaceCond {
    volatile Counter counter = new Counter();

    public void showRaceCondition() {
        System.out.println("Counter set to 0");
        Thread t1 = new Thread(new Manipulator(counter));
        Thread t2 = new Thread(new Manipulator(counter));
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception err) {
            err.printStackTrace();
        }

        System.out.println("Counter is: " + counter.get() + ", expected: 20,000");
        counter.set(0);
    }

    public void showNoRaceCondition(FixnumLock l) {
        System.out.println("Counter set to 0");
        Thread t1 = new Thread(new LockManipulator(counter, l));
        Thread t2 = new Thread(new LockManipulator(counter, l));
        //Register threads in FixNumLock for Lock
        //Thread will have its ID
        l.register(t1);
        l.register(t2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception err) {
            err.printStackTrace();
        }

        System.out.println("Counter is: " + counter.get() + ", expected: 20,000");
        counter.set(0);
    }
}
