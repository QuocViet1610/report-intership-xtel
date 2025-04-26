import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class RandomNumberWriter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        FileWriter fileWriter = null;

        try {
            // Mở file để ghi vào, sử dụng true để ghi thêm vào cuối file
            fileWriter = new FileWriter("src//output.txt", true);
            System.out.println("Chương trình đang chạy. Gõ 'stop' để dừng.");

            // Vòng lặp vô hạn để ghi số liên tục
            while (true) {
                // Sinh số nguyên ngẫu nhiên từ 0 đến 99
                int randomNumber = random.nextInt(100);
                fileWriter.write(randomNumber + "\n");

                System.out.println("Đã ghi: " + randomNumber);

                 fileWriter.flush();

                System.out.print("Gõ 'stop' để dừng: ");
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("stop")) {
                    break; // Dừng vòng lặp khi người dùng gõ "stop"
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi vào file: " + e.getMessage());
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close(); // Đóng file sau khi hoàn tất
                }
            } catch (IOException e) {
                System.out.println("Lỗi khi đóng file: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
