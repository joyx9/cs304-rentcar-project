package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.RELATIVE;
import static java.lang.Integer.parseInt;

public class ReturnCarDisplay extends JFrame implements ActionListener {

    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static int width = 1000;
    private static int height = 800;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JLabel label;
    private JButton button;
    private MainDisplay mainDisplay;
    private TerminalTransactionsDelegate delegate;
    private JTextField datetf;
    private JTextField odomtf;
    private JTextField tanktf;
    private JTextField ridtf;
    private JButton backButton;


    ReturnCarDisplay(MainDisplay md, TerminalTransactionsDelegate del){
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("Rent-A-Car");
        mainDisplay = md;
        delegate = del;
        setupDisplay(frame.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupDisplay(Container pane) {
        pane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        // add dateReturned, odometer, fulltank
        // add dateReturned
        JLabel ldate = new JLabel();
        ldate.setText("Date Returned (YYYY/MM/DD):");
        ldate.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        pane.add(ldate, gbc);

        datetf = new JTextField(20);
        datetf.setFont(defaultFont);
        datetf.setMinimumSize(new Dimension(30, 10));
        datetf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        pane.add(datetf, gbc);

        // add odometer
        JLabel lodom = new JLabel();
        lodom.setText("Odometer Reading:");
        lodom.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(lodom, gbc);

        odomtf = new JTextField(20);
        odomtf.setFont(defaultFont);
        odomtf.setMinimumSize(new Dimension(30, 10));
        odomtf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 2;
        pane.add(odomtf, gbc);

        // add fulltank (Y/N)
        JLabel ltank = new JLabel();
        ltank.setText("Tank Full? (y/n):");
        ltank.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        pane.add(ltank, gbc);

        tanktf = new JTextField(20);
        tanktf.setFont(defaultFont);
        tanktf.setMinimumSize(new Dimension(30, 10));
        tanktf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 3;
        pane.add(tanktf, gbc);

        // add rental ID
        JLabel lrid = new JLabel();
        lrid.setText("Rental ID:");
        lrid.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        pane.add(lrid, gbc);

        ridtf = new JTextField(20);
        ridtf.setFont(defaultFont);
        ridtf.setMinimumSize(new Dimension(30, 10));
        ridtf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 4;
        pane.add(ridtf, gbc);


        // adds "confirm return" button
        button = new JButton("confirm return");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("confirmPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        pane.add(button, gbc);

        // adds "back to main" button
        backButton = new JButton("Back to Main");
        backButton.setFont(defaultFont);
        backButton.addActionListener(this);
        backButton.setActionCommand("backPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        pane.add(backButton, gbc);
    }

    private void displayReceipt(String str){
        JTextArea receipt = new JTextArea();
        receipt.setText("YOUR RECEIPT:" + str);
        receipt.setFont(defaultFont);
        receipt.setLineWrap(true);
        receipt.setWrapStyleWord(true);
        receipt.setEditable(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        gbc.ipadx = 40;
        gbc.ipady = 40;
        frame.getContentPane().add(receipt, gbc);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "backPressed"){
            mainDisplay.returnFromDisplay();
            frame.setVisible(false);
        } else if (e.getActionCommand() == "confirmPressed"){
            //todo get receipt info or return error if rid is not in rentals
            try {
                String str = delegate.returnVehicle(parseInt(ridtf.getText()),datetf.getText(),
                        parseInt(odomtf.getText()), tanktf.getText());
                this.displayReceipt(str);
            }
            catch (Exception exc) {
                String str = "ERROR: something went wrong. Please check your inputs and try again";
                this.displayReceipt(str);
            }
        }
    }
}