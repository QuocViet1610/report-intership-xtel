package join;

public class ThreadTwo implements Runnable {

    ThreadOne threadOne;

    public ThreadTwo(ThreadOne threadOne){
        this.threadOne = threadOne;
    }

    @Override
    public void run() {
        try {
            threadOne.join();
            System.out.println("Thread 2 running");
            for (int i = 0; i < 10; i++) {
                System.out.println("t2" );
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
