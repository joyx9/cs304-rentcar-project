package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import static java.awt.GridBagConstraints.RELATIVE;

public class ViewVehiclesDisplay extends JFrame implements ActionListener {
    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static int width = 1000;
    private static int height = 800;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JLabel label;
    private JButton button;
    private JTextArea vehicleText;
    private MainDisplay mainDisplay;
    private JTextField carTypetf;
    private JTextField locationtf;
    private JTextField timetf;

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
        gbc = new GridBagConstraints();
        // adds introductory label
        label = new JLabel();
        label.setText("Enter Information");
        label.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        pane.add(label, gbc);

        // adds button & label for car type
        label = new JLabel();
        label.setText("Car Type:");
        label.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        pane.add(label, gbc);

        carTypetf = new JTextField(20);
        carTypetf.setFont(defaultFont);
        carTypetf.setMinimumSize(new Dimension(30, 10));
        carTypetf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        pane.add(carTypetf, gbc);

        // adds button & label for location
        JLabel l2 = new JLabel();
        l2.setText("Location:");
        l2.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(l2, gbc);

        locationtf = new JTextField(20);
        locationtf.setFont(defaultFont);
        locationtf.setMinimumSize(new Dimension(30, 5));
        locationtf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 2;
        pane.add(locationtf, gbc);

        // adds button & label for time interval
        // todo add a check for yyyy/mm/dd
        JLabel l3 = new JLabel();
        l3.setText("Start Time (yyyy/mm/dd):");
        l3.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        pane.add(l3, gbc);

        timetf = new JTextField(20);
        timetf.setFont(defaultFont);
        timetf.setMinimumSize(new Dimension(30, 10));
        timetf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 3;
        pane.add(timetf, gbc);

        // adds button & label for time interval
        // todo add a check for yyyy/mm/dd
        JLabel l4 = new JLabel();
        l4.setText("End Time (yyyy/mm/dd):");
        l4.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        pane.add(l4, gbc);

        timetf = new JTextField(20);
        timetf.setFont(defaultFont);
        timetf.setMinimumSize(new Dimension(30, 10));
        timetf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 4;
        pane.add(timetf, gbc);

        // adds "show available vehicles" button
        button = new JButton("show available vehicles");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("showPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        pane.add(button, gbc);

        // adds "back to main" button
        button = new JButton("back to main");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("backPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 6;
        pane.add(button, gbc);
    }

    private void showNumVehicles(){
        // adds "num available vehicles" button
        String num = "!!!!!";
        button = new JButton("# of available vehicles:" + num);
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("detailsPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        frame.getContentPane().add(button, gbc);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "showPressed"){
            this.showNumVehicles();
        } else if (e.getActionCommand() == "backPressed"){
            mainDisplay.returnFromDisplay();
            frame.setVisible(false);
        } else if (e.getActionCommand() == "detailsPressed"){
            new ViewVehicleDetailDisplay(mainDisplay);
            frame.setVisible(false);
        }
    }
}
