package ThreadPriority;

public class ThreadPriorityDemo {
    public static void main(String[] args) {
        // Tạo 3 thread với độ ưu tiên khác nhau
        Thread highPriorityThread = new Thread(new Task(), "High Priority Thread");
        Thread normalPriorityThread = new Thread(new Task(), "Normal Priority Thread");
        Thread lowPriorityThread = new Thread(new Task(), "Low Priority Thread");

        // Gán độ ưu tiên,  thread có priority thấp vẫn có thể chạy xen kẽ hoặc thậm chí trước thread priority cao
        highPriorityThread.setPriority(Thread.MAX_PRIORITY);    // 10
        normalPriorityThread.setPriority(Thread.NORM_PRIORITY); // 5
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY);     // 1

//        System.out.println("Priority of High Priority Thread: " + highPriorityThread.getPriority());
//        System.out.println("Priority of Normal Priority Thread: " + normalPriorityThread.getPriority());
//        System.out.println("Priority of Low Priority Thread: " + lowPriorityThread.getPriority());

        // Start các thread
        highPriorityThread.start();
        lowPriorityThread.start();
        normalPriorityThread.start();

    }

    static class Task implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + " - count: " + i); // gọi thread hiện tai dang chay
                try {
                    // Tạm dừng để dễ quan sát kết quả
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
