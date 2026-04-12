package dextro.command;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import dextro.app.Storage;
import dextro.exception.CommandException;
import dextro.model.Student;
import dextro.model.record.StudentDatabase;

public class CreateCommand implements Command {

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String course;
    private int createdIndex = -1;

    public CreateCommand(String name, String phone, String email, String address, String course) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.course = course;
    }

    @Override
    public CommandResult execute(StudentDatabase db) throws CommandException {
        return null;
    }

    @Override
    public CommandResult execute(StudentDatabase db, Storage storage) {
        Map<Student, List<String>> conflicts = db.findDuplicateFields(name, phone, email, address);

        if (!conflicts.isEmpty()) {
            Set<String> allMatchedFields = new LinkedHashSet<>();
            conflicts.values().forEach(allMatchedFields::addAll);

            StringBuilder warning = new StringBuilder();
            warning.append("Warning: at least one student shares the following fields: ")
                    .append(String.join(", ", allMatchedFields))
                    .append("\n");

            for (Map.Entry<Student, List<String>> entry : conflicts.entrySet()) {
                Student s = entry.getKey();
                int displayIndex = db.getAllStudents().indexOf(s) + 1;
                warning.append(displayIndex).append(": ").append(s).append("\n");
            }

            warning.append("Type 'y' to confirm, or anything else to cancel.");
            return new CommandResult(warning.toString().trim(),
                    new ForceCreateCommand(name, phone, email, address, course));
        }

        return doCreate(db, storage);
    }

    CommandResult doCreate(StudentDatabase db, Storage storage) {
        Student student = new Student.Builder(name)
                .phone(phone).email(email).address(address).course(course).build();
        db.addStudent(student);
        storage.saveStudentList(db);
        createdIndex = db.getStudentCount() - 1;
        return new CommandResult("Student created: " + student.getName());
    }

    @Override
    public CommandResult undo(StudentDatabase db, Storage storage) throws CommandException {
        if (createdIndex == -1) {
            throw new CommandException("Cannot undo: create command was not executed");
        }
        if (createdIndex >= db.getStudentCount()) {
            throw new CommandException("Cannot undo: student no longer exists at the created index");
        }
        Student removed = db.removeStudent(createdIndex);
        return new CommandResult("Undone: Student creation of " + removed.getName());
    }

    @Override
    public CommandResult undo(StudentDatabase db) throws CommandException {
        return null;
    }

    @Override
    public boolean isUndoable() {
        return true;
    }
}
