package com.app.quizz.connection;

import com.app.quizz.config.DotEnv;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    public static void connect(){
        DotEnv dotenv = new DotEnv(".env");
        String url = dotenv.get("DB_HOST");
        String username = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
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
