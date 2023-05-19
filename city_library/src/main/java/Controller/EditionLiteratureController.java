package Controller;

import View.editionsAndLiterature.*;

import java.sql.SQLException;

public class EditionLiteratureController {
    public static changeEdition frameChangeEdition;

    public static void addEdition() throws SQLException {
        addEditionFrame frame = new addEditionFrame();
        frame.addEditionFrame();
    }
    public static void addLiterature() throws SQLException {
        addLiteratureFrame frame = new addLiteratureFrame();
        frame.addLiteratureFrame();
    }
    public static void changeEditionStart() throws SQLException {
        frameChangeEdition = new changeEdition();
    }
    public static void changeLiteratureStart() throws SQLException {
        new changeLiterature();
    }

}
