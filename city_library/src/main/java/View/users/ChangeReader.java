package View.users;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangeReader extends JFrame implements ActionListener {

    public static JComboBox<String> categoryEdComboBox, libraryComboBox;
    public static JTextField nameField, searchField, authorField, searchAuthorField;
    private static JButton addButton;
    private static JButton backButton, searchButton, changeCategoryButton;
    public static String name, searchText;
    public int ReaderID;
    private JPanel mainPanel, firstPanel, attributePanel, categoryPanel, namePanel;
    private JCheckBox disableCheckBox;
    private int disableChecker;
    private JTextField[] fields;
    public String[] firstAttributes;



    public ChangeReader() throws SQLException {

        JPanel idPanel = new JPanel();
        searchButton = new JButton("Поиск");
        searchButton.addActionListener(this);
        JLabel searchLabel = new JLabel("Введите ID читателя:");
        searchField = new JTextField(20);
        idPanel.add(searchLabel);
        idPanel.add(searchField);
        idPanel.add(searchButton);

        namePanel = new JPanel(new GridLayout(2,2, 5, 5));
        categoryPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        firstPanel = new JPanel(new GridLayout(2,1, 5, 5));


        JLabel nameLabel = new JLabel("ФИО:", SwingConstants.CENTER);
        namePanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setEditable(true);
        namePanel.add(nameField);

        JLabel libraryLabel = new JLabel("Библиотека: ", SwingConstants.CENTER);
        namePanel.add(libraryLabel);
        libraryComboBox = new JComboBox<>(ConnectToOracle.getLibrariesName());
        namePanel.add(libraryComboBox);

        JLabel categoryLabel = new JLabel("Категория читателя:", SwingConstants.CENTER);
        categoryPanel.add(categoryLabel);
        categoryEdComboBox = new JComboBox<>(ConnectToOracle.getReaderCategories());
        categoryPanel.add(categoryEdComboBox);
        changeCategoryButton = new JButton("Подтвердить смену категории");
        categoryPanel.add(new JPanel());
        changeCategoryButton.addActionListener(this);
        categoryPanel.add(changeCategoryButton);

        firstPanel.add(namePanel);
        firstPanel.add(categoryPanel);

        attributePanel = new JPanel();

        disableCheckBox = new JCheckBox("Деактивировать читателя");
        disableCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        disableCheckBox.addActionListener(this);

        addButton = new JButton("Сохранить изменения");
        addButton.addActionListener(this);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        JPanel tempPanel = new JPanel(new GridLayout(2,1));
        tempPanel.add(firstPanel);
        tempPanel.add(attributePanel);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(idPanel, BorderLayout.NORTH);
        mainPanel.add(tempPanel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Изменить читателя");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == disableCheckBox){
            if(!disableCheckBox.isSelected()){
            } else {
                JOptionPane.showMessageDialog(null, "Данные о категории пользователя будут удалены!\nПользователь будет деактивирован!");
            }
        }
        if(e.getSource() == changeCategoryButton){
            JOptionPane.showMessageDialog(null, "При подтверждении смены категории, вы не сможете редактировать старые характеристики!\n" +
                    "Чтобы после продолжить редактировать прошлую категорию, нажмите кнопку \"поиск\" еще раз.");
        }
        if(disableCheckBox.isSelected()){
            disableChecker = 1;
        } else {
            disableChecker = 0;
        }
        if (e.getSource() == searchButton) {
            searchText = searchField.getText();
            try{
                ReaderID = Integer.parseInt(searchText);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Проверьте еще раз, что ввели число!");
                ex.printStackTrace();
            }
            try {
                fillData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == changeCategoryButton) {
            try {
                addCategoryPanel();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == addButton) {
            try {
                ConnectToOracle.updateReader(ReaderID, nameField.getText(), (String) libraryComboBox.getSelectedItem(), (String) categoryEdComboBox.getSelectedItem(), firstAttributes, fields, disableChecker);
                JOptionPane.showMessageDialog(null, "Данные пользователя изменены!");
                MainStart.StartMainMenu();
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
                ex.printStackTrace();
            }
        }
        if (e.getSource() == backButton) {
            try {
                MainStart.StartMainMenu();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            dispose();
        }
    }

    private void fillData() throws SQLException {
        ResultSet res1 = ConnectToOracle.getReaderInfo(ReaderID);
        while (true){
            try {
                if (!res1.next()){
                    JOptionPane.showMessageDialog(null, "Пользователь не найден!\nПроверьте еще раз данные!");
                    break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                nameField.setText(res1.getString(1));
                categoryEdComboBox.setSelectedItem(res1.getString(2));
                libraryComboBox.setSelectedItem(res1.getString(3));
                if(res1.getInt(4) == 1){
                    disableCheckBox.setSelected(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            addCategoryPanel();
            break;
        }
    }
    private void addCategoryPanel() throws SQLException {
        attributePanel.removeAll();
        firstAttributes = ConnectToOracle.getAttributeReaderByName((String) categoryEdComboBox.getSelectedItem());
        attributePanel.setLayout(new GridLayout(firstAttributes.length + 1, 2));
        fields = new JTextField[firstAttributes.length];
        for (int i = 0; i < firstAttributes.length; i++) {
            JLabel categoryLabel = new JLabel(firstAttributes[i], SwingConstants.CENTER);
            categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            fields[i] = new JTextField();
            attributePanel.add(categoryLabel);
            attributePanel.add(fields[i]);
        }
        for (int i = 0; i < firstAttributes.length; i++) {
            fields[i].setText(ConnectToOracle.getAttributeValueFromReaderByAttributeName(firstAttributes[i], ReaderID));
        }
        attributePanel.add(disableCheckBox);
        attributePanel.add(addButton);
        attributePanel.revalidate();
        attributePanel.repaint();
    }
}

