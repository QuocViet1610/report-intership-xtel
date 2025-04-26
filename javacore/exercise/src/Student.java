import java.util.ArrayList;
import java.util.Scanner;

public class Student {
    String name;
    String gender;
    String hometown;
    int age;

    Student(String name, String gender, String hometown, int age) {
        this.name = name;
        this.gender = gender;
        this.hometown = hometown;
        this.age = age;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Tạo ArrayList để lưu thông tin sinh viên
        ArrayList<Student> studentList = new ArrayList<>();

        while (true) {
            System.out.println("Nhập thông tin sinh viên (hoặc nhập 'stop' để kết thúc): ");

            System.out.print("Tên: ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("stop")) {
                break;
            }

            System.out.print("Giới tính: ");
            String gender = scanner.nextLine();

            System.out.print("Quê quán: ");
            String hometown = scanner.nextLine();

            System.out.print("Tuổi: ");
            int age = scanner.nextInt();
            scanner.nextLine();  // Đọc dòng trống còn lại sau khi nhập tuổi

            // Lưu thông tin sinh viên vào ArrayList
            studentList.add(new Student(name, gender, hometown, age));

            System.out.println("Thông tin sinh viên đã được lưu!\n");

            System.out.println("\nDanh sách sinh viên đã nhập:");
            for (int i = 0; i < studentList.size(); i++) {
                Student student = studentList.get(i);
                System.out.println("id:" + (i + 1) + ": ");
                System.out.println("Tên: " + student.name);
                System.out.println("Giới tính: " + student.gender);
                System.out.println("Quê quán: " + student.hometown);
                System.out.println("Tuổi: " + student.age);
                System.out.println();
            }
        }

        // Hiển thị thông tin tất cả sinh viên đã nhập

        scanner.close();
    }

}

