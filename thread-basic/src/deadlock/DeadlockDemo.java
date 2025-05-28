package deadlock;

public class DeadlockDemo {

    // Hai tài nguyên dùng làm khóa (lock)
    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public static void main(String[] args) {

        // Thread 1: khóa resource1 trước, sau đó khóa resource2
        Thread t1 = new Thread(() -> {
            System.out.println("Thread 1: Bắt đầu, cố gắng lock resource1");
            synchronized (resource1) {
                System.out.println("Thread 1: Đã lock resource1");

                // Tạm dừng để Thread 2 có thể chạy và khóa resource2
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Thread 1: Cố gắng lock resource2");
                synchronized (resource2) { // do dang cho thread 2 nen bi deadlock
                    System.out.println("Thread 1: Lúc này Thread 1 đang giữ đồng thời cả hai khóa resource1 và resource2."); //
                }
            }
            System.out.println("Thread 1: Hoàn thành");
        });

        // Thread 2: khóa resource2 trước, sau đó khóa resource1
        Thread t2 = new Thread(() -> {
            System.out.println("Thread 2: Bắt đầu, cố gắng lock resource2");
            synchronized (resource2) {  // chuyen thanh resource1 de tranh deadlock
                System.out.println("Thread 2: Đã lock resource2");

                // Tạm dừng để Thread 1 có thể chạy và khóa resource1
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Thread 2: Cố gắng lock resource1");
                synchronized (resource1) {
                    System.out.println("Thread 2: Đã lock resource1");
                }
            }
            System.out.println("Thread 2: Hoàn thành");
        });

        // Bắt đầu cả 2 thread
        t1.start();
        t2.start();

        try {
            // Đợi 5 giây để quan sát deadlock
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Kết thúc chương trình, nếu không có dòng 'Hoàn thành' nghĩa là deadlock đã xảy ra.");
    }
}
