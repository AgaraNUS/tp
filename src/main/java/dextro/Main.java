package dextro;

import dextro.app.App;
import dextro.parser.Parser;
import dextro.model.record.StudentDatabase;
import dextro.ui.Ui;

public class Main {

    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();
        StudentDatabase db = new StudentDatabase();

        App app = new App(ui, parser, db);
        app.run();
    }
}
