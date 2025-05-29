import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        // Tạo một BlockingQueue với dung lượng tối đa 10 phần tử
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        // Producer thread: tạo dữ liệu và đưa vào queue
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    queue.put(i);  // Thêm phần tử vào queue, sẽ bị chặn nếu queue đầy
                    System.out.println("Produced: " + i);
                    Thread.sleep(500);  // Giả lập thời gian tạo dữ liệu
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Consumer thread: lấy dữ liệu từ queue và xử lý
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    int item = queue.take();  // Lấy phần tử từ queue, sẽ bị chặn nếu queue trống
                    System.out.println("Consumed: " + item);
                    Thread.sleep(1000);  // Giả lập thời gian xử lý dữ liệu
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Khởi động cả hai thread
        producer.start();
        consumer.start();

        // Đợi cho đến khi cả hai thread hoàn thành
        producer.join();
        consumer.join();
    }
}
