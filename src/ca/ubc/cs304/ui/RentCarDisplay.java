package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.RELATIVE;

public class RentCarDisplay extends JFrame implements ActionListener {

    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static int width = 1000;
    private static int height = 800;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JLabel label;
    private JButton button;
    private MainDisplay mainDisplay;
    private JTextField conftf;
    private JTextField cardNotf;
    private JTextField expDatetf;
    private JButton backButton;


    RentCarDisplay(MainDisplay md){
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

        //  we need a check of conf num. if it dne, point them back to main
            // and say they need to reserve first
        // add all the inputs: conf num.
        label = new JLabel();
        label.setText("Reservation Confirmation Number:");
        label.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        pane.add(label, gbc);

        conftf = new JTextField(20);
        conftf.setFont(defaultFont);
        conftf.setMinimumSize(new Dimension(30, 10));
        conftf.setText("");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        pane.add(conftf, gbc);

        // adds "next" button
        button = new JButton("next");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("nextPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pane.add(button, gbc);

        // adds "back to main" button
        backButton = new JButton("Back to Main");
        backButton.setFont(defaultFont);
        backButton.addActionListener(this);
        backButton.setActionCommand("backPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        pane.add(backButton, gbc);
    }

    private void displayNext(boolean hasReservation){
        // either redirects user back to main, or asks them for rest of rental information
        if (!hasReservation) {
            JLabel lnores = new JLabel("No reservation found. Go back to main and make a reservation");
            lnores.setFont(defaultFont);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = RELATIVE;
            frame.getContentPane().add(lnores, gbc);
        } else {
            // remove the 'next' button
            frame.getContentPane().remove(button);
            // add the following inputs: cardNo, ExpDate
            // add cardNo
            JLabel lcard = new JLabel();
            lcard.setText("credit card number:");
            lcard.setFont(defaultFont);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 4;
            frame.getContentPane().add(lcard, gbc);

            cardNotf = new JTextField(20);
            cardNotf.setFont(defaultFont);
            cardNotf.setMinimumSize(new Dimension(30, 10));
            cardNotf.setText("");
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 1;
            gbc.gridy = 4;
            frame.getContentPane().add(cardNotf, gbc);

            // add expDate
            JLabel lexp = new JLabel("Card Expiry Date");
            lexp.setText("credit card expiry date:");
            lexp.setFont(defaultFont);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 5;
            frame.getContentPane().add(lexp, gbc);

            expDatetf = new JTextField(20);
            expDatetf.setFont(defaultFont);
            expDatetf.setMinimumSize(new Dimension(30, 10));
            expDatetf.setText("");
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 1;
            gbc.gridy = 5;
            frame.getContentPane().add(expDatetf, gbc);

            // adds "confirm rental" button
            button = new JButton("Confirm Rental");
            button.setFont(defaultFont);
            button.addActionListener(this);
            button.setActionCommand("confirmPressed");
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = RELATIVE;
            frame.getContentPane().add(button, gbc);
        }
        frame.revalidate();
        frame.repaint();
    }

    private void displayReceipt(String str){
        // adds text field with all vehicle names
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
        } else if (e.getActionCommand() == "nextPressed"){
            // todo check if they have a reservation !!!!!!!!!!
            boolean hasReservation = true;
            this.displayNext(hasReservation);
        } else if (e.getActionCommand() == "confirmPressed"){
            // todo get receipt !!!!!
            String str = "Receipt!!!!!!!!!!!!!!!!!!!";
            this.displayReceipt(str);
        }
    }
}

