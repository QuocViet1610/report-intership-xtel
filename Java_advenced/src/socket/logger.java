package socket;

import java.io.FileWriter;
import java.io.IOException;

public class logger {
    public static void logError(String errorMessage) {
        try (FileWriter fileWriter = new FileWriter("socket//error_log.txt", true)) {
            fileWriter.write(errorMessage + "\n");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi vào log: " + e.getMessage());
        }
    }
}
