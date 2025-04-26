package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        String serverIp = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverIp, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            Random random = new Random();
            while (true) {
                String message = "Random Message: " + random.nextInt(1000);
                out.println(message);
                System.out.println("Đã gửi: " + message);
                Thread.sleep(2000);  // Gửi sau mỗi 2 giây
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Lỗi client: " + e.getMessage());
        }
    }
}
