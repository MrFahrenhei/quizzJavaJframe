package com.app.quizz.connection;

import com.app.quizz.config.DotEnv;
import com.app.quizz.models.Answer;
import com.app.quizz.models.Category;
import com.app.quizz.models.Question;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class JDBC {
    private static final DotEnv dotenv = new DotEnv();
    private static final String DB_HOST = dotenv.get("DB_HOST");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");
    private static Connection connection;

    public static boolean saveQuestionCategoryAndAnswers(String question, String category, String[] answers, int correctIndex){
        try{
            connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);

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
    public static ArrayList<String> getCategories(){
        ArrayList<String> categoryList = new ArrayList<>();
        try{
            connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            Statement getCategoriesQuery = connection.createStatement();
            ResultSet resultSet = getCategoriesQuery.executeQuery("SELECT * FROM category");

            while(resultSet.next()){
                String categoryName = resultSet.getString("category_name");
                categoryList.add(categoryName);
            }
            return categoryList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<Question> getQuestions(Category category){
        ArrayList<Question> questions = new ArrayList<>();
        try{
            connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            PreparedStatement getQuestionsQuery = connection.prepareStatement(
                    "SELECT * FROM question join category " +
                            "ON question.category_id = category.category_id " +
                            "WHERE category.category_id = ? order by RAND()"
            );
            getQuestionsQuery.setInt(1, category.getCategoryId());
            ResultSet resultSet = getQuestionsQuery.executeQuery();
            while(resultSet.next()){
                int questionId = resultSet.getInt("question_id");
                int categoryId = resultSet.getInt("category_id");
                String question = resultSet.getString("question_text");
                questions.add(new Question(questionId, categoryId, question));
            }
            return questions;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<Answer> getAnswers(Question question){
        ArrayList<Answer> answers = new ArrayList<>();
        try{
            connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            PreparedStatement getAnswersQuery = connection.prepareStatement(
                    "SELECT * FROM question JOIN answer " +
                            "ON question.question_id = answer.question_id " +
                            " WHERE question.question_id = ? ORDER BY rand()"
            );
            getAnswersQuery.setInt(1, question.getQuestionId());
            ResultSet resultSet = getAnswersQuery.executeQuery();
            while(resultSet.next()){
                int answerId = resultSet.getInt("answer_id");
                String answerText = resultSet.getString("answer_text");
                boolean isCorrect = resultSet.getBoolean("is_correct");
                Answer answer = new Answer(answerId, question.getQuestionId(), answerText, isCorrect);
                answers.add(answer);
            }
            return answers;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static boolean insertAnswer(Question question, String[] answers, int correctIndex){
        try{
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
