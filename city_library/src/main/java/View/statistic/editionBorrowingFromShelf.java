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

public class editionBorrowingFromShelf extends JFrame implements ActionListener {

    private JTextField searchField;
    private JTextArea resultArea;
    private static JButton backButton, searchButton;
    public static String searchText;
    private ResultSet result;
    private JTable table;

    public editionBorrowingFromShelf() {

        JLabel searchLabel = new JLabel("Введите номер полки:");
        searchField = new JTextField(40);
        searchButton = new JButton("Поиск");
        searchButton.addActionListener(this);
        add(searchButton);

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.setLayout(new FlowLayout());

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);
        add(backButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(searchPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Статистика по выданной литературе с полки");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchText = searchField.getText();
            try {
                result = ConnectToOracle.getListOfEditionBorrowingFromShelf();
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
