package View.booksearch;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkAvailabilityFrame extends JFrame implements ActionListener {

    private JCheckBox byTitleCheckBox;
    private JCheckBox byAuthorCheckBox;
    private JCheckBox byPublisherCheckBox;
    private JCheckBox checkIssuedCheckBox, libraryCheckBox;
    private JTextField searchField;
    private JTextArea resultArea;
    private static JButton backButton, searchButton;
    public static String searchText;
    public int categorySQL;
    public static int categoryBorrowing, libraryBorrowing;
    private ResultSet result;
    private JTable table;

    public checkAvailabilityFrame() {

        byTitleCheckBox = new JCheckBox("Поиск по произведению");
        byAuthorCheckBox = new JCheckBox("Поиск по автору");
        byPublisherCheckBox = new JCheckBox("Поиск по изданию");

        byTitleCheckBox.addActionListener(this);
        byAuthorCheckBox.addActionListener(this);
        byPublisherCheckBox.addActionListener(this);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(byTitleCheckBox);
        buttonGroup.add(byAuthorCheckBox);
        buttonGroup.add(byPublisherCheckBox);

        byPublisherCheckBox.setSelected(true);

        checkIssuedCheckBox = new JCheckBox("Проверить выданные");
        checkIssuedCheckBox.addActionListener(this);
        libraryCheckBox = new JCheckBox("Проверить во всех библиотеках");
        libraryCheckBox.addActionListener(this);

        searchField = new JTextField(60);
        searchButton = new JButton("Поиск");
        searchButton.addActionListener(this);
        add(searchButton);

        JPanel checkBoxPanel = new JPanel(new GridLayout(1, 3));
        checkBoxPanel.add(byTitleCheckBox);
        checkBoxPanel.add(byAuthorCheckBox);
        checkBoxPanel.add(byPublisherCheckBox);

        JPanel searchPanel = new JPanel();
        searchPanel.add(checkIssuedCheckBox);
        searchPanel.add(libraryCheckBox);
        searchPanel.add(new JLabel("Введите поисковый запрос:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        /*resultArea = new JTextArea(35,40);
        resultArea.setEditable(false);*/


        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        backButton = new JButton("Назад");
        add(backButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(checkBoxPanel, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Поиск книг");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == byTitleCheckBox) {
            categorySQL = 1;
        } else if (e.getSource() == byAuthorCheckBox) {
            categorySQL = 2;
        } else if (e.getSource() == byPublisherCheckBox) {
            categorySQL = 3;
        }
        if (checkIssuedCheckBox.isSelected()) {
            categoryBorrowing = 1;
        }
        else{
            categoryBorrowing = 0;
        }
        if(libraryCheckBox.isSelected()){
            libraryBorrowing = 1;
        } else{
            libraryBorrowing = 0;
        }

        if (e.getSource() == searchButton) {
            searchText = searchField.getText();
            try {
                result = ConnectToOracle.getListOfEdition(categorySQL, categoryBorrowing, libraryBorrowing);
                createTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createTable() throws SQLException {

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Категория");
        tableModel.addColumn("Название");
        tableModel.addColumn("Номер зала");
        tableModel.addColumn("Номер стенда");
        tableModel.addColumn("Номер полки");
        tableModel.addColumn("Библиотека");

        tableModel.setRowCount(0);
        while (result.next()) {
            Object[] row = new Object[7];
            for (int i = 1; i <= 7; i++) {
                if(i == 2){
                    row[i - 1] = ConnectToOracle.getEditionCategoryIDSearch(result.getObject(i));
                }
                else{
                    row[i - 1] = result.getObject(i);
                }
            }
            tableModel.addRow(row);
        }
        table.setModel(tableModel);

        JScrollPane scrollPane = (JScrollPane) resultArea.getParent();
        scrollPane.setViewportView(table);
    }

    public static void checkAvailabilityStart() {
        checkAvailabilityFrame page = new checkAvailabilityFrame();
        categoryBorrowing = 0;
        backButton.addActionListener(e -> {
            try {
                MainStart.StartMainMenu();
                page.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
