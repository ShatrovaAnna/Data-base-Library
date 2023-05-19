package View;

import Controller.*;
import View.editionsAndLiterature.findEditionByID;
import View.users.findReaderByID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainMenu extends JFrame{

    private JPanel mainPanel;
    private JPanel usersPanel;
    private JPanel borrowPanel;
    private JPanel collectionPanel;
    private JPanel statsPanel;

    public void MainMenu() {

        int rowsNumber = 6;

        mainPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        usersPanel = new JPanel(new GridLayout(rowsNumber, 1, 5, 5));
        usersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        borrowPanel = new JPanel(new GridLayout(rowsNumber, 1, 5, 5));
        borrowPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        collectionPanel = new JPanel(new GridLayout(rowsNumber, 1, 5,5));
        collectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statsPanel = new JPanel(new GridLayout(rowsNumber, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(usersPanel);
        mainPanel.add(borrowPanel);
        mainPanel.add(collectionPanel);
        mainPanel.add(statsPanel);

        JLabel usersLabel = new JLabel("Действия над пользователями и библиотекарями", SwingConstants.CENTER);
        JLabel borrowLabel = new JLabel("Выдача/Возврат", SwingConstants.CENTER);
        JLabel collectionLabel = new JLabel("Работа с фондом", SwingConstants.CENTER);
        JLabel statsLabel = new JLabel("Получение статистики", SwingConstants.CENTER);

        usersPanel.add(usersLabel);
        borrowPanel.add(borrowLabel);
        collectionPanel.add(collectionLabel);
        statsPanel.add(statsLabel);

        JButton addUserButton = new JButton("Добавить пользователя");
        addUserButton.addActionListener(new ClickListener());
        JButton addLibrarianButton = new JButton("Добавить библиотекаря");
        addLibrarianButton.addActionListener(new ClickListener());
        JButton editUserButton = new JButton("Изменить данные пользователя");
        editUserButton.addActionListener(new ClickListener());
        JButton editLibrarianButton = new JButton("Изменить информацию о библиотекаре");
        editLibrarianButton.addActionListener(new ClickListener());
        JButton readerByIDButton = new JButton("Найти читателя по ID");
        readerByIDButton.addActionListener(new ClickListener());
        usersPanel.add(addUserButton);
        usersPanel.add(addLibrarianButton);
        usersPanel.add(editUserButton);
        usersPanel.add(editLibrarianButton);
        usersPanel.add(readerByIDButton);

        JButton borrowButton = new JButton("Выдать издание");
        borrowButton.addActionListener(new ClickListener());
        JButton returnButton = new JButton("Принять издание");
        returnButton.addActionListener(new ClickListener());
        JButton findEditionButton = new JButton("Найти место издания по ID");
        findEditionButton.addActionListener(new ClickListener());

        borrowPanel.add(borrowButton);
        borrowPanel.add(returnButton);
        borrowPanel.add(findEditionButton);

        JButton checkAvailabilityButton = new JButton("Проверить наличие");
        checkAvailabilityButton.addActionListener(new ClickListener());
        JButton addPublicationButton = new JButton("Добавить издание");
        addPublicationButton.addActionListener(new ClickListener());
        JButton addLiteratureButton = new JButton("Добавить произведение");
        addLiteratureButton.addActionListener(new ClickListener());
        JButton editPublicationButton = new JButton("Изменить информацию об издании");
        editPublicationButton.addActionListener(new ClickListener());
        JButton editLiteratureButton = new JButton("Изменить информацию о произведении");
        editLiteratureButton.addActionListener(new ClickListener());
        collectionPanel.add(checkAvailabilityButton);
        collectionPanel.add(addPublicationButton);
        collectionPanel.add(addLiteratureButton);
        collectionPanel.add(editPublicationButton);
        collectionPanel.add(editLiteratureButton);


        JButton showStatsButton = new JButton("Перейти к выбору категории");
        showStatsButton.addActionListener(new ClickListener());
        statsPanel.add(showStatsButton);


        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ClickListener());
        add(backButton, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        setTitle("Главное меню");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                choseActionFromButton(e.getActionCommand());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        private void choseActionFromButton(String command) throws SQLException {
            switch (command){
                case ("Добавить пользователя"):
                    PeopleController.addReaderCategories();
                    break;
                case ("Изменить данные пользователя"):
                    PeopleController.changerReader();
                    break;
                case ("Добавить библиотекаря"):
                    PeopleController.addWorker();
                    break;
                case ("Изменить информацию о библиотекаре"):
                    PeopleController.changerWorker();
                    break;
                case ("Найти читателя по ID"):
                    new findReaderByID();
                    break;
                case ("Выдать издание"):
                    TakenController.takenEdition();
                    break;
                case ("Принять издание"):
                    TakenController.returnEdition();
                    break;
                case("Найти место издания по ID"):
                    new findEditionByID();
                    break;
                case ("Проверить наличие"):
                    AvailabilityController.AvailabilityController();
                    break;
                case ("Добавить издание"):
                    EditionLiteratureController.addEdition();
                    break;
                case ("Добавить произведение"):
                    EditionLiteratureController.addLiterature();
                    break;
                case ("Изменить информацию об издании"):
                    EditionLiteratureController.changeEditionStart();
                    break;
                case ("Изменить информацию о произведении"):
                    EditionLiteratureController.changeLiteratureStart();
                    break;
                case ("Перейти к выбору категории"):
                    statisticController.addFrameForStatistic();
                    break;
                case ("Назад"):
                    MainStart.StartLibrary();
                    break;
            }
            dispose();
        }
    }
}
