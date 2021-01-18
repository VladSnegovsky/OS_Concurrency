package benchmark;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Function;

import lampord.AdvancedLampordLock;
import lampord.LampordLock;
import spinlock.Spinlock;


/**
 * ProcessFunction is used to provide interface for all 
 * lock-type functions
 */
abstract class ProcessFunction {
    public abstract void run();
}

/**
 * OneLockBenchmark provides methods to run the benchmark
 */
class OneLockBenchmark {

    /**
     * Number of threads to be tested
     */
    static int THREAD_COUNT = 10;

    /**
     * Number of test iterations
     */
    static int MAX_ITERATIONS = 10;

    /**
     * Counter to manipulate by theads
     */
    static volatile Integer counter = 0;

    /**
     * Atomic counter to manipulate by theads
     */
    static AtomicInteger atomic_counter = new AtomicInteger(0);

    /**
     * Generates threads redy to execute measure function
     * @param r Measure function
     * @return threads list
     */
    private static Thread[] generateThreads(Runnable r) {
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(r);
        }

        return threads;
    }

    /**
     * Measures time to run measure function
     * @param f measure functtion
     * @return averate time to run function in THREAD_COUNT threads
     */
    public static synchronized Duration measureLock(ProcessFunction f) {
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT + 1);

        Runnable r = new Runnable(){
            @Override
            public void run() {
                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                f.run();

                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Duration timeElapsed = Duration.ZERO;

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {

            Thread[] threads = generateThreads(r);
            for (int i = 0; i < threads.length; i++) {
                threads[i].start();
            }

            Instant end = Instant.now();
            Instant start = Instant.now();
            
            try {
                barrier.await();
                barrier.await();

                end = Instant.now();


                for (int i = 0; i < threads.length; i++) {
                    threads[i].join();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            timeElapsed = timeElapsed.plus(Duration.between(start, end));
        }

        return timeElapsed.dividedBy(MAX_ITERATIONS);
    }
}

/**
 * LockBenhmark prints results of benchmark
 */
public class LockBenhmark {
    public static void measure() {

        System.out.printf("Monitor : %d%n", OneLockBenchmark.measureLock(new ProcessFunction() {
            @Override
            public void run() {
                synchronized(OneLockBenchmark.counter) {
                    OneLockBenchmark.counter++;
                }
            }
        }).toNanos());

        System.out.printf("Atomic : %d%n", OneLockBenchmark.measureLock(new ProcessFunction() {
            @Override
            public void run() {
                OneLockBenchmark.atomic_counter.incrementAndGet();
            }
        }).toNanos());

        ReentrantLock l1 = new ReentrantLock();
        System.out.printf("Reentrant lock : %d%n", OneLockBenchmark.measureLock(new ProcessFunction() {
            @Override
            public void run() {
                l1.lock();
                OneLockBenchmark.counter++;
                l1.unlock();
            }
        }).toNanos());

        Spinlock l2 = new Spinlock(OneLockBenchmark.THREAD_COUNT);
        System.out.printf("Spin lock : %d%n", OneLockBenchmark.measureLock(new ProcessFunction() {
            @Override
            public void run() {
                l2.lock();
                OneLockBenchmark.counter++;
                l2.unlock();
            }
        }).toNanos());

        // LampordLock l3 = new LampordLock(OneLockBenchmark.THREAD_COUNT);
        // System.out.printf("LampordLock lock : %d%n", OneLockBenchmark.measureLock(new ProcessFunction() {
        //     @Override
        //     public void run() {
        //         l3.lock();
        //         OneLockBenchmark.counter++;
        //         l3.unlock();
        //     }
        // }).toNanos());

        AdvancedLampordLock l4 = new AdvancedLampordLock(OneLockBenchmark.THREAD_COUNT);
        System.out.printf("AdvancedLampordLock lock : %d%n", OneLockBenchmark.measureLock(new ProcessFunction() {
            @Override
            public void run() {
                l4.lock();
                OneLockBenchmark.counter++;
                l4.unlock();
            }
        }).toNanos());
    }
}