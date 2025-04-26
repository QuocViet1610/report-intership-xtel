import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            // ket noi toi serve
            Socket socket = new Socket("localhost", 8080);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter a string: ");
            String str = userInput.readLine();

            // gui den server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(str);
            // nhan phai hoi tu server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("Server response: " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
