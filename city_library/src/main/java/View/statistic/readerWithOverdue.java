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

public class readerWithOverdue extends JFrame implements ActionListener {

    private static JTextArea resultArea;
    private static JButton backButton;
    private static ResultSet result;
    private static JTable table;

    public readerWithOverdue() {

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);
        add(backButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Читатели с просроченной литературой");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            dispose();
            statisticMenu.startStatisticMenu();
        }
    }

    private static void createTable() throws SQLException {

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Читатель");
        tableModel.addColumn("Издание");
        tableModel.addColumn("Библиотека");
        tableModel.addColumn("Дата предполагаемого возврата");

        tableModel.setRowCount(0);
        while (result.next()) {
            Object[] row = new Object[4];
            for (int i = 1; i <= 4; i++) {
                row[i - 1] = result.getObject(i);
            }
            tableModel.addRow(row);
        }
        table.setModel(tableModel);

        JScrollPane scrollPane = (JScrollPane) resultArea.getParent();
        scrollPane.setViewportView(table);
    }
    public static void startReaderWithOverdue() throws SQLException {
        new readerWithOverdue();
        result = ConnectToOracle.getListOfReaderWithOverdue();
        createTable();
    }
}
