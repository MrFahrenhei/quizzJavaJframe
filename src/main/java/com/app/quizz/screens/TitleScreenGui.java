package com.app.quizz.screens;

import com.app.quizz.connection.JDBC;
import com.app.quizz.design.Colors;
import com.app.quizz.models.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TitleScreenGui extends JFrame {
    private JComboBox categoriesMenu;
    private JTextField numOfQuestionsField;


    public TitleScreenGui() {
        super("Aplicativo Quizz");

        setSize(400, 565);

        // allowing manual position of the components
        setLayout(null);

        // set the frame to be centered on the screen when displayed
        setLocationRelativeTo(null);

        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(Colors.LIGHT_BLUE);

        addGuiComponents();
    }
    private void addGuiComponents() {
        JLabel titleLabel = new JLabel("Quiz Game!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(0, 20, 390, 43);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Colors.BRIGHT_YELLOW);
        add(titleLabel);

        JLabel chooseCategoryLabel = new JLabel("Choose Category");
        chooseCategoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chooseCategoryLabel.setBounds(0, 90, 400, 43);
        chooseCategoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseCategoryLabel.setForeground(Colors.BRIGHT_YELLOW);
        add(chooseCategoryLabel);

        // drop down menu for categories
        // temporary categories
        //String[] categories = new String[]{"Math", "Programing", "History"};

        ArrayList<String> categoriesList = JDBC.getCategories();
        categoriesMenu = new JComboBox(categoriesList.toArray());
        categoriesMenu.setBounds(20, 120, 337, 45);
        categoriesMenu.setForeground(Colors.DARK_BLUE);
        add(categoriesMenu);

        JLabel numOfQuestionsLabel = new JLabel("Number of Questions");
        numOfQuestionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsLabel.setBounds(20, 190, 172, 20);
        numOfQuestionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numOfQuestionsLabel.setForeground(Colors.BRIGHT_YELLOW);
        add(numOfQuestionsLabel);

        numOfQuestionsField = new JTextField("10");
        numOfQuestionsField.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsField.setBounds(200, 190, 148, 26);
        numOfQuestionsField.setForeground(Colors.DARK_BLUE);
        add(numOfQuestionsField);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBounds(65, 290, 262, 45);
        startButton.setBackground(Colors.BRIGHT_YELLOW);
        startButton.setForeground(Colors.LIGHT_BLUE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validInput()){
                    Category category = JDBC.getCategory(categoriesMenu.getSelectedItem().toString());
                    if(category == null) return;
                    int numOfQuestions = Integer.parseInt(numOfQuestionsField.getText());
                    QuizScreen quizScreen = new QuizScreen(category, numOfQuestions);
                    quizScreen.setLocationRelativeTo(TitleScreenGui.this);

                    TitleScreenGui.this.dispose();

                    quizScreen.setVisible(true);
                }
            }
        });
        add(startButton);

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 16));
        quitButton.setBounds(65, 350, 262, 45);
        quitButton.setBackground(Colors.BRIGHT_YELLOW);
        quitButton.setForeground(Colors.LIGHT_BLUE);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               TitleScreenGui.this.dispose();
            }
        });
        add(quitButton);

        JButton createAQuestion = new JButton("Create a Question");
        createAQuestion.setFont(new Font("Arial", Font.BOLD, 16));
        createAQuestion.setBounds(65, 420, 262, 45);
        createAQuestion.setBackground(Colors.BRIGHT_YELLOW);
        createAQuestion.setForeground(Colors.LIGHT_BLUE);
        createAQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateQuestion createQuestion = new CreateQuestion();
                createQuestion.setLocationRelativeTo(TitleScreenGui.this);

                TitleScreenGui.this.dispose();

                createQuestion.setVisible(true);
            }
        });
        add(createAQuestion);
    }

    private boolean validInput() {
        if(numOfQuestionsField.getText().replaceAll(" ", "").length() <= 0) return false;
        if(categoriesMenu.getSelectedItem() == null) return false;
        return true;
    }
}
