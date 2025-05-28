package join;

public class ThreadOne extends Thread{
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("t1");
        }
    }
}
