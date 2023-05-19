package View.users;

import Controller.MainStart;
import Controller.PeopleController;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddWorker extends JFrame implements ActionListener {

    private JButton backButton;
    private static JButton saveButton;
    public static JTextField fioField;
    public static JTextField roomField;

    public void AddWorker() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        JLabel fioLabel = new JLabel("ФИО");
        fioLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(fioLabel);
        fioField = new JTextField();
        panel.add(fioField);

        JLabel roomNumberLabel = new JLabel("Номер зала");
        roomNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(roomNumberLabel);
        roomField = new JTextField();
        panel.add(roomField);

        saveButton = new JButton("Сохранить");
        saveButton.addActionListener(this);
        panel.add(saveButton);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Добавление библиотекаря");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            try {
                MainStart.StartMainMenu();
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == saveButton) {
            try {
                if (ConnectToOracle.insertWorker() == -1) {
                    JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
                } else {
                    int id = ConnectToOracle.getWorkerId();
                    JOptionPane.showMessageDialog(null, "Библиотекарь добавлен!\n Его id: "+id);
                    MainStart.StartMainMenu();
                    dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
