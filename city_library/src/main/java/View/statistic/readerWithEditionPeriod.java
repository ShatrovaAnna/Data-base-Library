package View.statistic;

import DBase.ConnectToOracle;
import View.statisticMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class readerWithEditionPeriod extends JFrame implements ActionListener {

    private JTextField searchField, data1Field, data2Field;
    private JTextArea resultArea;
    private static JButton backButton, searchButton;
    public static String searchText;
    public static LocalDate data1;
    public static LocalDate data2;
    private ResultSet result;
    private JTable table;
    private JCheckBox libraryCheckBox;

    public readerWithEditionPeriod() throws SQLException {

        JLabel searchLabel = new JLabel("Введите ФИО читателя:");
        searchField = new JTextField(50);
        searchButton = new JButton("Поиск");
        searchButton.addActionListener(this);

        JPanel dataPanel = new JPanel(new GridLayout(1,3));
        JPanel dataPanel1 = new JPanel();
        JLabel data1Label = new JLabel("Введите начальную дату (дд.мм.гг): ");
        data1Field = new JTextField(15);
        dataPanel1.add(data1Label);
        dataPanel1.add(data1Field);

        JPanel dataPanel2 = new JPanel();
        JLabel data2Label = new JLabel("Введите конечную дату (дд.мм.гг): ");
        data2Field = new JTextField(15);
        dataPanel2.add(data2Label);
        dataPanel2.add(data2Field);

        dataPanel.add(dataPanel1);
        dataPanel.add(dataPanel2);

        libraryCheckBox = new JCheckBox("В библиотеке, где он зарегистрирован ");
        libraryCheckBox.addActionListener(this);
        dataPanel.add(libraryCheckBox);

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);


        JPanel topPanel = new JPanel();
        topPanel.add(searchPanel);
        topPanel.add(dataPanel);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);
        add(backButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Статистика по читателям и изданиям за определенный период");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
            searchText = searchField.getText();

            int t = 0;
            if (libraryCheckBox.isSelected()){
                t = 1;
            }
            else {
                t = 0;
            }
            try{
                data1 = LocalDate.parse(data1Field.getText(), formatter);
                data2 = LocalDate.parse(data2Field.getText(), formatter);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Проверьте, что заполнили все поля правильно!");
                ex.printStackTrace();
            }
            try {
                result = ConnectToOracle.getListOfReaderWithEditionPeriod(t, searchText, data1, data2);
                createTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Проверьте, что заполнили все поля правильно!");
                ex.printStackTrace();
            }
        }
        if (e.getSource() == backButton) {
            statisticMenu.startStatisticMenu();
            dispose();
        }
    }

    private void createTable() throws SQLException {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Издание");
        tableModel.addColumn("Категория издания");
        tableModel.addColumn("Библиотека");

        tableModel.setRowCount(0);
        while (result.next()) {
            Object[] row = new Object[3];
            for (int i = 1; i <= 3; i++) {
                row[i - 1] = result.getObject(i);
            }
            tableModel.addRow(row);
        }
        table.setModel(tableModel);

        JScrollPane scrollPane = (JScrollPane) resultArea.getParent();
        scrollPane.setViewportView(table);
    }
}
