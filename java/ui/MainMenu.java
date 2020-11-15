package ui;

import game.Game;
import questions.Answer;
import questions.Questions;

import javax.swing.*;

public class MainMenu extends JDialog {
    private JPanel contentPane;
    private JButton hallHelpBtn;
    private JButton fiftyFiftyBtn;
    private JButton callFriendBtn;
    private JButton secondChanceBtn;
    private JButton questionSwapBtn;
    private JButton answer1Btn;
    private JButton answer2Btn;
    private JButton answer3Btn;
    private JButton answer4Btn;
    private JLabel questionTxt;
    private JList<String> rewardsLst;

    public MainMenu() {
        setContentPane(contentPane);
        setModal(true);

        var path = System.getenv("QUESTIONS_FILE");
        var questions = Questions.fromFile(path);
        var game = new Game(questions);
        update(game);

        hallHelpBtn.addActionListener(e -> {
            game.takeHallHelp();
            update(game);
        });

        fiftyFiftyBtn.addActionListener(e -> {
            game.takeFiftyFifty();
            update(game);
        });

        callFriendBtn.addActionListener(e -> {
            game.takeCallFriend();
            update(game);
        });

        secondChanceBtn.addActionListener(e -> {
            game.takeSecondChance();
            update(game);
        });

        questionSwapBtn.addActionListener(e -> {
            game.takeQuestionSwap();
            update(game);
        });

        answer1Btn.addActionListener(e -> selectAnswer(game, 0));
        answer2Btn.addActionListener(e -> selectAnswer(game, 1));
        answer3Btn.addActionListener(e -> selectAnswer(game, 2));
        answer4Btn.addActionListener(e -> selectAnswer(game, 3));
    }

    private void selectAnswer(Game game, int i) {
        if (!game.answer(i)) {
            JOptionPane.showMessageDialog(this, "Неверный ответ!");
        }
        update(game);
    }

    private void update(Game game) {
        switch (game.getGameStatus()) {
            case Won -> {
                updateRewardsList(game);
                disableButtons();
                JOptionPane.showMessageDialog(this, "Вы выиграли!");
            }
            case Lost -> {
                updateRewardsList(game);
                disableButtons();
                JOptionPane.showMessageDialog(this, "Вы проиграли!");
            }
            case InProgress -> {
                updateRewardsList(game);
                updateLifelinesButtons(game);
                updateQuestion(game);
            }
        }
    }

    private void updateRewardsList(Game game) {
        rewardsLst.setSelectedIndex(rewardsLst.getModel().getSize() - 1 - game.getCurrentReward());
    }

    private void updateQuestion(Game game) {
        var question = game.getQuestion();
        questionTxt.setText(question.getText());

        var answers = question.getAnswers();
        updateAnswerButton(answer1Btn, answers[0]);
        updateAnswerButton(answer2Btn, answers[1]);
        updateAnswerButton(answer3Btn, answers[2]);
        updateAnswerButton(answer4Btn, answers[3]);
    }

    private void updateLifelinesButtons(Game game) {
        hallHelpBtn.setEnabled(game.hasHallHelp());
        fiftyFiftyBtn.setEnabled(game.hasFiftyFifty());
        callFriendBtn.setEnabled(game.hasCallFriend());
        secondChanceBtn.setEnabled(game.hasSecondChance());
        questionSwapBtn.setEnabled(game.hasQuestionSwap());
    }

    private void disableButtons() {
        hallHelpBtn.setEnabled(false);
        fiftyFiftyBtn.setEnabled(false);
        callFriendBtn.setEnabled(false);
        secondChanceBtn.setEnabled(false);
        questionSwapBtn.setEnabled(false);

        answer1Btn.setEnabled(false);
        answer2Btn.setEnabled(false);
        answer3Btn.setEnabled(false);
        answer4Btn.setEnabled(false);
    }

    private void updateAnswerButton(JButton btn, Answer answer) {
        btn.setText(answer.getText());
        btn.setEnabled(answer.isActive());
    }

    public static void main(String[] args) {
        MainMenu dialog = new MainMenu();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
