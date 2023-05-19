package View.editionsAndLiterature;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class changeLiterature extends JFrame implements ActionListener {

    public static JComboBox<String> categoryEdComboBox;
    public static JTextField yearField;
    public static JTextField nameField, searchNameField, authorField, searchAuthorField;
    private static JButton addButton;
    private static JButton backButton, searchButton;
    public static String category, literatureName, author,  name;
    private JPanel mainPanel, panel;
    public static int LiteratureID, year;


    public changeLiterature() throws SQLException {

        JPanel idPanel = new JPanel();
        searchButton = new JButton("Поиск");
        searchButton.addActionListener(this);
        JLabel searchNameLabel = new JLabel("Введите название произведения:");
        searchNameField = new JTextField(20);
        JLabel searchAuthorLabel = new JLabel("Введите автора(иоф):");
        searchAuthorField = new JTextField(15);
        idPanel.add(searchNameLabel);
        idPanel.add(searchNameField);
        idPanel.add(searchAuthorLabel);
        idPanel.add(searchAuthorField);
        idPanel.add(searchButton);

        panel = new JPanel(new GridLayout(5, 2, 5,5));

        JLabel categoryLabel = new JLabel("Категория произведения:", SwingConstants.CENTER);
        panel.add(categoryLabel);

        categoryEdComboBox = new JComboBox<>(ConnectToOracle.getLiteratureCategory());
        panel.add(categoryEdComboBox);

        JLabel nameLabel = new JLabel("Название произведения:", SwingConstants.CENTER);
        panel.add(nameLabel);
        nameField = new JTextField();
        nameField.setEditable(true);
        panel.add(nameField);

        JLabel authorLabel = new JLabel("Автор:", SwingConstants.CENTER);
        panel.add(authorLabel);
        authorField = new JTextField();
        authorField.setEditable(true);
        panel.add(authorField);

        JLabel yearLabel = new JLabel("Год издания:", SwingConstants.CENTER);
        panel.add(yearLabel);
        yearField = new JTextField();
        yearField.setEditable(true);
        panel.add(yearField);

        addButton = new JButton("Сохранить");
        addButton.addActionListener(this);
        panel.add(addButton);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(idPanel, BorderLayout.NORTH);
        //mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Изменить издание");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            literatureName = searchNameField.getText();
            author = searchAuthorField.getText();
            try{
                LiteratureID = ConnectToOracle.getLiteratureID(literatureName, author);
                if(LiteratureID == 0){
                    JOptionPane.showMessageDialog(null, "Произведение не найдено!\nПроверьте еще раз данные!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Произведение не найдено!\nПроверьте еще раз данные!");
                ex.printStackTrace();
            }
            ResultSet res = ConnectToOracle.getLiteratureInfo(LiteratureID);
            while (true){
                try {
                    if (!res.next()){
                        break;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    categoryEdComboBox.setSelectedItem(ConnectToOracle.getLiteratureCategoryByID(res.getInt(1)));
                    year = res.getInt(2);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                nameField.setText(literatureName);
                authorField.setText(author);
                yearField.setText(String.valueOf(year));
                mainPanel.add(panel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            }
        }

        if (e.getSource() == addButton) {
            category = (String)categoryEdComboBox.getSelectedItem();
            try {
                updateLiteratureStart();
            } catch (SQLException ex) {
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
    private void updateLiteratureStart() throws SQLException {
        if (ConnectToOracle.updateLiterature(LiteratureID) == -1) {
            JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
        } else {
            JOptionPane.showMessageDialog(null, "Информция о произведении изменена!");
            MainStart.StartMainMenu();
            dispose();
        }
    }
}
