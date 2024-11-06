package com.app.quizz.connection;

import com.app.quizz.config.DotEnv;

import java.sql.*;
import java.util.ArrayList;

public class Connection {
    private static final DotEnv dotenv = new DotEnv();
    private static final String DB_HOST = dotenv.get("DB_HOST");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public static boolean insertQuestion(String question, String category, String[] answers, int correctIndex){
        try{
           Connection connection = DriverManager.getConnection(
                   DB_HOST, DB_USER, DB_PASSWORD
           );
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
