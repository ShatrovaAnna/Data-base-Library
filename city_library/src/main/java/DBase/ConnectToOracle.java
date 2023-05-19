package DBase;

import View.ChoseLibraryFrame;
import View.booksearch.checkAvailabilityFrame;
import View.editionsAndLiterature.*;
import View.statistic.*;
import View.taken.returnEditionFrame;
import View.users.AddReader;
import View.users.AddWorker;
import View.users.ChangeWorker;
import View.users.ReaderCategory;
import View.taken.takenEdition;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ConnectToOracle {

    public static Connection conn;
    public static Statement statmt;
    public static PreparedStatement pstmt, pstmt2;
    public static ResultSet resSet;

    public static void ConnectionToDB() throws SQLException {
        String url = "jdbc:oracle:thin:@84.237.50.81:1521:";
        String username = "";
        String password = "";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        statmt = conn.createStatement();
    }


    public static String[] getLibrariesName() throws SQLException {
        resSet = statmt.executeQuery(SQLRequests.LibraryName);
        ArrayList<String> list = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString("name");
            list.add(name);
        }
        return list.toArray(new String[0]);
    }

    public static int getLibraryID(String name) throws SQLException {

        int id = 0;

        pstmt = conn.prepareStatement(SQLRequests.LibraryID);
        pstmt.setString(1, name);
        resSet = pstmt.executeQuery();
        while (resSet.next()) {
            id = resSet.getInt("id_library");
        }
        return id;
    }

    public static String[] getReaderCategories() throws SQLException {
        resSet = statmt.executeQuery(SQLRequests.ReaderCategories);
        ArrayList<String> list = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString("category");
            list.add(name);
        }
        return list.toArray(new String[0]);
    }

    public static int getReaderCategoryID(String category) throws SQLException {

        int id = 0;

        pstmt = conn.prepareStatement(SQLRequests.CategoryID);
        pstmt.setString(1, category); // устанавливаем значение параметра
        resSet = pstmt.executeQuery(); // выполняем запрос
        while (resSet.next()) {
            id = resSet.getInt("id_category");
        }
        return id;
    }

    public static String[] getAttribute() throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.Attribute);
        pstmt.setInt(1, ReaderCategory.getSelectedReaderCategoryID());
        resSet = pstmt.executeQuery();
        ArrayList<String> attribute = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString("attribute_name");
            attribute.add(name);
        }
        return attribute.toArray(new String[0]);
    }

    public static String[] getAttributeID() throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.AttributeID);
        pstmt.setInt(1, ReaderCategory.getSelectedReaderCategoryID()); // устанавливаем значение параметра
        resSet = pstmt.executeQuery();
        ArrayList<Integer> attribute_id = new ArrayList<>();
        while (resSet.next()) {
            int id = resSet.getInt("Id_attribute_read");
            attribute_id.add(id);
        }
        return attribute_id.toArray(new String[0]);
    }

    public static int insertReader() throws SQLException {
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertNameReader);
            pstmt.setInt(1, 0);
            pstmt.setInt(2, ChoseLibraryFrame.getSelectedLibrary());
            pstmt.setInt(3, ReaderCategory.getSelectedReaderCategoryID());
            pstmt.setString(4, AddReader.nameField.getText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        // System.out.println(getAttributeReaderId(AddReader.attributes[0]) + " " + getReaderId() + " " + AddReader.fields[0].getText());
        for (int i = 0; i < AddReader.attributes.length; i++) {
            try {
                pstmt2 = conn.prepareStatement(SQLRequests.InsertAttributeReader);
                pstmt2.setInt(1, getAttributeReaderId(AddReader.attributes[i]));
                pstmt2.setInt(2, getReaderId());
                pstmt2.setString(3, AddReader.fields[i].getText());
                pstmt2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 0;
    }

    public static Integer getReaderId() throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.ReaderID);
        pstmt.setString(1, AddReader.nameField.getText());
        resSet = pstmt.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_reader");
        }
        return id;
    }

    public static ResultSet getReaderName(String text, int checker) throws SQLException {
        if (checker == 0){
            pstmt = conn.prepareStatement(SQLRequests.ReaderName);
            pstmt.setInt(1, Integer.parseInt(text));
        } else {
            String prepareString = "%"+text+"%";
            pstmt = conn.prepareStatement(SQLRequests.ReaderNameByName);
            pstmt.setString(1, prepareString);
        }
        resSet = pstmt.executeQuery();
        return resSet;
    }

    public static int getAttributeReaderId(String attribute) throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.AttributeReaderID);
        pstmt.setString(1, attribute);
        resSet = pstmt.executeQuery();
        int id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_attribute_read");
        }
        return id;
    }

    public static int insertWorker() throws SQLException {
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertWorkerName);
            pstmt.setInt(1, 0);
            pstmt.setString(2, AddWorker.fioField.getText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.InsertWorkerRoom);
            pstmt2.setInt(1, Integer.parseInt(AddWorker.roomField.getText()));
            pstmt2.setInt(2, getWorkerId());
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        return 0;
    }

    public static Integer getWorkerId() throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.WorkerID);
        pstmt.setString(1, AddWorker.fioField.getText());
        resSet = pstmt.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_worker");
        }
        return id;
    }

    public static String[] getWorkerLibrary() throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.WorkerLibrary);
        pstmt.setInt(1, ChoseLibraryFrame.getSelectedLibrary());
        resSet = pstmt.executeQuery();
        ArrayList<String> names = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString("name");
            names.add(name);
        }
        return names.toArray(new String[0]);
    }

    public static Integer getWorkerIdBorrowing() throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.WorkerID);
        pstmt2.setString(1, takenEdition.librarianName);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_worker");
        }
        return id;
    }

    public static Integer getWorkerIdBorrowingReturn() throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.WorkerID);
        pstmt2.setString(1, returnEditionFrame.librarianName);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_worker");
        }
        return id;
    }

    public static int insertBorrowing() throws SQLException {
        LocalDate date = LocalDate.now();
        LocalDate dateReturn = date.plusDays(takenEdition.returnTerm);
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertTaken);
            pstmt.setInt(1, 0);
            pstmt.setInt(2, Integer.parseInt(takenEdition.readerField.getText()));
            pstmt.setInt(3, Integer.parseInt(takenEdition.idEdition.getText()));
            pstmt.setInt(4, getWorkerIdBorrowing());
            pstmt.setInt(5, ChoseLibraryFrame.getSelectedLibrary());
            pstmt.setDate(6, Date.valueOf(date));
            pstmt.setDate(7, Date.valueOf(dateReturn));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static String getRuleForEdition() throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.RuleForEdition);
        pstmt2.setInt(1, Integer.parseInt(takenEdition.idEdition.getText()));
        resSet = pstmt2.executeQuery();
        String rule = null;
        while (resSet.next()) {
            rule = resSet.getString("name");
        }
        return rule;
    }

    public static int updateBorrowing() throws SQLException {
        LocalDate date = LocalDate.now();
        try {
            pstmt = conn.prepareStatement(SQLRequests.ReturnUpdate);
            pstmt.setInt(1, getWorkerIdBorrowingReturn());
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setInt(3, Integer.parseInt(returnEditionFrame.idEdition.getText()));
            pstmt.setInt(4, Integer.parseInt(returnEditionFrame.readerField.getText()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static int checkLibraryForEdition() throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.EditionLibraryID);
        pstmt.setInt(1, Integer.parseInt(returnEditionFrame.idEdition.getText()));
        pstmt.setInt(2, Integer.parseInt(returnEditionFrame.readerField.getText()));
        resSet = pstmt.executeQuery();
        int id = 0;
        while (resSet.next()) {
            id = resSet.getInt("library_id");
        }
        if (id != ChoseLibraryFrame.getSelectedLibrary()) {
            return -1;
        }
        return 0;
    }

    public static int insertLiterature() throws SQLException {
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertLiterature);
            pstmt.setInt(1, 0);
            pstmt.setInt(2, getCategoryLiteratureID());
            pstmt.setString(3, addLiteratureFrame.titleField.getText());
            pstmt.setString(4, addLiteratureFrame.authorField.getText());
            pstmt.setInt(5, Integer.parseInt(addLiteratureFrame.yearField.getText()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    private static int getCategoryLiteratureID() throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.CategoryLiteratureID);
        pstmt2.setString(1, addLiteratureFrame.category);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_lit_category");
        }
        return id;
    }

    public static String[] getLiteratureCategory() throws SQLException {
        resSet = statmt.executeQuery(SQLRequests.LiteratureCategoryName);
        ArrayList<String> names = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString("name");
            names.add(name);
        }
        return names.toArray(new String[0]);
    }

    public static int insertEdition() {
        LocalDate date = LocalDate.now();
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertEdition);
            pstmt.setInt(1, 0);
            pstmt.setInt(2, getEditionCategoryID());
            pstmt.setInt(3, Integer.parseInt(addEditionFrame.shelfNumberField.getText()));
            pstmt.setString(4, addEditionFrame.nameField.getText());
            pstmt.setDate(5, Date.valueOf(date));
            pstmt.setInt(6, getRuleCategoryID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    private static int getEditionCategoryID() throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.CategoryEditionID);
        pstmt2.setString(1, addEditionFrame.category);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_ed_category");
        }
        return id;
    }

    private static int getEditionCategoryByID(String param) throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.CategoryEditionID);
        pstmt2.setString(1, param);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_ed_category");
        }
        return id;
    }

    public static String[] getEditionCategory() throws SQLException {
        resSet = statmt.executeQuery(SQLRequests.EditionCategoryName);
        ArrayList<String> names = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString("name");
            names.add(name);
        }
        return names.toArray(new String[0]);
    }

    private static int getRuleCategoryID() throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.RuleID);
        pstmt2.setString(1, addEditionFrame.rule);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_rule");
        }
        return id;
    }

    private static int getRuleCategoryIDWithParam(String param) throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.RuleID);
        pstmt2.setString(1, param);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_rule");
        }
        return id;
    }

    public static String[] getRuleCategory() throws SQLException {
        resSet = statmt.executeQuery(SQLRequests.RuleName);
        ArrayList<String> names = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString("name");
            names.add(name);
        }
        return names.toArray(new String[0]);
    }

    public static String getRuleCategoryNameByID(int param) throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.RuleNameByID);
        pstmt2.setInt(1, param);
        resSet = pstmt2.executeQuery();
        String name = null;
        while (resSet.next()) {
            name = resSet.getString("name");
        }
        return name;
    }

    public static void getLiteratureByCategory(JComboBox<String> itemComboBox) throws SQLException {

        pstmt = conn.prepareStatement(SQLRequests.LiteratureByCategory);
        pstmt.setInt(1, getCategoryLiteratureIDForEdition());
        resSet = pstmt.executeQuery();

        ArrayList<String> items = new ArrayList<>();
        while (resSet.next()) {
            int id = resSet.getInt("id_literature");
            String title = resSet.getString("title");
            String author = resSet.getString("author");
            int year = resSet.getInt("year");

            items.add(title + " " + author + " (" + year + ")");
            itemComboBox.addItem(title + " " + author + " (" + year + ")");
            itemComboBox.putClientProperty(title + " " + author + " (" + year + ")", id);
        }
    }
    public static void getLiteratureByCategoryWithParametr(String category, JComboBox<String> itemComboBox) throws SQLException {

        pstmt = conn.prepareStatement(SQLRequests.LiteratureByCategory);
        pstmt.setInt(1, getCategoryLiteratureIDForEditionWithParametr(category));
        resSet = pstmt.executeQuery();

        ArrayList<String> items = new ArrayList<>();
        while (resSet.next()) {
            int id = resSet.getInt("id_literature");
            String title = resSet.getString("title");
            String author = resSet.getString("author");
            int year = resSet.getInt("year");

            items.add(title + " " + author + " (" + year + ")");
            itemComboBox.addItem(title + " " + author + " (" + year + ")");
            itemComboBox.putClientProperty(title + " " + author + " (" + year + ")", id);
        }
    }

    public static int insertEditionLiterature(int id_lit) throws SQLException {
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertEditionLit);
            pstmt.setInt(1, getEditionID());
            pstmt.setInt(2, id_lit);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static int getEditionID() throws SQLException {
        LocalDate date = LocalDate.now();
        pstmt2 = conn.prepareStatement(SQLRequests.EditionID);
        pstmt2.setInt(1, Integer.parseInt(addEditionFrame.shelfNumberField.getText()));
        pstmt2.setString(2, addEditionFrame.nameField.getText());
        pstmt2.setDate(3, Date.valueOf(date));
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_edition");
        }
        return id;
    }

    public static int insertEditionLiteratureWithEditionID(int id_ed, int id_lit) throws SQLException {
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertEditionLit);
            pstmt.setInt(1, id_ed);
            pstmt.setInt(2, id_lit);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    private static int getCategoryLiteratureIDForEdition() throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.CategoryLiteratureID);
        pstmt2.setString(1, CategoryPage.category);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_lit_category");
        }
        return id;
    }
    private static int getCategoryLiteratureIDForEditionWithParametr(String category) throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.CategoryLiteratureID);
        pstmt2.setString(1, category);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt("id_lit_category");
        }
        return id;
    }

    public static ResultSet getListOfEdition(int categorySQL, int categoryBorrowing, int libraryBorrowing) throws SQLException {
        try {
            String preparedString = "%" + checkAvailabilityFrame.searchText + "%";
            if (libraryBorrowing == 0){
            if (categorySQL == 1 && categoryBorrowing == 0) {
                pstmt2 = conn.prepareStatement(SQLRequests.EditionListByTitle);
                pstmt2.setString(1, preparedString);
                pstmt2.setInt(2, ChoseLibraryFrame.getSelectedLibrary());
                pstmt2.setString(3, preparedString);
                pstmt2.setInt(4, ChoseLibraryFrame.getSelectedLibrary());
                resSet = pstmt2.executeQuery();
            }
            if (categorySQL == 2 && categoryBorrowing == 0) {
                pstmt2 = conn.prepareStatement(SQLRequests.EditionListByAuthor);
                pstmt2.setString(1, preparedString);
                pstmt2.setInt(2, ChoseLibraryFrame.getSelectedLibrary());
                pstmt2.setString(3, preparedString);
                pstmt2.setInt(4, ChoseLibraryFrame.getSelectedLibrary());
                resSet = pstmt2.executeQuery();
            }
            if (categorySQL == 3 && categoryBorrowing == 0) {
                pstmt2 = conn.prepareStatement(SQLRequests.EditionListByEdition);
                pstmt2.setString(1, preparedString);
                pstmt2.setInt(2, ChoseLibraryFrame.getSelectedLibrary());
                pstmt2.setString(3, preparedString);
                pstmt2.setInt(4, ChoseLibraryFrame.getSelectedLibrary());
                resSet = pstmt2.executeQuery();
            }
            if (categorySQL == 1 && categoryBorrowing == 1) {
                pstmt2 = conn.prepareStatement(SQLRequests.EditionListByTitleBorr);
                pstmt2.setString(1, preparedString);
                pstmt2.setInt(2, ChoseLibraryFrame.getSelectedLibrary());
                resSet = pstmt2.executeQuery();
            }
            if (categorySQL == 2 && categoryBorrowing == 1) {
                pstmt2 = conn.prepareStatement(SQLRequests.EditionListByAuthorBorr);
                pstmt2.setString(1, preparedString);
                pstmt2.setInt(2, ChoseLibraryFrame.getSelectedLibrary());
                resSet = pstmt2.executeQuery();
            }
            if (categorySQL == 3 && categoryBorrowing == 1) {
                pstmt2 = conn.prepareStatement(SQLRequests.EditionListByEditionBorr);
                pstmt2.setString(1, preparedString);
                pstmt2.setInt(2, ChoseLibraryFrame.getSelectedLibrary());
                resSet = pstmt2.executeQuery();
            }
            } else {
                if (categorySQL == 1 && categoryBorrowing == 0) {
                    pstmt2 = conn.prepareStatement(SQLRequests.EditionListByTitleAll);
                    pstmt2.setString(1, preparedString);
                    pstmt2.setString(2, preparedString);
                    resSet = pstmt2.executeQuery();
                }
                if (categorySQL == 2 && categoryBorrowing == 0) {
                    pstmt2 = conn.prepareStatement(SQLRequests.EditionListByAuthorAll);
                    pstmt2.setString(1, preparedString);
                    pstmt2.setString(2, preparedString);
                    resSet = pstmt2.executeQuery();
                }
                if (categorySQL == 3 && categoryBorrowing == 0) {
                    pstmt2 = conn.prepareStatement(SQLRequests.EditionListByEditionAll);
                    pstmt2.setString(1, preparedString);
                    pstmt2.setString(2, preparedString);
                    resSet = pstmt2.executeQuery();
                }
                if (categorySQL == 1 && categoryBorrowing == 1) {
                    pstmt2 = conn.prepareStatement(SQLRequests.EditionListByTitleBorrAll);
                    pstmt2.setString(1, preparedString);
                    resSet = pstmt2.executeQuery();
                }
                if (categorySQL == 2 && categoryBorrowing == 1) {
                    pstmt2 = conn.prepareStatement(SQLRequests.EditionListByAuthorBorrAll);
                    pstmt2.setString(1, preparedString);
                    resSet = pstmt2.executeQuery();
                }
                if (categorySQL == 3 && categoryBorrowing == 1) {
                    pstmt2 = conn.prepareStatement(SQLRequests.EditionListByEditionBorrAll);
                    pstmt2.setString(1, preparedString);
                    resSet = pstmt2.executeQuery();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static String getEditionCategoryIDSearch(Object param) throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.EditionCategoryNameByID);
        pstmt2.setObject(1, param);
        resSet = pstmt2.executeQuery();
        String name = null;
        while (resSet.next()) {
            name = resSet.getString("name");
        }
        return name;
    }

    public static int getWorkerInfo() throws SQLException {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.WorkerInfoByName);
            pstmt2.setString(1, ChangeWorker.nameField.getText());
            resSet = pstmt2.executeQuery();
            while (resSet.next()) {
                ChangeWorker.WorkerID = resSet.getInt("id_worker");
                ChangeWorker.roomField.setText(String.valueOf(resSet.getInt("room_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static int insertChangedWorker() {
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertChangedWorkerName);
            pstmt.setString(1, ChangeWorker.fioField.getText());
            pstmt.setInt(2, ChangeWorker.WorkerID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertChangedWorkerRoom);
            pstmt.setInt(1, Integer.parseInt(ChangeWorker.roomField.getText()));
            pstmt.setInt(2, ChangeWorker.WorkerID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        return 0;
    }

    public static ResultSet getEditionInfo() {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.EditionInfoByID);
            pstmt2.setInt(1, changeEdition.EditionID);
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
        return resSet;
    }

    public static int updateEdition(int outCheck) {
        LocalDate date = LocalDate.now();
        if (outCheck == 0) {
            try {
                pstmt = conn.prepareStatement(SQLRequests.InsertChangedEdition);
                pstmt.setInt(1, getEditionCategoryByID(changeEdition.category));
                pstmt.setInt(2, Integer.parseInt(changeEdition.shelfNumberField.getText()));
                pstmt.setString(3, changeEdition.nameField.getText());
                pstmt.setInt(4, getRuleCategoryIDWithParam(changeEdition.rule));
                pstmt.setInt(5, changeEdition.EditionID);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            try {
                pstmt = conn.prepareStatement(SQLRequests.InsertChangedEditionOut);
                pstmt.setInt(1, getEditionCategoryByID(changeEdition.category));
                pstmt.setNull(2, Types.NUMERIC);
                pstmt.setString(3, changeEdition.nameField.getText());
                pstmt.setDate(4, Date.valueOf(date));
                pstmt.setInt(5, getRuleCategoryIDWithParam(changeEdition.rule));
                pstmt.setInt(6, changeEdition.EditionID);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 0;
    }

    public static ResultSet getListOfLiteratureByLiteratureTitle() {
        try {
            String preparedString = "%" + readerWithLiterature.searchText + "%";
            pstmt2 = conn.prepareStatement(SQLRequests.ReaderListByTitleBorrowing);
            pstmt2.setString(1, preparedString);
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfEditionByNameAndCategory() {
        try {
            String preparedString = "%" + readerWithEdition.searchText + "%";
            pstmt2 = conn.prepareStatement(SQLRequests.ReaderListByEditionBorrowing);
            pstmt2.setString(1, readerWithEdition.category);
            pstmt2.setString(2, preparedString);
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfEditionBorrowingFromShelf() {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.EditionListBorrowingFromShelf);
            pstmt2.setInt(1, Integer.parseInt(editionBorrowingFromShelf.searchText));
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfReaderWithOverdue() {
        LocalDate date = LocalDate.now();
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.ReaderWithOverdue);
            pstmt2.setDate(1, Date.valueOf(date));
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfReaderWithLiteraturePeriod(String name, LocalDate data1, LocalDate data2) throws SQLException {
        String preparedString = "%" + name + "%";
        try {
            System.out.println(readerWithLiteraturePeriod.searchText);
            pstmt2 = conn.prepareStatement(SQLRequests.ReaderWithLiteraturePeriod);
            pstmt2.setDate(1, Date.valueOf(data1));
            pstmt2.setDate(2, Date.valueOf(data2));
            pstmt2.setString(3, preparedString);
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfWorkerFromRoom() {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.WorkerFromRoom);
            pstmt2.setInt(1, Integer.parseInt(workerFromRoom.searchText));
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfReaderWithEditionPeriod(int i, String searchText, LocalDate data1, LocalDate data2) {
        try {
            if(i == 1){
                pstmt2 = conn.prepareStatement(SQLRequests.ReaderWithEditionPeriodSigh);
            }
            else{
                pstmt2 = conn.prepareStatement(SQLRequests.ReaderWithEditionPeriodNoSigh);
            }
            pstmt2.setString(1, searchText);
            pstmt2.setDate(2, Date.valueOf(data1));
            pstmt2.setDate(3, Date.valueOf(data2));
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfWorkerResults(LocalDate data1, LocalDate data2) {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.WorkerResults);
            pstmt2.setDate(1, Date.valueOf(data1));
            pstmt2.setDate(2, Date.valueOf(data2));
            pstmt2.setDate(3, Date.valueOf(data1));
            pstmt2.setDate(4, Date.valueOf(data2));
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }
    public static String[] getWorker() throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.WorkerList);
        resSet = pstmt.executeQuery();
        ArrayList<String> names = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString("name");
            names.add(name);
        }
        return names.toArray(new String[0]);
    }

    public static ResultSet getListOfReaderByWorkerPeriod(String worker, LocalDate data1, LocalDate data2) {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.ReaderByWorkerPeriod);
            pstmt2.setString(1, worker);
            pstmt2.setDate(2, Date.valueOf(data1));
            pstmt2.setDate(3, Date.valueOf(data2));
            pstmt2.setDate(4, Date.valueOf(data1));
            pstmt2.setDate(5, Date.valueOf(data2));
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfReaderNotInLibrary(LocalDate data1, LocalDate data2) {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.ReaderNotInLibrary);
            pstmt2.setDate(1, Date.valueOf(data1));
            pstmt2.setDate(2, Date.valueOf(data2));
            pstmt2.setDate(3, Date.valueOf(data1));
            pstmt2.setDate(4, Date.valueOf(data2));
            pstmt2.setInt(5, ChoseLibraryFrame.getSelectedLibrary());
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfMostPopularLiterature(int num) {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.MostPopularLiterature);
            pstmt2.setInt(1, num);
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getListOfEditionInOrOutPeriod(int category, LocalDate data1, LocalDate data2) throws SQLException {
        try {
            if (category == 1){
            pstmt2 = conn.prepareStatement(SQLRequests.EditionInPeriod);
            } else{
            pstmt2 = conn.prepareStatement(SQLRequests.EditionOutPeriod);
            }
            pstmt2.setDate(1, Date.valueOf(data1));
            pstmt2.setDate(2, Date.valueOf(data2));
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ResultSet getEditionInfoByID(int id) {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.EditionInfoByid);
            pstmt2.setInt(1, id);
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static void getAttributeByReaderCategory(JComboBox<String> categoryAttributeComboBox, String categoryReader) throws SQLException {
        try{
            pstmt = conn.prepareStatement(SQLRequests.AttributeByReaderCategory);
            pstmt.setString(1, categoryReader);
            resSet = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resSet.next()) {
            String name = resSet.getString(1);
            categoryAttributeComboBox.addItem(name);
        }
    }

    public static void getAttributeValueFromReader(JComboBox<String> attributeValueComboBox, String categoryAttribute) throws SQLException {
        try{
            pstmt = conn.prepareStatement(SQLRequests.AttributeValueFromReader);
            pstmt.setString(1, categoryAttribute);
            resSet = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resSet.next()) {
            String name = resSet.getString(1);
            attributeValueComboBox.addItem(name);
        }
    }

    public static ResultSet getListOfReaderByCategoryAndAttribute(String categoryReader, String categoryAttribute, String valueAttribute, int library) {
        try {
            if (library == 1){
                pstmt2 = conn.prepareStatement(SQLRequests.ReaderByCategoryAndAttribute);
                pstmt2.setString(1, categoryReader);
                pstmt2.setString(2, categoryAttribute);
                pstmt2.setString(3, valueAttribute);
            } else {
                pstmt2 = conn.prepareStatement(SQLRequests.ReaderByCategoryAndAttributeLibrary);
                pstmt2.setString(1, categoryReader);
                pstmt2.setString(2, categoryAttribute);
                pstmt2.setString(3, valueAttribute);
                pstmt2.setInt(4, ChoseLibraryFrame.getSelectedLibrary());
            }
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static int checkReaderBeforeTaken(int id) throws SQLException {
        try{
            pstmt = conn.prepareStatement(SQLRequests.ReaderDisabled);
            pstmt.setInt(1, id);
            resSet = pstmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (resSet.next()) {
            String value = resSet.getString(1);
            if (value == null) {
                return 0;
            } else if (value.equals("1")) {
                return -1;
            }
        }
        return -2;
    }

    public static int getLiteratureID(String literatureName, String author) throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.LiteratureIDByName);
        pstmt2.setString(1, literatureName);
        pstmt2.setString(2, author);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt(1);
        }
        return id;
    }

    public static ResultSet getLiteratureInfo(int id) {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.LiteratureInfoByid);
            pstmt2.setInt(1, id);
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static Object getLiteratureCategoryByID(int anInt) throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.LiteratureCategoryByID);
        pstmt.setInt(1, anInt);
        resSet = pstmt.executeQuery();
        String name = null;
        while (resSet.next()) {
            name = resSet.getString(1);
        }
        return name;
    }
    public static int getLiteratureCategoryIDByName(String name) throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.LiteratureCategoryIDByName);
        pstmt2.setString(1, name);
        resSet = pstmt2.executeQuery();
        Integer id = 0;
        while (resSet.next()) {
            id = resSet.getInt(1);
        }
        return id;
    }

    public static int updateLiterature(int literatureID) {
        try {
            pstmt = conn.prepareStatement(SQLRequests.InsertChangedLiterature);
            pstmt.setInt(1, getLiteratureCategoryIDByName(changeLiterature.category));
            pstmt.setString(2,changeLiterature.nameField.getText());
            pstmt.setString(3, changeLiterature.authorField.getText());
            pstmt.setInt(4, Integer.parseInt(changeLiterature.yearField.getText()));
            pstmt.setInt(5, literatureID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static ResultSet getReaderInfo(int readerID) {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.ReaderMainInfoByID);
            pstmt2.setInt(1, readerID);
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }
    public static String[] getAttributeReaderByName(String category) throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.AttributeByName);
        pstmt.setString(1, category);
        resSet = pstmt.executeQuery();
        ArrayList<String> attribute = new ArrayList<>();
        while (resSet.next()) {
            String name = resSet.getString(1);
            attribute.add(name);
        }
        return attribute.toArray(new String[0]);
    }

    public static String getAttributeValueFromReaderByAttributeName(String firstAttribute, int readerID) throws SQLException {
        pstmt = conn.prepareStatement(SQLRequests.AttributeByValueName);
        pstmt.setInt(1, readerID);
        pstmt.setString(2, firstAttribute);
        resSet = pstmt.executeQuery();
        String value = null;
        while (resSet.next()) {
            value = resSet.getString(1);
        }
        return value;
    }

    public static void updateReader(int readerID, String name, String library, String categoryReader, String[] attributes, JTextField[] fields, int disable) throws SQLException {
        System.out.println(readerID+" "+getLibraryID(library) + " "+categoryReader+" "+getReaderCategoryID(categoryReader));
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.UpdateNameReader);
            pstmt2.setInt(1, getLibraryID(library));
            pstmt2.setInt(2, getReaderCategoryID(categoryReader));
            pstmt2.setString(3, name);
            if (disable == 0){
                pstmt2.setNull(4, Types.NUMERIC);
            } else{
                pstmt2.setInt(4, 1);
            }
            pstmt2.setInt(5, readerID);
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // System.out.println(getAttributeReaderId(AddReader.attributes[0]) + " " + getReaderId() + " " + AddReader.fields[0].getText());
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.DeleteAttributeReader);
            pstmt2.setInt(1, readerID);
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (disable == 0) {
            for (int i = 0; i < attributes.length; i++) {
                try {
                    pstmt2 = conn.prepareStatement(SQLRequests.InsertAttributeReader);
                    pstmt2.setInt(1, getAttributeReaderId(attributes[i]));
                    pstmt2.setInt(2, readerID);
                    pstmt2.setString(3, String.valueOf(fields[i].getText()));
                    pstmt2.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static ResultSet getListOfLiteratureFromEdition(String searchText, int checker) {
        try {
            if(checker==1){
                pstmt2 = conn.prepareStatement(SQLRequests.LiteratureListFromEditionName);
                pstmt2.setString(1, searchText);
            }else{
                pstmt2 = conn.prepareStatement(SQLRequests.LiteratureListFromEditionID);
                pstmt2.setInt(1, Integer.parseInt(searchText));
            }
            resSet = pstmt2.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static void getListOfLiteratureForDelete(int editionID, JComboBox<String> categoryComboBox) throws SQLException {
        pstmt2 = conn.prepareStatement(SQLRequests.LiteratureListByEditionID);
        pstmt2.setInt(1, editionID);
        resSet = pstmt2.executeQuery();
        ArrayList<String> items = new ArrayList<>();
        while (resSet.next()) {
            int id = resSet.getInt("id_literature");
            String title = resSet.getString("title");
            String author = resSet.getString("author");
            int year = resSet.getInt("year");

            items.add(title + " " + author + " (" + year + ")");
            categoryComboBox.addItem(title + " " + author + " (" + year + ")");
            categoryComboBox.putClientProperty(title + " " + author + " (" + year + ")", id);
        }
    }

    public static int deleteLiteratureFromEdition(int editionID, int id_lit) {
        try {
            pstmt2 = conn.prepareStatement(SQLRequests.DeleteLiteratureFromEdition);
            pstmt2.setInt(1, editionID);
            pstmt2.setInt(2, id_lit);
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
