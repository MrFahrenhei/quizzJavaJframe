package com.app.quizz.screens;

import com.app.quizz.connection.JDBC;
import com.app.quizz.design.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateQuestion extends JFrame{
    private JTextArea questionTextArea;
    private JTextField categoryTextField;
    private final JTextField[] answerTextFields;
    private final ButtonGroup buttonGroup;
    private final JRadioButton[] answerRadioButtons;

    public  CreateQuestion(){
        super("Create a Question");
        setSize(851, 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Colors.LIGHT_BLUE);

        answerRadioButtons = new JRadioButton[4];
        answerTextFields = new JTextField[4];
        buttonGroup = new ButtonGroup();

        addGuiComponents();
    }

    private void addGuiComponents(){
        JLabel titleLabel = new JLabel("Create a Question");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(50, 15, 310, 29);
        titleLabel.setForeground(Colors.BRIGHT_YELLOW);
        add(titleLabel);

        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setBounds(58, 60, 93, 20);
        questionLabel.setForeground(Colors.BRIGHT_YELLOW);
        add(questionLabel);

        questionTextArea = new JTextArea();
        questionTextArea.setFont(new Font("Arial", Font.BOLD, 16));
        questionTextArea.setBounds(50, 90, 310, 110);
        questionTextArea.setForeground(Colors.DARK_BLUE);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        add(questionTextArea);

        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        categoryLabel.setBounds(50, 250, 93, 20);
        categoryLabel.setForeground(Colors.BRIGHT_YELLOW);
        add(categoryLabel);

        categoryTextField = new JTextField();
        categoryTextField.setFont(new Font("Arial", Font.BOLD, 16));
        categoryTextField.setBounds(50, 280, 310, 36);
        categoryTextField.setForeground(Colors.DARK_BLUE);
        add(categoryTextField);

        addAnswerComponents();

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBounds(300, 450, 262, 45);
        submitButton.setForeground(Colors.DARK_BLUE);
        submitButton.setBackground(Colors.BRIGHT_YELLOW);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validateInput()) {
                    String question = questionTextArea.getText();
                    String category = categoryTextField.getText();
                    String[] answers = new String[answerTextFields.length];
                    int correctIndex = 0;
                    for (int i = 0; i < answers.length; i++) {
                        answers[i] = answerTextFields[i].getText();
                        if (answerRadioButtons[i].isSelected()) {
                            correctIndex = i;
                        }
                    }
                    if (JDBC.saveQuestionCategoryAndAnswers(question, category, answers, correctIndex)) {
                        JOptionPane.showMessageDialog(CreateQuestion.this, "Successfuly added Question");
                        resetFields();
                    } else {
                        JOptionPane.showMessageDialog(CreateQuestion.this, "Something went wrong");
                    }
                }else{
                    JOptionPane.showMessageDialog(CreateQuestion.this, "Invalid Input");
                }
            }
        });
        add(submitButton);

        JLabel returnLabel = new JLabel("Return");
        returnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        returnLabel.setBounds(300, 500, 262, 20);
        returnLabel.setForeground(Colors.BRIGHT_YELLOW);
        returnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        returnLabel.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        TitleScreenGui titleScreenGui = new TitleScreenGui();
                        titleScreenGui.setLocationRelativeTo(CreateQuestion.this);

                        CreateQuestion.this.dispose();

                        titleScreenGui.setVisible(true);dispose();
                    }
                }
        );
        add(returnLabel);
    }

    private void addAnswerComponents(){
        int verticalSpacing = 100;

        for(int i = 0; i < 4; i++)
        {
            JLabel answerLabel = new JLabel("Answer #" + (i+1));
            answerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            answerLabel.setBounds(470, 60 + (i*verticalSpacing), 93, 20);
            answerLabel.setForeground(Colors.BRIGHT_YELLOW);
            add(answerLabel);

            answerRadioButtons[i] = new JRadioButton();
            answerRadioButtons[i].setBounds(440, 100 + (i * verticalSpacing), 21, 21);
            answerRadioButtons[i].setBackground(null);
            buttonGroup.add(answerRadioButtons[i]);
            add(answerRadioButtons[i]);

            answerTextFields[i] = new JTextField();
            answerTextFields[i].setBounds(470, 90 + (i * verticalSpacing), 310, 36);
            answerTextFields[i].setFont(new Font("Arial", Font.PLAIN, 16));
            answerTextFields[i].setForeground(Colors.DARK_BLUE);
            add(answerTextFields[i]);
        }
        answerRadioButtons[0].setSelected(true);
    }

    private boolean validateInput(){
        if(questionTextArea.getText().replaceAll("", "").length() <= 0)return false;
        if(categoryTextField.getText().replaceAll("", "").length() <= 0)return false;
        for(int i = 0; i < answerTextFields.length; i++){
            if(answerTextFields[i].getText().replaceAll("", "").length() <= 0){
                return false;
            }
        }
        return true;
    }

    private void resetFields(){
        questionTextArea.setText("");
        categoryTextField.setText("");
        for(int i = 0; i < answerRadioButtons.length; i++){
            answerTextFields[i].setText("");
        }
    }
}
