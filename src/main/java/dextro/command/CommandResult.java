package dextro.command;

public class CommandResult {

    private final String message;
    private final boolean exit;
    private final boolean requiresConfirmation;
    private final Command pendingCommand;

    public CommandResult(String message) {
        this(message, false);
    }

    public CommandResult(String message, boolean exit) {
        this.message = message;
        this.exit = exit;
        this.requiresConfirmation = false;
        this.pendingCommand = null;
    }

    //For commands that require confirmation
    public CommandResult(String message, Command pendingCommand) {
        this.message = message;
        this.exit = false;
        this.requiresConfirmation = true;
        this.pendingCommand = pendingCommand;
    }

    public String getMessage() {
        return message;
    }

    public boolean shouldExit() {
        return exit;
    }

    public boolean requiresConfirmation() {
        return requiresConfirmation;
    }

    public Command getPendingCommand() {
        return pendingCommand;
    }
}
