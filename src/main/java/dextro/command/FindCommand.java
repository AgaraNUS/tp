package dextro.command;

import dextro.app.Storage;
import dextro.exception.CommandException;
import dextro.model.Student;
import dextro.model.record.StudentDatabase;

import java.util.List;

public class FindCommand implements Command {

    private final String substring;

    public FindCommand(String substring) {
        this.substring = substring;
    }

    @Override
    public CommandResult execute(StudentDatabase db) throws CommandException {
        return null;
    }

    @Override
    public CommandResult execute(StudentDatabase db, Storage storage) throws CommandException {
        List<Student> students = db.getAllStudents();
        StringBuilder sb = new StringBuilder();
        boolean found = false;

        if (substring.equals(null)) {
            throw new CommandException("String to find is null");
        }

        sb.append("Here are the matching students in your list:\n");

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            int originalIndex = i + 1; // 1-based index for display

            if (matchesSubstring(student, substring)) {
                sb.append(originalIndex).append(". ").append(student.toString()).append("\n");
                found = true;
            }
        }

        if (!found) {
            return new CommandResult("No matching students found.");
        }

        return new CommandResult(sb.toString().trim(), false);
    }

    /**
     * Checks if the substring exists in any of the student's attributes,
     * excluding grade and module.
     */
    private boolean matchesSubstring(Student student, String substring) {
        String sub = substring.toLowerCase();

        if (student.getName().toLowerCase().contains(sub)) {
            return true;
        }
        if (student.getPhone().toLowerCase().contains(sub)) {
            return true;
        }
        if (student.getEmail().toLowerCase().contains(sub)) {
            return true;
        }
        if (student.getAddress().toLowerCase().contains(sub)) {
            return true;
        }
        if (student.getCourse().toLowerCase().contains(sub)) {
            return true;
        }

        return false;
    }

    @Override
    public CommandResult undo(StudentDatabase db) throws CommandException {
        throw new CommandException("Cannot undo find command");
    }

    @Override
    public CommandResult undo(StudentDatabase db, Storage storage) throws CommandException {
        return null;
    }

    @Override
    public boolean isUndoable() {
        return false;
    }
}
