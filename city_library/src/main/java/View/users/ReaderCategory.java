package View.users;

import Controller.MainStart;
import Controller.PeopleController;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ReaderCategory extends JFrame implements ActionListener {

    private JComboBox<String> comboBox;
    private JButton nextButton, backButton;
    public static int CategoryID;
    private static String CategoryName;


    public void ReaderCategory(String[] categories){

        comboBox = new JComboBox<>(categories);
        comboBox.addActionListener(this);
        comboBox.setSelectedIndex(0);
        comboBox.setPreferredSize(new Dimension(260, 20));
        nextButton = new JButton("Далее");
        nextButton.addActionListener(this);

        JPanel firstRow = new JPanel();
        firstRow.setLayout(new FlowLayout());
        JLabel label = new JLabel("Выберете категорию читателя:");
        firstRow.add(label);
        firstRow.add(comboBox);
        firstRow.add(nextButton);
        firstRow.setPreferredSize(new Dimension(700, 300));

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        JPanel fix = new JPanel();
        fix.setPreferredSize(new Dimension(500, 300));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(fix, BorderLayout.NORTH);
        mainPanel.add(firstRow, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Выберите категорию читателя");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == comboBox) {
            CategoryName = (String) comboBox.getSelectedItem();
            try {
                CategoryID = ConnectToOracle.getReaderCategoryID(CategoryName);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == nextButton) {
            try {
                PeopleController.addReader();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            dispose();
        } else if (e.getSource() == backButton) {
            try {
                MainStart.StartMainMenu();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            dispose();
        }
    }

    public static int getSelectedReaderCategoryID(){
        return CategoryID;
    }
    public static String getSelectedReaderCategoryName(){
        return CategoryName;
    }
}
