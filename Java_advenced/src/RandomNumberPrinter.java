import java.util.Random;

public class RandomNumberPrinter {
    public static void main(String[] args) {
        int n = 1;  // Thời gian chờ giữa mỗi lần in (giây)
        int duration = 1;  // Thời gian chạy chương trình (phút)

        Random random = new Random();
        int totalTimeInMillis = duration * 60 * 1000;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < totalTimeInMillis) {
            // Sinh số nguyên ngẫu nhiên
            int randomNumber = random.nextInt(100);  // Số ngẫu nhiên trong khoảng 0 đến 99
            System.out.println("Số ngẫu nhiên: " + randomNumber);

            try {
                Thread.sleep(n * 1000);  // Dừng chương trình n giây
            } catch (InterruptedException e) {
                System.out.println("Lỗi khi dừng chương trình: " + e.getMessage());
            }
        }
    }}
