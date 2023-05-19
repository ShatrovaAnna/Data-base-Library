import Controller.MainStart;
import DBase.ConnectToOracle;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        MainStart.StartApp();
        ConnectToOracle.checkReaderBeforeTaken(1);
    }
}
