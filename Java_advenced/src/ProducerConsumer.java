public class ProducerConsumer {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(5);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();
    }
}
