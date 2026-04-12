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

class CreateCommandTest {

    private StudentDatabase db;
    private Storage storage;

    @BeforeEach
    void setUp() {
        db = new StudentDatabase();
        storage = new Storage("./data/DextroStudentList.txt");
    }

    // ===== execute(db, storage) — normal creation =====

    @Test
    void execute_validInput_studentAddedToDb() {
        CreateCommand cmd = new CreateCommand("JOHN", "91234567", "john@mail.com", "Orchard Road", "CS");
        cmd.execute(db, storage);
        assertEquals(1, db.getStudentCount());
    }

    @Test
    void execute_validInput_messageContainsName() {
        CreateCommand cmd = new CreateCommand("JOHN", "91234567", "john@mail.com", "Orchard Road", "CS");
        CommandResult result = cmd.execute(db, storage);
        assertEquals("Student created: JOHN", result.getMessage());
    }

    @Test
    void execute_validInput_allFieldsStoredCorrectly() {
        CreateCommand cmd = new CreateCommand("JOHN", "91234567", "john@mail.com", "Orchard Road", "CS");
        cmd.execute(db, storage);
        Student s = db.getStudent(0);
        assertEquals("JOHN", s.getName());
        assertEquals("91234567", s.getPhone());
        assertEquals("john@mail.com", s.getEmail());
        assertEquals("Orchard Road", s.getAddress());
        assertEquals("CS", s.getCourse());
    }

    @Test
    void execute_optionalFieldsNull_studentCreatedWithNaDisplayed() {
        CreateCommand cmd = new CreateCommand("JOHN", null, null, null, null);
        cmd.execute(db, storage);
        Student s = db.getStudent(0);
        assertEquals("N.A.", s.getPhone());
        assertEquals("N.A.", s.getEmail());
        assertEquals("N.A.", s.getAddress());
        assertEquals("N.A.", s.getCourse());
    }

    @Test
    void execute_multipleStudents_allAdded() {
        new CreateCommand("ALICE", "91234567", "a@mail.com", "Addr1", "CS").execute(db, storage);
        new CreateCommand("BOB", "98765432", "b@mail.com", "Addr2", "CEG").execute(db, storage);
        assertEquals(2, db.getStudentCount());
        assertEquals("ALICE", db.getStudent(0).getName());
        assertEquals("BOB", db.getStudent(1).getName());
    }

    // ===== execute(db, storage) — duplicate detection =====

    @Test
    void execute_duplicateName_returnsConfirmationResult() {
        new CreateCommand("JOHN", null, null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("JOHN", null, null, null, null);
        CommandResult result = cmd.execute(db, storage);
        assertTrue(result.requiresConfirmation());
    }

    @Test
    void execute_duplicateName_warningMessageContainsName() {
        new CreateCommand("JOHN", null, null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("JOHN", null, null, null, null);
        CommandResult result = cmd.execute(db, storage);
        assertTrue(result.getMessage().contains("name"));
    }

    @Test
    void execute_duplicatePhone_returnsConfirmationResult() {
        new CreateCommand("ALICE", "91234567", null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("BOB", "91234567", null, null, null);
        CommandResult result = cmd.execute(db, storage);
        assertTrue(result.requiresConfirmation());
        assertTrue(result.getMessage().contains("phone"));
    }

    @Test
    void execute_duplicateEmail_returnsConfirmationResult() {
        new CreateCommand("ALICE", null, "shared@mail.com", null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("BOB", null, "shared@mail.com", null, null);
        CommandResult result = cmd.execute(db, storage);
        assertTrue(result.requiresConfirmation());
        assertTrue(result.getMessage().contains("email"));
    }

    @Test
    void execute_duplicateAddress_returnsConfirmationResult() {
        new CreateCommand("ALICE", null, null, "Orchard Road", null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("BOB", null, null, "Orchard Road", null);
        CommandResult result = cmd.execute(db, storage);
        assertTrue(result.requiresConfirmation());
        assertTrue(result.getMessage().contains("address"));
    }

    @Test
    void execute_duplicateDetected_pendingCommandIsForceCreate() {
        new CreateCommand("JOHN", null, null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("JOHN", null, null, null, null);
        CommandResult result = cmd.execute(db, storage);
        assertTrue(result.getPendingCommand() instanceof ForceCreateCommand);
    }

    @Test
    void execute_duplicateDetected_dbNotYetModified() {
        new CreateCommand("JOHN", null, null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("JOHN", null, null, null, null);
        cmd.execute(db, storage);
        // duplicate check should not have added the student yet
        assertEquals(1, db.getStudentCount());
    }

    @Test
    void execute_noDuplicate_noConfirmationRequired() {
        new CreateCommand("ALICE", "91234567", null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("BOB", "98765432", null, null, null);
        CommandResult result = cmd.execute(db, storage);
        assertFalse(result.requiresConfirmation());
    }

    @Test
    void execute_emptyPhoneNotTreatedAsDuplicate() {
        // Two students with no phone should not trigger phone duplicate
        new CreateCommand("ALICE", null, null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("BOB", null, null, null, null);
        CommandResult result = cmd.execute(db, storage);
        // only name would match if same, here names differ so no confirmation
        assertFalse(result.requiresConfirmation());
    }

    // ===== undo =====

    @Test
    void undo_afterExecute_removesStudentFromDb() throws CommandException {
        CreateCommand cmd = new CreateCommand("JOHN", "91234567", "john@mail.com", "Addr", "CS");
        cmd.execute(db, storage);
        assertEquals(1, db.getStudentCount());
        cmd.undo(db, storage);
        assertEquals(0, db.getStudentCount());
    }

    @Test
    void undo_afterExecute_messageContainsName() throws CommandException {
        CreateCommand cmd = new CreateCommand("JOHN", "91234567", "john@mail.com", "Addr", "CS");
        cmd.execute(db, storage);
        CommandResult result = cmd.undo(db, storage);
        assertTrue(result.getMessage().contains("JOHN"));
    }

    @Test
    void undo_withoutExecute_throwsCommandException() {
        CreateCommand cmd = new CreateCommand("JOHN", "91234567", "john@mail.com", "Addr", "CS");
        assertThrows(CommandException.class, () -> cmd.undo(db, storage));
    }

    @Test
    void undo_afterDuplicateCheckOnly_throwsCommandException() {
        // execute() returned confirmation, doCreate was never called
        new CreateCommand("JOHN", null, null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("JOHN", null, null, null, null);
        cmd.execute(db, storage); // returns requiresConfirmation, does not create
        assertThrows(CommandException.class, () -> cmd.undo(db, storage));
    }

    @Test
    void undo_removesCorrectStudentWhenMultipleExist() throws CommandException {
        new CreateCommand("ALICE", "91234567", null, null, null).execute(db, storage);
        CreateCommand cmd = new CreateCommand("BOB", "98765432", null, null, null);
        cmd.execute(db, storage);
        assertEquals(2, db.getStudentCount());
        cmd.undo(db, storage);
        assertEquals(1, db.getStudentCount());
        assertEquals("ALICE", db.getStudent(0).getName());
    }

    @Test
    void undo_singleOverload_returnsNull() throws CommandException {
        CreateCommand cmd = new CreateCommand("JOHN", null, null, null, null);
        assertNull(cmd.undo(db));
    }

    // ===== isUndoable / execute(db) =====

    @Test
    void isUndoable_alwaysReturnsTrue() {
        assertTrue(new CreateCommand("JOHN", null, null, null, null).isUndoable());
    }

    @Test
    void execute_dbOnlyOverload_returnsNull() throws CommandException {
        CreateCommand cmd = new CreateCommand("JOHN", null, null, null, null);
        assertNull(cmd.execute(db));
    }
}
