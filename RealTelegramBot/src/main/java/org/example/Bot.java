package org.example;
import org.example.question.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {

    private HashMap<Long, UserData> users;
    private ArrayList<AbstractQuestion> questions;

    public Bot() {
        users = new HashMap<>();
        questions = new ArrayList<>();
        questions.add(new JavaQuestion());
        questions.add(new SQLQuestion());
        questions.add(new GitQuestion());
        questions.add(new HttpQuestion());
    }

    @Override
    public String getBotUsername() {
        return "@...";
    }

    @Override
    public String getBotToken() {
        return ".......";
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        long userId = message.getFrom().getId();

        if (text.equals("/start")) {
            sendText(userId, "Привет! Это тест по Java.  Сейчас начнём:)");
            users.put(userId, new UserData());
            String question = questions.get(0).getQuestion();
            sendText(userId, question);
        } else if (users.get(userId).getQuestionNumber() == questions.size()) {
            sendText(userId, "Тест завершён. Для перезапуска бота используйте команду /start");
        } else {
            UserData userData = users.get(userId);
            int questionNumber = userData.getQuestionNumber();
            boolean result = questions.get(questionNumber).checkAnswer(text);
            int score = userData.getScore();
            int nextQuestion = questionNumber + 1;
            userData.setScore(score + (result ? 1 : 0));
            userData.setQuestionNumber(nextQuestion);

            if (questionNumber + 1 == questions.size()) {
                sendText(userId, "Ваш рейтинг: " + users.get(userId).getScore() +
                        " из" + questions.size());
            } else {
                String question = questions.get(userData.getQuestionNumber()).getQuestion();
                sendText(userId, question);
            }
        }
    }
}

