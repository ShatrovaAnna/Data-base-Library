package View.taken;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class takenEdition extends JFrame implements ActionListener {

    public static JTextField idEdition;
    public static JTextField readerField;
    public static JTextField returnField;
    private JComboBox librarianSelect;
    private JButton issueButton, backButton, addButton;
    public static String librarianName;
    public static int returnTerm;

    public void takenEdition() throws SQLException {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 5,5));

        String[] librarians = ConnectToOracle.getWorkerLibrary();
        JLabel librarianLabel = new JLabel("Выберете библиотекаря:", SwingConstants.CENTER);
        librarianSelect = new JComboBox(librarians);
        librarianSelect.setSelectedIndex(0);
        mainPanel.add(librarianLabel);
        mainPanel.add(librarianSelect);

        JPanel idPanel = new JPanel();
        idPanel.setLayout(new BorderLayout());
        JLabel idLabel = new JLabel("Введите ID издания:", SwingConstants.CENTER);
        idEdition = new JTextField();
        mainPanel.add(idLabel);
        mainPanel.add(idEdition);

        JLabel readerLabel = new JLabel("Введите ID читателя:", SwingConstants.CENTER);
        readerField = new JTextField();
        mainPanel.add(readerLabel);
        mainPanel.add(readerField);

        issueButton = new JButton("Выдать");
        issueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        issueButton.addActionListener(this);
        mainPanel.add(issueButton);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel);

        setTitle("Выдать");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void returnFrame() throws SQLException {
        JFrame dialog = new JFrame();
        dialog.setSize(650, 100);
        dialog.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(ConnectToOracle.getRuleForEdition()+" (введите кол-во дней)");
        returnField = new JTextField();
        returnField.setPreferredSize(new Dimension(50,30));
        dialog.add(label);
        dialog.add(returnField);
        addButton = new JButton("Добавить");
        addButton.addActionListener(e -> {
            returnTerm = Integer.parseInt(returnField.getText());
            insertBorrowingStart();
            dialog.dispose();
        });
        JPanel paneld = new JPanel();
        paneld.add(label);
        paneld.add(returnField);
        paneld.add(addButton);
        dialog.add(paneld);
        dialog.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            try {
                MainStart.StartMainMenu();
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == issueButton) {
            try {
                if(ConnectToOracle.checkReaderBeforeTaken(Integer.parseInt(readerField.getText())) == -1){
                    JOptionPane.showMessageDialog(null, "Невозможно совершить!\nЧитатель деактивирован");
                } else if (ConnectToOracle.checkReaderBeforeTaken(Integer.parseInt(readerField.getText())) == -2) {
                    JOptionPane.showMessageDialog(null, "Читатель с таким ID не найден!");
                }
                else {
                    try {
                        returnFrame();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            librarianName = (String) librarianSelect.getSelectedItem();
        }
    }

    private void insertBorrowingStart() {
        try {
            if (ConnectToOracle.insertBorrowing() == -1) {
                JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!\nОсобенно id издания");
            } else {
                JOptionPane.showMessageDialog(null, "Выдано!");
                MainStart.StartMainMenu();
                dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
