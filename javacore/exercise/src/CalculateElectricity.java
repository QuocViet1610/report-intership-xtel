import java.util.Scanner;

public class CalculateElectricity {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số điện đã dùng trong tháng: ");
        int soDien = scanner.nextInt();
        int tienDien = 0;

        if (soDien <= 100) {
            tienDien = soDien * 1000;
        } else if (soDien <= 150) {
                tienDien = 100 * 1000 + (soDien - 100) * 1500;
        } else {
            tienDien = 100 * 1000 + 50 * 1500 + (soDien - 150) * 2000;
        }

        System.out.println("Tiền điện phải trả: " + tienDien + "đ");
        scanner.close();
    }
}
