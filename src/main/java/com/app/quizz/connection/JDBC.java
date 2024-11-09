package com.app.quizz.connection;

import com.app.quizz.config.DotEnv;
import com.app.quizz.models.Category;
import com.app.quizz.models.Question;

import java.sql.*;
import java.util.Objects;

public class JDBC {
    private static final DotEnv dotenv = new DotEnv();
    private static final String DB_HOST = dotenv.get("DB_HOST");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public static boolean saveQuestionCategoryAndAnswers(String question, String category, String[] answers, int correctIndex){
        try{
           Connection connection = DriverManager.getConnection(
                   DB_HOST, DB_USER, DB_PASSWORD
           );

            Category categoryObj = getCategory(category);
            if(categoryObj == null){
                categoryObj = insertCategory(category);
            }

            Question questionObj = insertQuestion(categoryObj, question);

            return insertAnswer(questionObj, answers, correctIndex);

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    private static Question insertQuestion(Category category, String question){
        try{
            Connection connection = DriverManager.getConnection(
                    DB_HOST, DB_USER, DB_PASSWORD
            );
            PreparedStatement insertQuestionQuery = connection.prepareStatement(
                    "INSERT INTO question(category_id, question_text, dt_insert) VALUES (?, ?, now())",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertQuestionQuery.setInt(1, category.getCategoryId());
            insertQuestionQuery.setString(2, question);
            insertQuestionQuery.executeUpdate();

            ResultSet generatedKeys = insertQuestionQuery.getGeneratedKeys();
            if(generatedKeys.next()){
               int questionId = generatedKeys.getInt(1);
               return new Question(questionId, category.getCategoryId(), question);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    private static Category getCategory(String category){
        try{
            Connection connection = DriverManager.getConnection(
                    DB_HOST, DB_USER, DB_PASSWORD
            );
            PreparedStatement getCategoryQuery = connection.prepareStatement("SELECT * FROM category WHERE category_name = ?");
            getCategoryQuery.setString(1, category);
            ResultSet resultSet = getCategoryQuery.executeQuery();
            if(resultSet.next()){
                int categoryId = resultSet.getInt("category_id");
                return new Category(categoryId, category);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    private static Category insertCategory(String category){
        try{
            Connection connection = DriverManager.getConnection(
                    DB_HOST, DB_USER, DB_PASSWORD
            );
            PreparedStatement insertCategoryQuery = connection.prepareStatement(
                    "INSERT INTO category(category_name, dt_insert) VALUES (?, now())",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertCategoryQuery.setString(1, category);
            insertCategoryQuery.executeUpdate();

            ResultSet generatedKeys = insertCategoryQuery.getGeneratedKeys();
            if(generatedKeys.next()){
                int categoryId = generatedKeys.getInt(1);
                return new Category(categoryId, category);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    private static boolean insertAnswer(Question question, String[] answers, int correctIndex){
        try{
            Connection connection = DriverManager.getConnection(
                    DB_HOST, DB_USER, DB_PASSWORD
            );
            PreparedStatement insertAnswerQuery = connection.prepareStatement(
                    "INSERT INTO answer(question_id, answer_text, is_correct, dt_insert) " +
                            "VALUES (?, ?, ?, now())",
                    Statement.RETURN_GENERATED_KEYS
            );

            insertAnswerQuery.setInt(1, question.getQuestionId());
            for(int i = 0; i < answers.length; i++){
                insertAnswerQuery.setString(2, answers[i]);

                if(i==correctIndex){
                    insertAnswerQuery.setBoolean(3, true);
                }else{
                    insertAnswerQuery.setBoolean(3, false);
                }
               insertAnswerQuery.executeUpdate();
            }
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
