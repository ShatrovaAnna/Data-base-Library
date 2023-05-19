package Controller;

import DBase.ConnectToOracle;
import View.users.*;

import java.sql.SQLException;

import static View.users.ChangeWorker.addChangeWorkerFrame;

public class PeopleController {

    public static void addReaderCategories() throws SQLException {
        ReaderCategory frame = new ReaderCategory();
        frame.ReaderCategory(ConnectToOracle.getReaderCategories());
    }

    public static void addReader() throws SQLException {
        AddReader frame = new AddReader();
        frame.AddReader(ConnectToOracle.getAttribute());
    }

    public static void addWorker() throws SQLException {
        AddWorker frame = new AddWorker();
        frame.AddWorker();
    }
    public static void changerReader() throws SQLException {
        new ChangeReader();
    }

    public static void changerWorker() throws SQLException {
        addChangeWorkerFrame();
    }
}
