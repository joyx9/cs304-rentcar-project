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

public class ViewVehiclesDisplay extends JFrame implements ActionListener {
    private static Font defaultFont = new Font("Courier New", Font.PLAIN, 25);
    private static int width = 1000;
    private static int height = 800;
    private TerminalTransactionsDelegate delegate;
    private JFrame frame;
    private GridBagConstraints gbc;
    private JLabel label;
    private JButton button;
    private JButton showavbutton;
    private JTextArea vehicleText;
    private MainDisplay mainDisplay;
    private JTextField carTypetf;
    private JTextField locationtf;
    private JTextField timetf;
    private JTable report;
    private ArrayList<String[]> vehicles;

    ViewVehiclesDisplay(MainDisplay md, TerminalTransactionsDelegate delegate){
        this.delegate = delegate;
        frame = new JFrame();
        frame.setTitle("Rent-A-Car View Vehicles");
        mainDisplay = md;
        setupDisplay(frame.getContentPane());
        frame.pack();
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
        showavbutton = new JButton("show available vehicles");
        showavbutton.setFont(defaultFont);
        showavbutton.addActionListener(this);
        showavbutton.setActionCommand("showPressed");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        pane.add(showavbutton, gbc);

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

    private void showNumVehicles(String num){
        // adds "num available vehicles" button
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

    private void showVehicleDetails(ArrayList<String[]> str){
        // remove some buttons
        frame.getContentPane().remove(showavbutton);
        // shows details in a table todo
        // headers for the table
        String[] columns = new String[] {
                "Location", "Make", "Model", "License", "City", "Color", "Status"
        };
        Object[][] data = new Object[][] {};
        // create table with data
        JTable report = new JTable(data, columns);
        JScrollPane jScrollPane = new JScrollPane(report);
        DefaultTableModel dtm = new DefaultTableModel(0, 7);
        report.setModel(dtm);
        // for each vehicle, add each string in the array to the appropriate column
        for (int i = 0; i < vehicles.size(); i++){
            dtm.addRow(new Object[]{
                   vehicles.get(i)[0], vehicles.get(i)[1], vehicles.get(i)[2],
                    vehicles.get(i)[3], vehicles.get(i)[4], vehicles.get(i)[5], vehicles.get(i)[6]
            });
            System.out.println(vehicles.get(i)[0]);
        }
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = RELATIVE;
        frame.getContentPane().add(jScrollPane,
                new GridBagConstraints(0, RELATIVE, 1, 1, 1.0,
                        1.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
                        new Insets(5, 5, 5, 5), 0, 0));
        frame.revalidate();
        frame.repaint();
//        report = new JTextArea();
//        report.setFont(defaultFont);
//        report.setLineWrap(true);
//        report.setWrapStyleWord(true);
//        report.setText("");
//        for (String s: str) {
//            report.append(s);
//        }
//        report.setEditable(false);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "showPressed"){
            // todo get the vehicle details using vtname and location, then save vtnumber
            vehicles = delegate.getAllVehicles(carTypetf.getText(), locationtf.getText());
//            String[] str = new String[]{"a", "b", "c", "d", "e", "f", "g"};
//            vehicles = new ArrayList<>();
//            vehicles.add(str);
            this.showNumVehicles(Integer.toString(vehicles.size()));
            System.out.println("cartype_tf:" + carTypetf.getText() + "_location_tf:" + locationtf.getText());
        } else if (e.getActionCommand() == "backPressed"){
            mainDisplay.returnFromDisplay();
            frame.setVisible(false);
        } else if (e.getActionCommand() == "detailsPressed"){
            // new ViewVehicleDetailDisplay(mainDisplay);
            this.showVehicleDetails(vehicles);
        }
    }
}
