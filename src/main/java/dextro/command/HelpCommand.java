package dextro.command;

import dextro.app.Storage;
import dextro.exception.CommandException;
import dextro.model.record.StudentDatabase;

public class HelpCommand implements Command {

    private static final String USER_GUIDE_URL = "https://ay2526s2-cs2113-t11-4.github.io/tp/UserGuide.html";

    private static final String HELP_MESSAGE = String.join("\n",
            "Available commands:",
            "  create n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [c/COURSE]",
            "  delete INDEX",
            "  edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [c/COURSE] [m/CODE/GRADE[/CREDITS]]",
            "  add INDEX CODE/GRADE[/CREDITS]",
            "  remove INDEX CODE",
            "  list",
            "  status INDEX",
            "  find KEYWORD",
            "  search c/COURSE | m/MODULE",
            "  sort [name|course|cap|mcs]",
            "  undo",
            "  help",
            "  exit",
            "",
            "For more information, see the user guide: " + USER_GUIDE_URL
    );

    @Override
    public CommandResult execute(StudentDatabase db, Storage storage) {
        return new CommandResult(HELP_MESSAGE);
    }

    @Override
    public CommandResult execute(StudentDatabase db) throws CommandException {
        return execute(db, null);
    }

    @Override
    public CommandResult undo(StudentDatabase db, Storage storage) throws CommandException {
        throw new CommandException("Help command cannot be undone");
    }

    @Override
    public CommandResult undo(StudentDatabase db) throws CommandException {
        throw new CommandException("Help command cannot be undone");
    }

    @Override
    public boolean isUndoable() {
        return false;
    }
}
