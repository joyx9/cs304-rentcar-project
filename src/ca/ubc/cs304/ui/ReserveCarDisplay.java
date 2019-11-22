
package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import static java.awt.GridBagConstraints.RELATIVE;

public class ReserveCarDisplay extends JFrame implements ActionListener {

    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static int width = 1000;
    private static int height = 800;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JLabel label;
    private JButton button;
    private MainDisplay mainDisplay;
    private JTextField carTypetf;
    private JTextField licencetf;
    private JTextField locationtf;
    private JTextField fromDatetf;
    private JTextField toDatetf;
    private JTextField nametf;
    private JTextField addresstf;

    ReserveCarDisplay(MainDisplay md){
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("Rent-A-Car");
        mainDisplay = md;
        setupDisplay(frame.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupDisplay(Container pane) {
        pane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

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

        // adds button & label for licence
        JLabel l2 = new JLabel();
        l2.setText("license #:");
        l2.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(l2, gbc);

        licencetf = new JTextField(20);
        licencetf.setFont(defaultFont);
        licencetf.setMinimumSize(new Dimension(30, 5));
        licencetf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 2;
        pane.add(licencetf, gbc);

        // adds button & label for location
        JLabel loc = new JLabel();
        loc.setText("location:");
        loc.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        pane.add(loc, gbc);

        locationtf = new JTextField(20);
        locationtf.setFont(defaultFont);
        locationtf.setMinimumSize(new Dimension(30, 5));
        locationtf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 3;
        pane.add(locationtf, gbc);

        // adds button & label for time interval
        // todo add a check for yyyy/mm/dd
        JLabel l3 = new JLabel();
        l3.setText("Start Time (yyyy/mm/dd):");
        l3.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        pane.add(l3, gbc);

        fromDatetf = new JTextField(20);
        fromDatetf.setFont(defaultFont);
        fromDatetf.setMinimumSize(new Dimension(30, 10));
        fromDatetf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 4;
        pane.add(fromDatetf, gbc);

        // adds button & label for time interval
        // todo add a check for yyyy/mm/dd
        JLabel l4 = new JLabel();
        l4.setText("End Time (yyyy/mm/dd):");
        l4.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        pane.add(l4, gbc);

        toDatetf = new JTextField(20);
        toDatetf.setFont(defaultFont);
        toDatetf.setMinimumSize(new Dimension(30, 10));
        toDatetf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 5;
        pane.add(toDatetf, gbc);

        // adds button & label for name
        JLabel lname = new JLabel();
        lname.setText("Your Name:");
        lname.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 6;
        pane.add(lname, gbc);

        nametf = new JTextField(20);
        nametf.setFont(defaultFont);
        nametf.setMinimumSize(new Dimension(30, 10));
        nametf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 6;
        pane.add(nametf, gbc);

        // adds button & label for address
        JLabel laddress = new JLabel();
        laddress.setText("Your Address:");
        laddress.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 7;
        pane.add(laddress, gbc);

        addresstf = new JTextField(20);
        addresstf.setFont(defaultFont);
        addresstf.setMinimumSize(new Dimension(30, 10));
        addresstf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 7;
        pane.add(addresstf, gbc);

        // adds "confirm" button
        button = new JButton("confirm");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("confirmPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        pane.add(button, gbc);

        // adds "back to main" button
        button = new JButton("Back to Main");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("backPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        pane.add(button, gbc);
    }

    private void displayConfNum(int num){
        // adds confirmation number text field
        JLabel conf = new JLabel("Your Confirmation Number:" + num);
        conf.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        frame.getContentPane().add(conf, gbc);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "backPressed"){
            mainDisplay.returnFromDisplay();
            frame.setVisible(false);
        } else if (e.getActionCommand() == "confirmPressed"){
            // todo get the confirmation number!
            int num = -1;
            this.displayConfNum(num);
        }
    }
}
