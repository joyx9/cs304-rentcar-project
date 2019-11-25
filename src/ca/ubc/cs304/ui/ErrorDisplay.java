package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import static java.awt.GridBagConstraints.RELATIVE;

public class ErrorDisplay extends JFrame implements ActionListener{
    private JFrame frame = null;
    private String errString;
    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);




    public ErrorDisplay(String s){
        frame = new JFrame();
        errString = s;
        frame.setTitle("Rent-A-Car");
        setupDisplay(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        JButton backButton = new JButton("Back to Main");
        backButton.setFont(defaultFont);
        backButton.addActionListener(this);
        backButton.setActionCommand("backPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        pane.add(backButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "backPressed"){
            frame.setVisible(false);
        }
    }

}
