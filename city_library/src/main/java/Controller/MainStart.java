package Controller;

import DBase.ConnectToOracle;
import View.ChoseLibraryFrame;
import View.MainMenu;

import java.sql.SQLException;

public class MainStart {
    public static void StartApp() throws SQLException {
        ConnectToOracle.ConnectionToDB();
        StartLibrary();
    }

    public static void StartLibrary() throws SQLException {
        ChoseLibraryFrame frame = new ChoseLibraryFrame();
        frame.ChoseLibraryFrame(ConnectToOracle.getLibrariesName());
    }

    public static void StartMainMenu() throws SQLException {
        MainMenu frame = new MainMenu();
        frame.MainMenu();
    }
}
