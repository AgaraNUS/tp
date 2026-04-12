package dextro.command;

import dextro.app.Storage;
import dextro.exception.CommandException;
import dextro.model.record.StudentDatabase;

public class ForceCreateCommand implements Command {
    private final CreateCommand inner;

    public ForceCreateCommand(String name, String phone, String email,
                              String address, String course) {
        this.inner = new CreateCommand(name, phone, email, address, course);
    }

    @Override
    public CommandResult execute(StudentDatabase db, Storage storage) {
        return inner.doCreate(db, storage);
    }

    @Override public CommandResult execute(StudentDatabase db) {
        return null;
    }

    @Override public CommandResult undo(StudentDatabase db) {
        return null;
    }

    @Override
    public CommandResult undo(StudentDatabase db, Storage storage) throws CommandException {
        return inner.undo(db, storage);
    }

    @Override public boolean isUndoable() {
        return true;
    }
}
