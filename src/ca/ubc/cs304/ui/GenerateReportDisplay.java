package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static java.awt.GridBagConstraints.RELATIVE;

public class GenerateReportDisplay extends JFrame implements ActionListener {
    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private MainDisplay mainDisplay;
    private TerminalTransactionsDelegate delegate;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JTextField branchtf;
    private JButton button;
    private JTextArea report;

    GenerateReportDisplay(MainDisplay md, TerminalTransactionsDelegate del) {
        mainDisplay = md;
        delegate = del;
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

    private void displayReport(ArrayList<String[]> repStr){
        // remove all buttons from frame
        frame.getContentPane().removeAll();
        //
        // headers for the table
        String[] columns = new String[] {
                "location", "vehicle type", "# rented"};
        Object[][] data = new Object[][] {};
        // create table with data
        JTable report = new JTable(data, columns);
        JScrollPane jScrollPane = new JScrollPane(report);
        DefaultTableModel dtm = new DefaultTableModel(0, 3);
        report.setModel(dtm);
        // for each row in repStr EXCEPT the last row, add a row to the table
        // for the last row, add it to a jlabel
        String totNum = "Total # of cars rented: ";
        for (int i = 0; i < repStr.size(); i++){
            if (i == repStr.size() -1 ){
                totNum += repStr.get(i)[0];
            } else {
                dtm.addRow(new Object[]{
                        repStr.get(i)[0], repStr.get(i)[1], repStr.get(i)[2],
                });
            }
        }
        frame.getContentPane().add(jScrollPane,
                new GridBagConstraints(0, RELATIVE, 1, 1, 1.0,
                        1.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
                        new Insets(5, 5, 5, 5), 0, 0));
        frame.revalidate();

        // add jlabel for tot # of cars
        JLabel ltot = new JLabel();
        ltot.setText(totNum);
        ltot.setFont(defaultFont);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.getContentPane().add(ltot, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;

        // add back to main button
        button = new JButton("back to main");
        button.setFont(defaultFont);
        button.addActionListener(this);
        button.setActionCommand("backPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.getContentPane().add(button, gbc);

        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "backPressed"){
            mainDisplay.returnFromDisplay();
            frame.setVisible(false);
        } else if (e.getActionCommand() == "rentalsPressed"){
//            String[] str = new String[]{"a", "b", "c", "d", "e", "f", "g"};
//            String[] str2 = new String[]{"a", "b", "c", "d", "e", "f", "g"};
//            ArrayList<String[]> arrStr= new ArrayList<>();
//            arrStr.add(str);
//            arrStr.add(str2);
            try {
                ArrayList<String[]> arrStr = delegate.getDailyRentals();
                this.displayReport(arrStr);
            } catch (Exception exc){
                new ErrorDisplay ("ERROR: something went wrong");
            }
        } else if (e.getActionCommand() == "rentalsBranchPressed"){
            // todo do something else
            String s = "List of daily rentals for branch: !!!!";
           // this.displayReport(s);
        } else if (e.getActionCommand() == "returnsPressed") {
            // todo do something else
            String s = "List of daily returns: !!!!";
            //this.displayReport(s);
        } else if (e.getActionCommand() == "returnsBranchPressed") {
            // todo do something else
            String s = "List of daily returns for branch: !!!!";
            //this.displayReport(s);
        }
    }
}