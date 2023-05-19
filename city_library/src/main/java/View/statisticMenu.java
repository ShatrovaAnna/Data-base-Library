package View;

import Controller.*;
import View.statistic.*;
import View.users.findReaderByID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class statisticMenu extends JFrame {

    public statisticMenu() {

        JPanel buttonPanel = new JPanel(new GridLayout(7,2, 20, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton button1 = new JButton("Список читателей, с произведением на руках");
        JButton button2 = new JButton("Список читателей, с изданием на руках");
        JButton button3 = new JButton("Список читателей, получавших издание с произведением в определенный период времени");
        JButton button4 = new JButton("Список читателей, с просроченным сроком литературы");
        JButton button5 = new JButton("Список читателей, не посешавших библиотеку в определенный период времени");
        JButton button6 = new JButton("Список изданий, полученных читателем в определенный период времени");
        JButton button7 = new JButton("Список читателей, с заданными характеристиками");
        JButton button8 = new JButton("Список читателей, обслуженных библиотекарем в определнный период времени");
        JButton button9 = new JButton("Выработка библиотекарей в определенный период времени");
        JButton button10 = new JButton("Список библиотекарей, работающих в зале");
        JButton button11 = new JButton("Список изданий, выданных с полки в настоящее время");
        JButton button12 = new JButton("Список изданий, поступивших или списанных в определенный период времени");
        JButton button13 = new JButton("Список самых популярных произведений");
        JButton button14 = new JButton("Список произведений, содержащихся в издании");
        JButton backButton = new JButton("Назад");

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        buttonPanel.add(button5);
        buttonPanel.add(button7);
        buttonPanel.add(button8);
        buttonPanel.add(button9);
        buttonPanel.add(button10);
        buttonPanel.add(button6);
        buttonPanel.add(button11);
        buttonPanel.add(button12);
        buttonPanel.add(button13);
        buttonPanel.add(button14);
        button1.addActionListener(new ClickListener());
        button2.addActionListener(new ClickListener());
        button3.addActionListener(new ClickListener());
        button4.addActionListener(new ClickListener());
        button5.addActionListener(new ClickListener());
        button6.addActionListener(new ClickListener());
        button7.addActionListener(new ClickListener());
        button8.addActionListener(new ClickListener());
        button9.addActionListener(new ClickListener());
        button10.addActionListener(new ClickListener());
        button11.addActionListener(new ClickListener());
        button12.addActionListener(new ClickListener());
        button13.addActionListener(new ClickListener());
        button14.addActionListener(new ClickListener());
        backButton.addActionListener(new ClickListener());

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        setTitle("Выбор статистики");
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
    }
        private void choseActionFromButton(String command) throws SQLException {
            switch (command){
                case ("Список читателей, с произведением на руках"):
                    new readerWithLiterature();
                    break;
                case ("Список читателей, с изданием на руках"):
                    new readerWithEdition();
                    break;
                case ("Список читателей, получавших издание с произведением в определенный период времени"):
                    new readerWithLiteraturePeriod();
                    break;
                case ("Список читателей, с просроченным сроком литературы"):
                    dispose();
                    readerWithOverdue.startReaderWithOverdue();
                    break;
                case ("Список изданий, выданных с полки в настоящее время"):
                    new editionBorrowingFromShelf();
                    break;
                case ("Список библиотекарей, работающих в зале"):
                    new workerFromRoom();
                    break;
                case ("Список изданий, полученных читателем в определенный период времени"):
                    new readerWithEditionPeriod();
                    break;
                case ("Выработка библиотекарей в определенный период времени"):
                    new workerResults();
                    break;
                case ("Список читателей, обслуженных библиотекарем в определнный период времени"):
                    new readerByWorkerPeriod();
                    break;
                case ("Список читателей, не посешавших библиотеку в определенный период времени"):
                    new readerNotEnterInLibrary();
                    break;
                case ("Список самых популярных произведений"):
                    new listOfMostPopularLiterature();
                    break;
                case ("Список произведений, содержащихся в издании"):
                    new findLiteratureInEdition();
                    break;
                case ("Список изданий, поступивших или списанных в определенный период времени"):
                    new editionInOrOut();
                    break;
                case("Список читателей, с заданными характеристиками"):
                    new readerWithCategoryAndAttribute();
                    break;
                case ("Назад"):
                    MainStart.StartMainMenu();
                    break;
            }
            dispose();
        }


    public static void startStatisticMenu(){
        new statisticMenu();
    }
}
