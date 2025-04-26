import java.util.LinkedList;
import java.util.Queue;

public class Message {
    String content;

    Message(String content) {
        this.content = content;
    }
}

class MessageQueue {
    private Queue<Message> queue = new LinkedList<>();
    private int capacity;

    MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produce(Message message) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();  // Chờ nếu queue đầy
        }
        queue.add(message);
        System.out.println("Producer đã gửi: " + message.content);
        notify();  // Thông báo consumer rằng có message mới
    }

    public synchronized Message consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();  // Chờ nếu queue rỗng
        }
        Message message = queue.poll();
        System.out.println("Consumer đã nhận: " + message.content);
        notify();  // Thông báo producer có chỗ trống trong queue
        return message;
    }
}

class Producer extends Thread {
    private MessageQueue queue;

    Producer(MessageQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                String messageContent = "Message " + System.currentTimeMillis();
                Message message = new Message(messageContent);
                queue.produce(message);
                Thread.sleep(1000);  // Tạo message mới sau mỗi 1 giây
            }
        } catch (InterruptedException e) {
            System.out.println("Producer bị gián đoạn.");
        }
    }
}

class Consumer extends Thread {
    private MessageQueue queue;

    Consumer(MessageQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                queue.consume();
                Thread.sleep(1500);  // Tiêu thụ message sau mỗi 1.5 giây
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer bị gián đoạn.");
        }
    }
}

