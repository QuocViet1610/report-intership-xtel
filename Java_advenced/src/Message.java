import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Message {
    String content;

    Message(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return content;
    }
}

class MessageQueue {

    private BlockingQueue<Message> queue;

    MessageQueue(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    // Thêm message vào queue
    public void put(Message msg) throws InterruptedException {
        queue.put(msg);
    }

    // Lấy message từ queue
    public Message take() throws InterruptedException {
        return queue.take();
    }

}

class Producer extends Thread {
    private MessageQueue queue;

    Producer(MessageQueue queue) {
        this.queue = queue;
    }

    public void run() {
        Random random = new Random();


        while (true){
            try {
                int randomInt = random.nextInt(100);
                Message message = new Message("message "+ randomInt);
                queue.put(message);
//                Thread.sleep(1000);
                System.out.println("producer: "+ message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

class Consumer extends Thread {
    private MessageQueue queue;

    Consumer(MessageQueue queue) {
        this.queue = queue;
    }

    public void run() {

        while (true){
            try {
                Message message = queue.take();
                System.out.println("Consumed: " + message.content);
//                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

