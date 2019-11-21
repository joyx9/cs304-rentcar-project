package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ViewVehiclesDisplay extends JFrame implements ActionListener {
    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static String initScoreText = "CURRENT SCORE: 0";
    private static Pattern p = Pattern.compile("^[ a-zA-Z]*$");
    private static String tfInitial = "";
    private static int width = 1000;
    private static int height = 800;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JLabel label;
    private JButton button;
    private JTextArea vehicleText;
    private MainDisplay mainDisplay;

    ViewVehiclesDisplay(MainDisplay md){
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

        // adds text field with all vehicle names
        vehicleText = new JTextArea();
        vehicleText.setText("Here is a list of all vehicles:");
        vehicleText.setFont(defaultFont);
        vehicleText.setColumns(2);
        vehicleText.setLineWrap(true);
        vehicleText.setWrapStyleWord(true);
        vehicleText.setEditable(false);
        // todo scrollpane not working lol
        JScrollPane scroll = new JScrollPane(vehicleText);
        // todo this should hold actual list of vehicles:
        vehicleText.setText("todo need this to be vehicle list" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        pane.add(vehicleText, gbc);

        // adds "ok" button
        button = new JButton("Ok");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("okPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(button, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "okPressed"){
           mainDisplay.returnFromDisplay();
           frame.setVisible(false);
        }
    }
}
