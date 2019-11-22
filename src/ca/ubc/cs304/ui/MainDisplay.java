package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class MainDisplay extends JFrame implements ActionListener {
    public TerminalTransactionsDelegate delegate = null;
    private static String[] multStrings= {"view vehicles", "make reservation", "rent vehicle",
                                            "return vehicle", "generate report"};
    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static Pattern p = Pattern.compile("^[ a-zA-Z]*$");
    final static int width = 1000;
    final static int height = 800;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JLabel label;
    private JButton button;
    private JComboBox multSelect;


    public MainDisplay(){
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("Rent-A-Car");
        setupDisplay(frame.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    public void showDisplay(TerminalTransactionsDelegate delegate) {
        this.delegate = delegate;
        frame.setVisible(true);
    }

    private void setupDisplay(Container pane) {
        pane.setLayout(new GridBagLayout());
        pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        gbc = new GridBagConstraints();
        // adds introductory label
        label = new JLabel();
        label.setText("Welcome to Rent-A-Car! What would you like to do?");
        label.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        pane.add(label, gbc);

        multSelect = new JComboBox(multStrings);
        multSelect.setSelectedIndex(0);
        multSelect.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        pane.add(multSelect, gbc);

        button = new JButton("Ok");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("okPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(button, gbc);
    }

    public void returnFromDisplay(){
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "okPressed"){
            String multString = (String) multSelect.getSelectedItem();
            if (multString == "view vehicles"){
                new ViewVehiclesDisplay (this, delegate);
            } else if (multString == "make reservation") {
               new ReserveCarDisplay (this);
            } else if (multString == "rent vehicle") {
               new RentCarDisplay (this, delegate);
            } else if (multString == "return vehicle") {
                new ReturnCarDisplay (this);
            } else if (multString == "generate report"){
                new GenerateReportDisplay (this);
            }
            frame.setVisible(false);
        }
    }


}
