package View.editionsAndLiterature;

import Controller.EditionLiteratureController;
import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class changeEdition extends JFrame implements ActionListener {

    public static JComboBox<String> categoryEdComboBox;
    public static JComboBox<String> ruleComboBox;
    public static JTextField shelfNumberField;
    public static JTextField nameField, idField;
    private static JButton addButton;
    private static JButton backButton, searchButton;
    private JCheckBox outCheckBox, addLitCheckBox, deleteLitCheckBox;
    public static String rule;
    public static String category;
    private JPanel mainPanel, panel;
    public static int EditionID;
    private int outCheck, litChecker;
    public  int shelfNumber;
    public  String editionName;


    public changeEdition() throws SQLException {

        JPanel idPanel = new JPanel();
        searchButton = new JButton("Поиск");
        searchButton.addActionListener(this);
        JLabel idLabel = new JLabel("Введите ID издания:");
        idField = new JTextField(15);
        idPanel.add(idLabel);
        idPanel.add(idField);
        idPanel.add(searchButton);

        panel = new JPanel(new GridLayout(6, 2, 5, 5));

        JLabel categoryLabel = new JLabel("Категория издания:", SwingConstants.CENTER);
        panel.add(categoryLabel);

        categoryEdComboBox = new JComboBox<>(ConnectToOracle.getEditionCategory());
        //categoryEdComboBox.setSelectedIndex(0);
        panel.add(categoryEdComboBox);

        JLabel shelfNumberLabel = new JLabel("Номер полки:", SwingConstants.CENTER);
        panel.add(shelfNumberLabel);
        shelfNumberField = new JTextField();
        shelfNumberField.setEditable(true);
        panel.add(shelfNumberField);

        JLabel nameLabel = new JLabel("Название издания:", SwingConstants.CENTER);
        panel.add(nameLabel);
        nameField = new JTextField();
        nameField.setEditable(true);
        panel.add(nameField);

        JLabel ruleLabel = new JLabel("Правила выдачи и использования:", SwingConstants.CENTER);
        panel.add(ruleLabel);
        ruleComboBox = new JComboBox<>(ConnectToOracle.getRuleCategory());
        //ruleComboBox.setSelectedIndex(0);
        panel.add(ruleComboBox);

        deleteLitCheckBox = new JCheckBox("Удалить произведения");
        deleteLitCheckBox.addActionListener(this);
        deleteLitCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(deleteLitCheckBox);

        addLitCheckBox = new JCheckBox("Добавить произведение");
        addLitCheckBox.addActionListener(this);
        addLitCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(addLitCheckBox);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(deleteLitCheckBox);
        buttonGroup.add(addLitCheckBox);

        outCheckBox = new JCheckBox("Списать");
        outCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        outCheckBox.addActionListener(this);
        panel.add(outCheckBox);

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
            EditionID = Integer.parseInt(idField.getText());

            ResultSet res = ConnectToOracle.getEditionInfo();
            while (true){
                try {
                    if (!res.next()){
                        JOptionPane.showMessageDialog(null, "Издание не найдено!\nПроверьте еще раз данные!");
                        break;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    categoryEdComboBox.setSelectedItem(ConnectToOracle.getEditionCategoryIDSearch(res.getInt("category_ed_id")));
                    shelfNumber = res.getInt("bookshelf_id");
                    editionName = res.getString("name");
                    ruleComboBox.setSelectedItem(ConnectToOracle.getRuleCategoryNameByID(res.getInt("rule_id")));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                shelfNumberField.setText(String.valueOf(shelfNumber));
                if(shelfNumber == 0){
                    JOptionPane.showMessageDialog(null, "Это издание списано!");
                    try {
                        MainStart.StartMainMenu();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    dispose();
                }
                nameField.setText(editionName);
                mainPanel.add(panel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            }
        }
        if (outCheckBox.isSelected()) {
            outCheck = 1;
        }
        else{
            outCheck = 0;
        }

        if (addLitCheckBox.isSelected()) {
            litChecker = 2;
        } else if (deleteLitCheckBox.isSelected()){
            litChecker = 1;
        } else {
            litChecker = 0;
        }

        if (e.getSource() == addButton) {
            category = (String)categoryEdComboBox.getSelectedItem();
            rule = (String) ruleComboBox.getSelectedItem();
            try {
                updateEditionStart();
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
    private void updateEditionStart() throws SQLException {
        if (ConnectToOracle.updateEdition(outCheck) == -1) {
            JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
        } else {
            if (litChecker == 2){
                addLiteratureForChangedEdition.addLiteratureFrame();
            } else if (litChecker == 1){
                deleteLiteratureFomChangedEdition.deleteLiteratureFrame();
            } else{
                JOptionPane.showMessageDialog(null, "Информация об издании изменена!");
                dispose();
                try {
                    MainStart.StartMainMenu();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
