import java.util.Scanner;

public class InputNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int correctNumber = 123;  // Số đúng để nhập
        int maxAttempts = 5;
        boolean success = false;

        for (int i = 1; i <= maxAttempts; i++) {
            System.out.print("Nhập số (lần " + i + "): ");
            int userInput = scanner.nextInt();

            if (userInput == correctNumber) {
                System.out.println("Thành công! Bạn đã nhập đúng.");
                success = true;
                break;
            } else {
                System.out.println("Nhập sai. Cố gắng lại.");
            }
        }

        if (!success) {
            System.out.println("Nhập lỗi quá số lần cho phép.");
        }

        scanner.close();
    }
}
