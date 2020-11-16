package ui;

import javax.swing.*;

public class TierSelection extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JList<Integer> tiersLst;

    public TierSelection(ListModel<Integer> tiers) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        tiersLst.setModel(tiers);
        tiersLst.setSelectedIndex(0);

        buttonOK.addActionListener(e -> dispose());
    }

    public int getSelectedTier() {
        return tiersLst.getSelectedIndex();
    }
}
