package timer;

import java.util.Timer;
import java.util.TimerTask;

public class Time {
    static class MyTask extends TimerTask {
        private int count = 0;
        private Timer timer;

        public MyTask(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void run() {
            System.out.println("Hello, timer task running: " + count++);
            if (count > 5) {
                timer.cancel(); // Hủy timer khi chạy xong 5 lần
            }
        }
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        MyTask task = new MyTask(timer);

        // Lập lịch chạy task lần đầu sau 1 giây, sau đó chạy lại mỗi 2 giây
        timer.schedule(task, 1000, 2000);
    }
}
