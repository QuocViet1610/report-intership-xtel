class Counter {
    private int count = 0;

    public synchronized void increment() {
        System.out.println(count);
        count++;  // Đồng bộ để chỉ một thread truy cập tại một thời điểm
    }
    public int getCount() {
        return count;
    }
}

public class Synchornized {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                System.out.println("t1" );
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                System.out.println("t2");
                counter.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final count (with sync): " + counter.getCount());
    }

}
