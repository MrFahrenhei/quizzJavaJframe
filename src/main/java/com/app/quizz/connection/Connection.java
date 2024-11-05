package com.app.quizz.connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    public static void connect(){
        String url = "jdbc:mysql://108.167.188.252:3306/rotinc02_osrdcc_rpg";
        String username = "rotinc02_beraldo";
        String password = "4IQ!zGY3yoTvBn04^1Dntynez";
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            System.out.println("Connected to the database!");
//        } catch (SQLException e) {
//            System.out.println("Failed to connect to the database.");
//            e.printStackTrace();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }
}
