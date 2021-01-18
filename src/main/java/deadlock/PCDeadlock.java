package deadlock;

import java.util.LinkedList;

public class PCDeadlock {
    public static void main(String[] args)
            throws InterruptedException
    {
        final PC pc = new PC();

        Thread producerThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.produce();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.consume();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();
    }

    public static class PC {
        static private boolean isFirstStep = true;

        LinkedList<Integer> list = new LinkedList<>();
        final static int capacity = 5;

        public void produce() throws InterruptedException
        {
            int value = 0;
            Thread.sleep(1000);
            while (true) {
                synchronized (this)
                {
                    if (list.size() >= capacity)
                        wait();

                    System.out.println("Producer produced-" + value);

                    list.add(value++);

                    if(isFirstStep) {
                        System.out.println("The producer creates an item, puts it into the buffer, and increases itemCount.");
                        isFirstStep = false;
                    }

                    if(list.size() == 1) {
                        System.out.println("Because the buffer was empty prior to the last addition, the producer tries to wake up the consumer.");
                        notify();
                    }

                    Thread.sleep(1000);
                }
            }
        }

        public void consume() throws InterruptedException
        {
            while (true) {
                synchronized (this)
                {
                    if (list.size() == 0) {
                        if(isFirstStep) {
                            System.out.println("Just before calling wait, the consumer is interrupted and the producer is resumed.");

                            wait();
                            System.out.println("Unfortunately, the consumer wasn't yet sleeping, and the wakeup call is lost. When the consumer resumes, it goes to sleep and will never be awakened again. This is because the consumer is only awakened by the producer when itemCount is equal to 1.");
                            System.out.println("The producer will loop until the buffer is full, after which it will also go to sleep.");
                        }
                        wait();
                    }

                    int val = list.removeFirst();

                    System.out.println("Consumer consumed-" + val);

                    if(list.size() == capacity - 1)
                        notify();

                    Thread.sleep(1000);
                }
            }
        }
    }
}

