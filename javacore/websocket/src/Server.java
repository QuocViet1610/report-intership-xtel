import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket ss = null;
        Socket soc = null;
        try {
            // Tao server socket tren port 8080
            ss = new ServerSocket(8080);
            System.out.println("Server is waiting for a connection...");

            // Chap nhan ket noi tu client
            soc = ss.accept();
            System.out.println("Connection established");

            // nhan du lieu tu client
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String str = in.readLine();  // Read the input from client
            System.out.println("Received from client: " + str);

            // Gui phan hoi den client
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            out.println("Server received: " + str);  // Send a response to client

            soc.close();  // Close the connection
            ss.close();   // Close the server socket
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}