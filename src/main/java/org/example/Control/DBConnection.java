package org.example.Control;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/quanlytaisan"
                + "?useSSL=false&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC";

        String user = "root";
        String password = "123456";

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System.out.println("Kết nối thành công: " + con.getCatalog());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
