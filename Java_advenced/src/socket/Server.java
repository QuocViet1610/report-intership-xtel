package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang chạy, chờ kết nối...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Kết nối từ client đã được chấp nhận.");

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Nhận được từ client: " + message);
            }
        } catch (IOException e) {
            System.out.println("Lỗi server: " + e.getMessage());
        }
    }
}
