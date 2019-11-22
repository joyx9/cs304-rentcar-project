package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import static java.awt.GridBagConstraints.RELATIVE;

public class GenerateReportDisplay extends JFrame implements ActionListener {
    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static Pattern p = Pattern.compile("^[ a-zA-Z]*$");
    private MainDisplay mainDisplay;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JTextField branchtf;
    private JButton button;
    private JTextArea report;

    GenerateReportDisplay(MainDisplay md) {
        mainDisplay = md;
        frame = new JFrame();
        frame.setTitle("Rent-A-Car");
        setupDisplay(frame.getContentPane());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupDisplay(Container pane) {
        pane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        // add label & textfield for branch
        JLabel lbranch = new JLabel();
        lbranch.setText("Branch:");
        lbranch.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        pane.add(lbranch, gbc);

        branchtf = new JTextField(20);
        branchtf.setFont(defaultFont);
        branchtf.setMinimumSize(new Dimension(30, 10));
        branchtf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        pane.add(branchtf, gbc);

        // add a "daily rentals" buttons
        // adds "next" button
        button = new JButton("view daily rentals");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("rentalsPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(button, gbc);

        // add a "daily rentals for branch" button
        button = new JButton("view daily rentals for branch");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("rentalsBranchPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        pane.add(button, gbc);

        // add a "daily returns" button
        button = new JButton("view daily returns");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("returnsPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        pane.add(button, gbc);

        // add a "daily returns for branch" button
        button = new JButton("view daily returns for branch");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("returnsBranchPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 5;
        pane.add(button, gbc);

        // add a "back" button
        button = new JButton("back to main");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("backPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 6;
        pane.add(button, gbc);
    }

    private void displayReport(String str){
        if (report == null) {
            report = new JTextArea();
        }
        report.setText(str);
        report.setFont(defaultFont);
        report.setLineWrap(true);
        report.setWrapStyleWord(true);
        report.setEditable(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        gbc.ipadx = 80;
        gbc.ipady = 40;
        frame.getContentPane().add(report, gbc);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "backPressed"){
            mainDisplay.returnFromDisplay();
            frame.setVisible(false);
        } else if (e.getActionCommand() == "rentalsPressed"){
            // todo do something
            String s = "List of daily rentals: !!!!";
            this.displayReport(s);
        } else if (e.getActionCommand() == "rentalsBranchPressed"){
            // todo do something else
            String s = "List of daily rentals for branch: !!!!";
            this.displayReport(s);
        } else if (e.getActionCommand() == "returnsPressed") {
            // todo do something else
            String s = "List of daily returns: !!!!";
            this.displayReport(s);
        } else if (e.getActionCommand() == "returnsBranchPressed") {
            // todo do something else
            String s = "List of daily returns for branch: !!!!";
            this.displayReport(s);
        }
    }
}