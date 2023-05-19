package View.editionsAndLiterature;

import Controller.EditionLiteratureController;
import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class deleteLiteratureFomChangedEdition extends JFrame {

    private JLabel categoryLabel;
    public static JComboBox<String> categoryComboBox;
    private static JButton backButton2;
    private static JButton nextButton;
    public static String category;

    public deleteLiteratureFomChangedEdition() throws SQLException {
        setSize(1000, 300);
        setTitle("Удаление произведений из издание");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2, 5, 5));

        categoryLabel = new JLabel("Выберете произведение для удаления:", SwingConstants.CENTER);
        add(categoryLabel);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.removeAllItems();
        ConnectToOracle.getListOfLiteratureForDelete(changeEdition.EditionID, categoryComboBox);
        categoryComboBox.setSelectedIndex(0);
        add(categoryComboBox);

        backButton2 = new JButton("Завершить удаление");
        add(backButton2);

        nextButton = new JButton("Удалить");
        add(nextButton);

        setVisible(true);
    }


    public static void deleteLiteratureFrame() throws SQLException {
        deleteLiteratureFomChangedEdition categoryPage = new deleteLiteratureFomChangedEdition();

        nextButton.addActionListener(e -> {
            int id = (int) categoryComboBox.getClientProperty(categoryComboBox.getSelectedItem());
            if(ConnectToOracle.deleteLiteratureFromEdition(changeEdition.EditionID, id) == -1){
                JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
            }
            else {
                JOptionPane.showMessageDialog(null, "Удалено!");
                categoryComboBox.removeAllItems();
                try {
                    ConnectToOracle.getListOfLiteratureForDelete(changeEdition.EditionID, categoryComboBox);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                categoryComboBox.setSelectedIndex(0);
            }
        });

        backButton2.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Удаление произведений из издания завершено!\nИнформция об издании изменена!");
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

