package View.users;

import Controller.MainStart;
import Controller.PeopleController;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddReader extends JFrame implements ActionListener {

    public static JTextField[] fields;
    public static JTextField nameField;
    private JButton backButton, saveButton;
    public static String[] attributes;

    public void AddReader(String[] attributes){

        this.attributes = attributes;

        JPanel labelPanel = new JPanel();
        JLabel label1 = new JLabel("Категория читателя:");
        JLabel label2 = new JLabel(ReaderCategory.getSelectedReaderCategoryName());
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        labelPanel.add(label1);
        labelPanel.add(label2);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(attributes.length + 2, 2, 5,5));

        nameField = new JTextField();
        JLabel nameFieldLabel = new JLabel("ФИО", SwingConstants.CENTER);
        nameFieldLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(nameFieldLabel);
        mainPanel.add(nameField);

        fields = new JTextField[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            JLabel categoryLabel = new JLabel(attributes[i], SwingConstants.CENTER);
            categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            fields[i] = new JTextField();
            mainPanel.add(categoryLabel);
            mainPanel.add(fields[i]);
        }

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        saveButton = new JButton("Сохранить");
        saveButton.addActionListener(this);
        mainPanel.add(saveButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel);

        setTitle("Добавление пользователя");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            try {
                PeopleController.addReaderCategories();
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == saveButton) {
            try {
                if(ConnectToOracle.insertReader() == -1){
                    JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
                }
                else{
                    int id = ConnectToOracle.getReaderId();
                    JOptionPane.showMessageDialog(null, "Читатель добавлен!\n Сообщите ему id: "+id);
                    MainStart.StartMainMenu();
                    dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public JTextField[] getFields(){
        return fields;
    }
    public JTextField getNameField(){
        return nameField;
    };
}
