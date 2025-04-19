import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Config {
    public static void main(String[] args) {
            // URL kết nối Oracle (Cấu trúc: jdbc:oracle:thin:@[hostname]:[port]:[sid])
            String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
            String username = "sys";
            String password = "123456";

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                // Tải Oracle JDBC Driver
                Class.forName("oracle.jdbc.driver.OracleDriver");

                // Kết nối với cơ sở dữ liệu
                conn = DriverManager.getConnection(url, username, password);

                // Tạo Statement để thực thi câu lệnh SQL
                stmt = conn.createStatement();

                // Thực thi truy vấn SQL
                String query = "SELECT * FROM student";  // Thay đổi tên bảng cho phù hợp
                rs = stmt.executeQuery(query);

                // Xử lý kết quả truy vấn
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String fullName = rs.getString("full_name");
                    String phone = rs.getString("phone");
                    System.out.println("ID: " + id + ", Full Name: " + fullName + ", Phone: " + phone);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    }
}
