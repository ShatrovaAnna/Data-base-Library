package View.taken;

import Controller.MainStart;
import DBase.ConnectToOracle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class returnEditionFrame extends JFrame implements ActionListener {
    public static JTextField idEdition, readerField;
    private JComboBox librarianSelect;
    private JButton issueButton, backButton;
    public static String librarianName;

    public void returnEditionFrame() throws SQLException {

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
        JLabel idLabel = new JLabel("Введите id издания:", SwingConstants.CENTER);
        idEdition = new JTextField();
        mainPanel.add(idLabel);
        mainPanel.add(idEdition);

        JLabel readerLabel = new JLabel("Введите id читателя:", SwingConstants.CENTER);
        readerField = new JTextField();
        mainPanel.add(readerLabel);
        mainPanel.add(readerField);

        issueButton = new JButton("Принять");
        issueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        issueButton.addActionListener(this);
        mainPanel.add(issueButton);

        backButton = new JButton("Назад");
        backButton.addActionListener(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel);

        setTitle("Принять");
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
        } else if (e.getSource() == issueButton) {
            librarianName = (String) librarianSelect.getSelectedItem();
            try {
                if(ConnectToOracle.checkLibraryForEdition() == -1){
                    JOptionPane.showMessageDialog(null, "Книга была получена не в этой библиотеке!");
                }
                else{
                    updateBorrowingReturn();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void updateBorrowingReturn() {
        try {
            if (ConnectToOracle.updateBorrowing() == -1) {
                JOptionPane.showMessageDialog(null, "Проверьте еще раз данные!");
            } else {
                JOptionPane.showMessageDialog(null, "Принято!");
                MainStart.StartMainMenu();
                dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
