package com.app.quizz.main;

import com.app.quizz.screens.CreateQuestion;
import com.app.quizz.screens.TitleScreenGui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                new TitleScreenGui().setVisible(true);
                new CreateQuestion().setVisible(true);
            }
        });
    }
}
