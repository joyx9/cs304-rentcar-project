package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
public class ErrorDisplay extends JFrame{
    private JFrame frame = null;
    private String errString;

    public ErrorDisplay(String s){
        frame = new JFrame();
        errString = s;
        frame.setTitle("Rent-A-Car");
        setupDisplay(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    private void setupDisplay(Container pane) {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // adds introductory label
        JLabel label = new JLabel();
        label.setText(errString);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        pane.add(label, gbc);
    }

}
