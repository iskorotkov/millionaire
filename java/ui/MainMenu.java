package ui;

import game.Game;
import game.GameStatus;
import game.Rewards;
import questions.Answer;
import storage.SQLiteLoader;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;

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
    private JList<Integer> rewardsLst;

    public MainMenu() {
        setContentPane(contentPane);
        setModal(true);

        var path = System.getenv("QUESTIONS_DB");
        var loader = new SQLiteLoader();
        var questions = loader.load(path);

        var rewards = new Integer[]{
                500,
                1_000,
                2_000,
                3_000,
                5_000,
                10_000,
                15_000,
                25_000,
                50_000,
                100_000,
                200_000,
                400_000,
                800_000,
                1_500_000,
                3_000_000
        };

        var rewardsList = Arrays.asList(rewards.clone());
        Collections.reverse(rewardsList);

        var model = new DefaultListModel<Integer>();
        model.addAll(rewardsList);
        rewardsLst.setModel(model);
        var tier = rewards.length - 1 - selectTier(model);

        var game = new Game(questions, new Rewards(tier, rewards));
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

    private int selectTier(ListModel<Integer> model) {
        TierSelection dialog = new TierSelection(model);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.getSelectedTier();
    }

    private void selectAnswer(Game game, int i) {
        if (!game.answer(i)) {
            JOptionPane.showMessageDialog(this, "Неверный ответ!");
        }
        update(game);
    }

    private void update(Game game) {
        updateRewardsList(game);

        if (game.getGameStatus() == GameStatus.InProgress) {
            updateLifelinesButtons(game);
            updateQuestion(game);
        } else {
            disableButtons();
            var reward = game.getCurrentReward();

            if (game.getGameStatus() == GameStatus.Won) {
                JOptionPane.showMessageDialog(this, String.format("Вы выиграли! Ваш выигрыш составил %d рублей", reward));
            } else {
                JOptionPane.showMessageDialog(this, String.format("Вы проиграли! Ваш выигрыш составил %d рублей", reward));
            }
        }
    }

    private void updateRewardsList(Game game) {
        rewardsLst.setSelectedValue(game.getCurrentReward(), true);
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
        btn.setEnabled(answer.isEnabled());
    }

    public static void main(String[] args) {
        MainMenu dialog = new MainMenu();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
