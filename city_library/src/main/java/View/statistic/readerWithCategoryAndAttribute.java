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

public class readerWithCategoryAndAttribute extends JFrame implements ActionListener {

    private JCheckBox libraryCheckBox;
    private JTextArea resultArea;
    private static JButton backButton, categoryReaderButton, categoryAttributeButton, searchButton;
    public static String categoryReader, categoryAttribute, valueAttribute;
    private ResultSet result;
    private JTable table;
    public JComboBox<String> categoryReaderComboBox, categoryAttributeComboBox, AttributeValueComboBox;
    private int library;

    public readerWithCategoryAndAttribute() throws SQLException {

        JLabel categoryReaderLabel = new JLabel("Категория читателей: ", SwingConstants.CENTER);
        categoryReaderComboBox = new JComboBox<>(ConnectToOracle.getReaderCategories());
        categoryReaderComboBox.setSelectedIndex(0);
        categoryReaderButton = new JButton("Далее");
        categoryReaderButton.addActionListener(this);

        JLabel categoryAttributeLabel = new JLabel("Выбор характеристики: ", SwingConstants.CENTER);
        categoryAttributeComboBox = new JComboBox<>();
        categoryAttributeButton = new JButton("Далее");
        categoryAttributeButton.addActionListener(this);

        JLabel AttributeValueLabel = new JLabel("Выбор подхарактеристики: ", SwingConstants.CENTER);
        AttributeValueComboBox = new JComboBox<>();
        searchButton = new JButton("Найти");
        searchButton.addActionListener(this);

        JPanel categoryPanel = new JPanel(new GridLayout(3,3));
        categoryPanel.add(categoryReaderLabel);
        categoryPanel.add(categoryReaderComboBox);
        categoryPanel.add(categoryReaderButton);
        categoryPanel.add(categoryAttributeLabel);
        categoryPanel.add(categoryAttributeComboBox);
        categoryPanel.add(categoryAttributeButton);
        categoryPanel.add(AttributeValueLabel);
        categoryPanel.add(AttributeValueComboBox);
        categoryPanel.add(searchButton);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        libraryCheckBox = new JCheckBox("Поиск во всех библиотеках");
        libraryCheckBox.addActionListener(this);
        libraryCheckBox.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(libraryCheckBox, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(categoryPanel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Статистика по читателям с заданными характеристиками");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == categoryReaderButton) {
            categoryReader = (String)categoryReaderComboBox.getSelectedItem();
            try {
                fillCategoryNameComboBox();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Проверьте, что заполнили все поля правильно!");
                ex.printStackTrace();
            }
        }
        if (e.getSource() == categoryAttributeButton) {
            categoryAttribute = (String)categoryAttributeComboBox.getSelectedItem();
            try {
                fillAttributeValueComboBox();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Проверьте, что заполнили все поля правильно!");
                ex.printStackTrace();
            }
        }
        if (e.getSource() == searchButton) {
            valueAttribute = (String)AttributeValueComboBox.getSelectedItem();
            try {
                result = ConnectToOracle.getListOfReaderByCategoryAndAttribute(categoryReader, categoryAttribute, valueAttribute, library);
                createTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Проверьте, что заполнили все поля правильно!");
                ex.printStackTrace();
            }
        }
        if(libraryCheckBox.isSelected()){
            library = 1;
        } else{
            library = 0;
        }
        if (e.getSource() == backButton) {
            statisticMenu.startStatisticMenu();
            dispose();
        }
    }

    private void fillCategoryNameComboBox() throws SQLException {
        categoryAttributeComboBox.removeAllItems();
        AttributeValueComboBox.removeAllItems();
        ConnectToOracle.getAttributeByReaderCategory(categoryAttributeComboBox,categoryReader);
        categoryAttributeComboBox.setSelectedIndex(0);
    }
    private void fillAttributeValueComboBox() throws SQLException {
        AttributeValueComboBox.removeAllItems();
        ConnectToOracle.getAttributeValueFromReader(AttributeValueComboBox,categoryAttribute);
        AttributeValueComboBox.setSelectedIndex(0);
    }

    private void createTable() throws SQLException {

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Читатель");
        tableModel.addColumn("Характеристика");
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