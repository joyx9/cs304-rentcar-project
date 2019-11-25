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

public class GenerateReportTable extends JFrame{
    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private JFrame frame;
    private GridBagConstraints gbc;
    private ArrayList<String[]> repStr;

    GenerateReportTable(ArrayList<String[]> rep) {
        repStr = rep;
        frame = new JFrame();
        frame.setTitle("Rent-A-Car");
        setupDisplay(frame.getContentPane());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupDisplay(Container pane) {
        pane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        // headers for the table
        int colCount = repStr.get(0).length;
        String[] columns = new String[] {};
        Object[][] data = new Object[][] {};
        // create table with data
        JTable report = new JTable(data, columns);
        JScrollPane jScrollPane = new JScrollPane(report);
        DefaultTableModel dtm = new DefaultTableModel(0, colCount);
        report.setModel(dtm);
        // for each row in repStr EXCEPT the last row, add a row to the table
        // for the last row, add it to a jlabel
        String totNum = "Total # of cars rented: ";
        for (int i = 0; i < repStr.size(); i++){
            if (i == repStr.size() -1 ){
                totNum += repStr.get(i)[0];
            } else {
                Object[] obj = repStr.get(i);
                dtm.addRow(obj);
            }
        }
        frame.getContentPane().add(jScrollPane,
                new GridBagConstraints(0, RELATIVE, 1, 1, 1.0,
                        1.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
                        new Insets(5, 5, 5, 5), 0, 0));

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
    }
}