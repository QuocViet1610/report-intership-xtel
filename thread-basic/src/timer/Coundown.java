package timer;

import java.util.Timer;
import java.util.TimerTask;

public class Coundown {

    static class Timetask extends TimerTask {
        Timer timer;
        int count = 10;

        public Timetask(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void run() {

            System.out.println(count);
            count --;
            if (count <= 0 ){
                timer.cancel();
            }
        }
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        Timetask task = new Timetask(timer);

        // Lập lịch chạy task lần đầu sau 1 giây, sau đó chạy lại mỗi 2 giây
        timer.schedule(task, 1000, 1000);
    }

}
