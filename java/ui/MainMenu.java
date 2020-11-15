package ui;

import javax.swing.*;

public class MainMenu extends JDialog {
    private JPanel contentPane;
    private JButton hallHelpBtn;
    private JButton fiftyFiftyBtn;
    private JButton callFriendBtn;
    private JButton secondChanceBtn;
    private JButton questionSwapBtn;
    private JList rewardsLst;
    private JButton answer1Btn;
    private JButton answer2Btn;
    private JButton answer3Btn;
    private JButton answer4Btn;

    public MainMenu() {
        setContentPane(contentPane);
        setModal(true);
    }

    public static void main(String[] args) {
        MainMenu dialog = new MainMenu();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
