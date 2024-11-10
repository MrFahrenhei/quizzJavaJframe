package com.app.quizz.screens;

import com.app.quizz.connection.JDBC;
import com.app.quizz.design.Colors;
import com.app.quizz.models.Answer;
import com.app.quizz.models.Category;
import com.app.quizz.models.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizScreen extends JFrame implements ActionListener {
    private JLabel scoreLabel;
    private JTextArea questionTextArea;
    private Category category;
    private JButton[] answerButtons;
    private JButton nextButton;

    private ArrayList<Question> questions;
    private Question currentQuestion;
    private int currentQuestionIndex;
    private int numberOfQuestions;
    private int score;
    private boolean firstChoiseMade;

    public QuizScreen(Category category, int numberOfQuestions) {
        super("Quiz Game");
        setSize(408, 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Colors.LIGHT_BLUE);

        answerButtons = new JButton[4];
        this.category = category;

        questions = JDBC.getQuestions(category);

        this.numberOfQuestions = Math.min(numberOfQuestions, questions.size());

        for(Question question : questions){
           ArrayList<Answer> answers = JDBC.getAnswers(question);
           question.setAnswers(answers);
        }

        currentQuestion = questions.get(currentQuestionIndex);
        addGuiComponents();
    }
    private void addGuiComponents() {
        JLabel topicLabel = new JLabel("Topic: "+category.getCategoryName());
        topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topicLabel.setBounds(15, 15, 250, 20);
        topicLabel.setForeground(Colors.BRIGHT_YELLOW);
        add(topicLabel);

        scoreLabel = new JLabel("Score: " + score + "/" + numberOfQuestions);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setBounds(270, 15, 96, 20);
        scoreLabel.setForeground(Colors.BRIGHT_YELLOW);
        add(scoreLabel);

        questionTextArea = new JTextArea(currentQuestion.getQuestionText());
        questionTextArea.setFont(new Font("Arial", Font.BOLD, 32));
        questionTextArea.setBounds(15, 50, 350, 91);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setForeground(Colors.BRIGHT_YELLOW);
        questionTextArea.setBackground(Colors.LIGHT_BLUE);
        add(questionTextArea);

        addAnswerComponents();

        JButton returnToTitleButton = new JButton("Return to Title");
        returnToTitleButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnToTitleButton.setBounds(60, 420, 262, 35);
        returnToTitleButton.setBackground(Colors.BRIGHT_YELLOW);
        returnToTitleButton.setForeground(Colors.LIGHT_BLUE);
        returnToTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               TitleScreenGui titleScreenGui = new TitleScreenGui();
               titleScreenGui.setLocationRelativeTo(QuizScreen.this);

               QuizScreen.this.dispose();
               titleScreenGui.setVisible(true);
            }
        });
        add(returnToTitleButton);

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setBounds(240,470,80,35);
        nextButton.setBackground(Colors.BRIGHT_YELLOW);
        nextButton.setForeground(Colors.LIGHT_BLUE);
        nextButton.setVisible(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               nextButton.setVisible(false);
               firstChoiseMade = false;
               currentQuestion = questions.get(++currentQuestionIndex);
               questionTextArea.setText(currentQuestion.getQuestionText());
               for(int i = 0; i < currentQuestion.getAnswers().size(); i++){
                   Answer answer = currentQuestion.getAnswers().get(i);

                   answerButtons[i].setBackground(Color.WHITE);

                   answerButtons[i].setText(answer.getAnswerText());
               }
            }
        });
        add(nextButton);
    }

    private void addAnswerComponents() {
        int verticalSpacegin = 60;
        for(int i = 0; i < currentQuestion.getAnswers().size(); i++){
            Answer answer = currentQuestion.getAnswers().get(i);
            JButton answerButton = new JButton(answer.getAnswerText());
            answerButton.setBounds(60, 180+(i*verticalSpacegin), 262, 45);
            answerButton.setFont(new Font("arial", Font.BOLD, 18));
            answerButton.setHorizontalAlignment(SwingConstants.LEFT);
            answerButton.setBackground(Color.WHITE);
            answerButton.setForeground(Colors.DARK_BLUE);
            answerButton.addActionListener(this);
            answerButtons[i] = answerButton;
            add(answerButtons[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton answerButton = (JButton) e.getSource();

        Answer correctAnswer = null;
        for(Answer answer : currentQuestion.getAnswers()){
           if(answer.isCorrect()){
               correctAnswer = answer;
               break;
           }
        }
        if(answerButton.getText().equals(correctAnswer.getAnswerText())){
           answerButton.setBackground(Colors.LIGHT_GREEN);
           if(!firstChoiseMade){
               scoreLabel.setText("Score: " + (++score) + "/" + numberOfQuestions);
           }
           if(currentQuestionIndex == numberOfQuestions -1){
              JOptionPane.showMessageDialog(QuizScreen.this, "You're final score is " + score + "/" + numberOfQuestions);
           }else{
               nextButton.setVisible(true);
           }
        }else{
            answerButton.setBackground(Colors.LIGHT_RED);
        }
        firstChoiseMade = true;
    }
}
