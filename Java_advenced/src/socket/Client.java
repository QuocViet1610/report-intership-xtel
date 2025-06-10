package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        String serverIp = "localhost"; // địa chỉ server
        int port = 12345; // cổng server mở kết nối

        try (

                Socket socket = new Socket(serverIp, port); // Tạo kết nối TCP đến server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            Random random = new Random();
            while (true) {
                String message = "Random thread.Message: " + random.nextInt(1000);
                out.println(message); // Gửi message tới server
                System.out.println("Đã gửi: " + message);
                Thread.sleep(2000);  // Gửi sau mỗi 2 giây
            }
        } catch (IOException | InterruptedException e) {




            System.out.println("Lỗi client: " + e.getMessage());
        }
    }
}
