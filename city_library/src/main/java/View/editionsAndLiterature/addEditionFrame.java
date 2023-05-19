package View.editionsAndLiterature;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class addEditionFrame extends JFrame implements ActionListener {

    public static JComboBox<String> categoryEdComboBox;
    public static JComboBox<String> ruleComboBox, litComboBox;
    public static JTextField shelfNumberField;
    public static JTextField nameField;
    private static JButton addButton;
    private static JButton backButton;
    public static String rule;
    public static String category;

    public void addEditionFrame() throws SQLException {

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        JLabel categoryLabel = new JLabel("Категория издания:", SwingConstants.CENTER);
        panel.add(categoryLabel);

        categoryEdComboBox = new JComboBox<>(ConnectToOracle.getEditionCategory());
        categoryEdComboBox.setSelectedIndex(0);
        panel.add(categoryEdComboBox);

        JLabel shelfNumberLabel = new JLabel("Номер полки:", SwingConstants.CENTER);
        panel.add(shelfNumberLabel);
        shelfNumberField = new JTextField();
        panel.add(shelfNumberField);

        JLabel nameLabel = new JLabel("Название издания:", SwingConstants.CENTER);
        panel.add(nameLabel);
        nameField = new JTextField();
        panel.add(nameField);

        JLabel ruleLabel = new JLabel("Правила выдачи и использования:", SwingConstants.CENTER);
        panel.add(ruleLabel);
        ruleComboBox = new JComboBox<>(ConnectToOracle.getRuleCategory());
        ruleComboBox.setSelectedIndex(0);
        panel.add(ruleComboBox);

        addButton = new JButton("Добавить произведения");
        addButton.addActionListener(this);
        panel.add(addButton);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Добавить издание");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            category = (String)categoryEdComboBox.getSelectedItem();
            rule = (String) ruleComboBox.getSelectedItem();
            try {
                insertEditionStart();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == backButton) {
            try {
                MainStart.StartMainMenu();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            dispose();
        }
    }
    private void insertEditionStart() throws SQLException {
        if (ConnectToOracle.insertEdition() == -1) {
            JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
        } else {
            CategoryPage.addLiteratureFrame();
            dispose();
        }
    }
}


