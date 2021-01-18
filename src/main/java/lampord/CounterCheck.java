package lampord;

import fixnumlock.FixnumLock;

class Counter {
    int counter = 0;
    public void increment() {
        counter++;
    }

    public void decrement() {
        counter--;
    }

    public void set(int c) {
        counter = c;
    }

    public int get() {
        return counter;
    }
}

class LockManipulator implements Runnable {
    volatile Counter counter;
    volatile FixnumLock l;
    boolean operation;

    public LockManipulator(Counter _counter, FixnumLock _l, boolean _operation) {
        counter = _counter;
        l = _l;
        operation = _operation;  // if true - ++, else --
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            l.lock();   //lock section
            if (operation) {  //operation
                counter.increment();
            } else {
                counter.decrement();
            }
            System.out.println(counter.get());
            l.unlock();  //unlock section
        }
    }
}

public class CounterCheck {
    public void showManipulation(FixnumLock l, int n) {
        System.out.println("Counter set to 0");
        Counter counter = new Counter();

        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            //creating thread with operation
            //i%2 = 0 or 1, if 1 - true - increment, if 0 - false - decrement
            threads[i] = new Thread(new LockManipulator(counter, l, i % 2 == 0));
            l.register(threads[i]);  //register all threads for lock
        }

        for (int i = 0; i < n; i++) {
            threads[i].start();
        }

        try {
            for (int i = 0; i < n; i++) {
                threads[i].join();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        System.out.println("Counter is: " + counter.get() + ", expected: 0");
    }
}
