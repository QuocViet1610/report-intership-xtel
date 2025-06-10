package MessageApp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Đọc yêu cầu username
            String serverMsg = in.readLine();
            if ("ENTER_USERNAME".equals(serverMsg)) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                out.println(username);
            }

            // Nhận phản hồi từ server
            String response = in.readLine();
            if ("USERNAME_EXISTS".equals(response)) {
                System.out.println("Username already taken. Exiting.");
                return;
            } else if (response.startsWith("WELCOME")) {
                System.out.println(response);
            }

            // Thread nhận tin nhắn realtime
            Thread receiveThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("\n[Message] " + msg);
                        System.out.print("recipient:message > ");
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            receiveThread.setDaemon(true);
            receiveThread.start();

            // Thread gửi tin nhắn
            System.out.print("recipient:message > ");
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) continue;
                out.println(input);
                System.out.print("recipient:message > ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
