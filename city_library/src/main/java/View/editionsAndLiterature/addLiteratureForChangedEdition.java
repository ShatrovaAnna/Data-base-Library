package View.editionsAndLiterature;

import Controller.EditionLiteratureController;
import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class addLiteratureForChangedEdition extends JFrame {

    private JLabel categoryLabel, itemLabel;
    public static JComboBox<String> categoryComboBox;
    public static JComboBox<String> itemComboBox;
    private static JButton backButton2;
    private static JButton nextButton;
    public static String category;

    public addLiteratureForChangedEdition() throws SQLException {
        setSize(1000, 300);
        setTitle("Добавление произведений в издание");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 5, 5));

        categoryLabel = new JLabel("Категория произведения:", SwingConstants.CENTER);
        add(categoryLabel);
        categoryComboBox = new JComboBox<>(ConnectToOracle.getLiteratureCategory());
        categoryComboBox.setSelectedIndex(0);
        add(categoryComboBox);

        itemLabel = new JLabel("Произведение: ", SwingConstants.CENTER);
        add(itemLabel);
        itemComboBox = new JComboBox<>();
        add(itemComboBox);

        backButton2 = new JButton("Завершить добавление");
        add(backButton2);

        nextButton = new JButton("Добавить далее");
        add(nextButton);

        setVisible(true);
    }

    private void fillItemComboBox() throws SQLException {
        itemComboBox.removeAllItems();
        ConnectToOracle.getLiteratureByCategoryWithParametr(category, itemComboBox);
        itemComboBox.setSelectedIndex(0);
    }

    public static void addLiteratureFrame() throws SQLException {
        addLiteratureForChangedEdition categoryPage = new addLiteratureForChangedEdition();
        categoryComboBox.addActionListener(e -> {
            category = (String) categoryComboBox.getSelectedItem();
            try {
                categoryPage.fillItemComboBox();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        nextButton.addActionListener(e -> {
            int id = (int) itemComboBox.getClientProperty(itemComboBox.getSelectedItem());
            try {
                if(ConnectToOracle.insertEditionLiteratureWithEditionID(changeEdition.EditionID, id) == -1){
                    JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Добавлено!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton2.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Добавление произведений к изданию завершено!\nИнформция об издании изменена!");
            categoryPage.dispose();
            EditionLiteratureController.frameChangeEdition.dispose();
            try {
                MainStart.StartMainMenu();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
