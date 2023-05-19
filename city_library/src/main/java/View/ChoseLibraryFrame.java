package View;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ChoseLibraryFrame extends JFrame implements ActionListener {

    private JPanel panel;
    private JComboBox<String> libraryComboBox;
    private JButton nextButton, exitButton;

    public static int LibraryID;

    public void ChoseLibraryFrame(String[] libraryNames) {

        setTitle("Начальный экран");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JLabel title = new JLabel("Добро пожаловать в сеть городских библиотек", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.PLAIN, 20));
        panel.add(title);

        JPanel secondRow = new JPanel();
        secondRow.setLayout(new FlowLayout());

        JLabel label = new JLabel("Выберете библиотеку:");
        secondRow.add(label);

        libraryComboBox = new JComboBox<>(libraryNames);
        libraryComboBox.setSelectedIndex(0);
        secondRow.add(libraryComboBox);

        nextButton = new JButton("Далее");
        nextButton.addActionListener(this);
        secondRow.add(nextButton);
        exitButton = new JButton("Выйти");
        exitButton.addActionListener(this);

        panel.add(secondRow);
       // panel.add(exitButton);

        add(panel);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            String LibraryString = (String) libraryComboBox.getSelectedItem();
            try {
                LibraryID = ConnectToOracle.getLibraryID(LibraryString);
                MainStart.StartMainMenu();
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == exitButton) {
            dispose();
            System.exit(0);
        }
    }

    public static int getSelectedLibrary() {
        return LibraryID;
    }
}
