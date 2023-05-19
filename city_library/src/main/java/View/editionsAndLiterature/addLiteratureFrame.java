package View.editionsAndLiterature;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class addLiteratureFrame extends JFrame implements ActionListener {

    public static JTextField titleField, authorField, yearField;
    private static JComboBox<String> categoryBox;
    private JButton addButton;
    private static JButton backButton;
    public static String category;

    public void addLiteratureFrame() throws SQLException {

        JLabel categoryLabel = new JLabel("Категория:", SwingConstants.CENTER);
        JLabel titleLabel = new JLabel("Название:", SwingConstants.CENTER);
        JLabel authorLabel = new JLabel("Автор(иоф):\n", SwingConstants.CENTER);
        JLabel yearLabel = new JLabel("Год:", SwingConstants.CENTER);

        titleField = new JTextField();
        authorField = new JTextField();
        yearField = new JTextField();

        categoryBox = new JComboBox<>(ConnectToOracle.getLiteratureCategory());
        categoryBox.setSelectedIndex(0);

        addButton = new JButton("Добавить");
        addButton.addActionListener(this);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5,5));
        panel.add(categoryLabel);
        panel.add(categoryBox);
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(yearLabel);
        panel.add(yearField);
        panel.add(addButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Добавить произведение");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            category = (String)categoryBox.getSelectedItem();
            insertLiteratureStart();
        } else if (e.getSource() == backButton) {
            try {
                MainStart.StartMainMenu();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            dispose();
        }
    }
    private void insertLiteratureStart() {
        try {
            if (ConnectToOracle.insertLiterature() == -1) {
                JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
            } else {
                JOptionPane.showMessageDialog(null, "Произведение добавлено!");
                MainStart.StartMainMenu();
                dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
