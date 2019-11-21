
package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ReserveCarDisplay extends JFrame implements ActionListener {

    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static int width = 1000;
    private static int height = 800;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JLabel label;
    private JButton button;
    private MainDisplay mainDisplay;

    ReserveCarDisplay(MainDisplay md){
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("Rent-A-Car View Vehicles");
        mainDisplay = md;
        setupDisplay(frame.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupDisplay(Container pane) {
        pane.setLayout(new GridBagLayout());
        pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        gbc = new GridBagConstraints();
        // adds introductory label
        label = new JLabel();
        label.setText("Here is a list of all vehicles:");
        label.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        pane.add(label, gbc);

        // todo add all the inputs:


        // adds "ok" button
        button = new JButton("Done");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("donePressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(button, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "donePressed"){
            mainDisplay.returnFromDisplay();
            frame.setVisible(false);
        }
    }
}
