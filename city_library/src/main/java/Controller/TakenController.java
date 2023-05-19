package Controller;

import View.taken.returnEditionFrame;
import View.taken.takenEdition;

import java.sql.SQLException;

public class TakenController {
    public static void takenEdition() throws SQLException {
        takenEdition frame = new takenEdition();
        frame.takenEdition();
    }
    public static void returnEdition() throws SQLException {
        returnEditionFrame frame = new returnEditionFrame();
        frame.returnEditionFrame();
    }
}
