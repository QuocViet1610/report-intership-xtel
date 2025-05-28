package join;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        ThreadOne thread1 = new ThreadOne();
        Thread thread2 = new Thread(new ThreadTwo(thread1));
        thread1.start();
        thread2.start();
    }

}
