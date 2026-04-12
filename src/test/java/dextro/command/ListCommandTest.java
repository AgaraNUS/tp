package dextro.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dextro.app.Storage;
import dextro.exception.CommandException;
import dextro.model.Student;
import dextro.model.record.StudentDatabase;

class ListCommandTest {

    private StudentDatabase db;
    private Storage storage;

    @BeforeEach
    void setUp() {
        db = new StudentDatabase();
        storage = new Storage("./data/DextroStudentList.txt");
    }

    // ===== execute(db, storage) =====

    @Test
    void execute_emptyDb_returnsNoStudentsMessage() {
        CommandResult result = new ListCommand().execute(db, storage);
        assertEquals("No students found.", result.getMessage());
    }

    @Test
    void execute_oneStudent_listContainsStudent() {
        db.addStudent(new Student.Builder("ALICE").build());
        CommandResult result = new ListCommand().execute(db, storage);
        assertTrue(result.getMessage().contains("ALICE"));
    }

    @Test
    void execute_oneStudent_indexStartsAtOne() {
        db.addStudent(new Student.Builder("ALICE").build());
        CommandResult result = new ListCommand().execute(db, storage);
        assertTrue(result.getMessage().startsWith("1: "));
    }

    @Test
    void execute_multipleStudents_allListedWithCorrectIndices() {
        db.addStudent(new Student.Builder("ALICE").build());
        db.addStudent(new Student.Builder("BOB").build());
        db.addStudent(new Student.Builder("CHARLIE").build());
        CommandResult result = new ListCommand().execute(db, storage);
        String msg = result.getMessage();
        assertTrue(msg.contains("1: "));
        assertTrue(msg.contains("2: "));
        assertTrue(msg.contains("3: "));
        assertTrue(msg.contains("ALICE"));
        assertTrue(msg.contains("BOB"));
        assertTrue(msg.contains("CHARLIE"));
    }

    @Test
    void execute_studentWithOptionalFields_naDisplayed() {
        db.addStudent(new Student.Builder("ALICE").build());
        CommandResult result = new ListCommand().execute(db, storage);
        assertTrue(result.getMessage().contains("N.A."));
    }

    @Test
    void execute_studentWithAllFields_allFieldsInOutput() {
        db.addStudent(new Student.Builder("ALICE")
                .phone("91234567")
                .email("alice@mail.com")
                .address("Orchard Road")
                .course("CS")
                .build());
        CommandResult result = new ListCommand().execute(db, storage);
        String msg = result.getMessage();
        assertTrue(msg.contains("ALICE"));
        assertTrue(msg.contains("91234567"));
        assertTrue(msg.contains("alice@mail.com"));
        assertTrue(msg.contains("Orchard Road"));
        assertTrue(msg.contains("CS"));
    }

    @Test
    void execute_doesNotSetExitFlag() {
        db.addStudent(new Student.Builder("ALICE").build());
        CommandResult result = new ListCommand().execute(db, storage);
        assertFalse(result.shouldExit());
    }

    @Test
    void execute_emptyDb_doesNotSetExitFlag() {
        CommandResult result = new ListCommand().execute(db, storage);
        assertFalse(result.shouldExit());
    }

    @Test
    void execute_dbOnlyOverload_returnsNull() throws CommandException {
        assertNull(new ListCommand().execute(db));
    }

    // ===== undo =====

    @Test
    void undo_withStorage_throwsCommandException() {
        assertThrows(CommandException.class, () -> new ListCommand().undo(db, storage));
    }

    @Test
    void undo_singleOverload_returnsNull() throws CommandException {
        assertNull(new ListCommand().undo(db));
    }

    // ===== isUndoable =====

    @Test
    void isUndoable_alwaysReturnsFalse() {
        assertFalse(new ListCommand().isUndoable());
    }
}
