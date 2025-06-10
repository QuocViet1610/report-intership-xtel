package MessageApp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clientMap = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            new ClientHandler(socket).start();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Yêu cầu client gửi username
                out.println("ENTER_USERNAME");
                username = in.readLine();

                // Kiểm tra trùng username
                synchronized (clientMap) {
                    if (clientMap.containsKey(username)) {
                        out.println("USERNAME_EXISTS");
                        socket.close();
                        return;
                    } else {
                        clientMap.put(username, this);
                        out.println("WELCOME " + username);
                        System.out.println(username + " connected");
                    }
                }

                String line;
                while ((line = in.readLine()) != null) {
                    int idx = line.indexOf(":");
                    if (idx != -1) {
                        String recipient = line.substring(0, idx);
                        String msg = line.substring(idx + 1);

                        ClientHandler recipientHandler = clientMap.get(recipient);
                        if (recipientHandler != null) {
                            recipientHandler.sendMessage(username + ": " + msg);
                        } else {
                            out.println("User " + recipient + " not found");
                        }
                    } else {
                        out.println("Invalid message format. Use recipient:message");
                    }
                }
            } catch (IOException e) {
                System.out.println(username + " disconnected");
            } finally {
                try {
                    if (username != null) {
                        clientMap.remove(username);
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
