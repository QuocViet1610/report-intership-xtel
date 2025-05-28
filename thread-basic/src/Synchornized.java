class Counter {
    private static int count = 0;

    public void increment() {
        synchronized (this){
            for (int i = 0; i < 10000; i++) {
                System.out.println(count);
                count++;  // Đồng bộ để chỉ một thread truy cập tại một thời điểm
            }
        }
}

    public int getCount() {
        return count;
    }
}

public class Synchornized {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        System.out.println("thread main");

        Thread t1 = new Thread(() -> { // lamda phuong thuc run trong thread
                counter.increment();
        });

        Thread t2 = new Thread(() -> { // thread ke thua tu runable
                counter.increment();
        });

        t1.start(); // thread main and t1
        t2.start(); // thread main, t1, t2

        t1.join();// join t1 vao main
        System.out.println("Doi t1 chay xong");
        t2.join();
        System.out.println("Doi t2 chay xong");
        System.out.println("Final count (with sync): " + counter.getCount());
    }

}
