package View.users;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ChangeWorker extends JFrame implements ActionListener {


    private JButton backButton;
    private static JButton saveButton, searchButton;
    public static JTextField fioField, nameField;
    public static JTextField roomField;
    private JPanel mainPanel, panel;
    public static int WorkerID;

    public ChangeWorker() {

        JPanel namePanel = new JPanel();

        searchButton = new JButton("Поиск");
        searchButton.addActionListener(this);
        JLabel nameLabel = new JLabel("Введите ФИО:");
        nameField = new JTextField(40);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        namePanel.add(searchButton);

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        JLabel fioLabel = new JLabel("ФИО");
        fioLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(fioLabel);
        fioField = new JTextField();
        fioField.setEditable(true);
        panel.add(fioField);

        JLabel roomNumberLabel = new JLabel("Номер зала");
        roomNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(roomNumberLabel);
        roomField = new JTextField();
        roomField.setEditable(true);
        panel.add(roomField);

        saveButton = new JButton("Сохранить");
        saveButton.addActionListener(this);
        panel.add(saveButton);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(namePanel, BorderLayout.NORTH);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Изменить библиотекаря");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            try {
                if (ConnectToOracle.getWorkerInfo() == -1) {
                    JOptionPane.showMessageDialog(null, "Библиотекарь не найден!\n Проверьте еще раз данные!");
                } else {
                    fioField.setText(nameField.getText());
                    mainPanel.add(panel, BorderLayout.CENTER);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == backButton) {
            try {
                MainStart.StartMainMenu();
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == saveButton) {
            try {
                if (ConnectToOracle.insertChangedWorker() == -1) {
                    JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
                } else {
                    JOptionPane.showMessageDialog(null, "Библиотекарь изменен!");
                    MainStart.StartMainMenu();
                    dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void addChangeWorkerFrame() {
        new ChangeWorker();
    }
}
