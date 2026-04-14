# Matthias Lim - Project Portfolio Page

## Overview

Dextro is a desktop application for managing student academic records, optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). It helps educators and administrators track student progress, manage course modules, and monitor academic performance efficiently.

## Summary of Contributions

### Code contributed
[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=zoom&zA=ChickenPancakeBeef&zR=AY2526S2-CS2113-T11-4%2Ftp%5Bmaster%5D&zACS=226.76727272727274&zS=2026-02-20T00%3A00%3A00&zFS=&zU=2026-04-03T23%3A59%3A59&zMG=false&zFTF=commit&zFGS=groupByRepos&zFR=false)



### Enhancements implemented

#### New Feature: Status Command
- **What it does**: Allows users to view detailed information about a specific student, including their CAP (Cumulative Average Point), total MCs (Modular Credits) completed, progress status, and comprehensive module statistics.
- **Justification**: This feature is essential for educators and administrators who need quick access to comprehensive student academic information without viewing all student details. It consolidates critical metrics in a single view, enabling informed decision-making about student academic standing and intervention needs.
- **Highlights**:
  - Calculates and displays student's current CAP with validation (0.0 to 5.0 range)
  - Shows total MCs completed out of 160 required for graduation
  - Categorizes progress status into 5 tiers: "Just Started" (<40 MCs), "On Track" (40-79 MCs), "Satisfactory" (80-119 MCs), "Good Progress" (120-159 MCs), and "Completed" (≥160 MCs)
  - Lists all modules sorted by grade (highest to lowest), then alphabetically by module code
  - Provides detailed module statistics including:
    - Grade distribution (e.g., "2 A's, 1 B+, 1 C")
    - Highest and lowest grades achieved with grade point values
  - Handles edge cases: empty module lists, non-graded modules (S/U), and invalid student indices
  - Uses assertions throughout for robust error detection during development
- **Implementation complexity**: The command required implementing complex sorting logic with custom comparators, grade aggregation algorithms, and integration with the Student model's CAP calculation methods. The module statistics feature involved creating hash maps for grade counting and stream-based sorting with multiple comparison criteria.
- **Code**: `src/main/java/dextro/command/StatusCommand.java` (169 lines including comprehensive statistics generation)

#### New Feature: Undo Command
- **What it does**: Allows users to revert the last undoable command that modified the student database, restoring the database to its previous state.
- **Justification**: This feature is critical for preventing data loss from accidental modifications and providing users with confidence when making changes to student records. In an educational context where data accuracy is paramount, the ability to quickly reverse mistakes prevents costly errors and reduces the need for manual data recovery from backups.
- **Highlights**:
  - Seamlessly integrates with the CommandHistory system to track and reverse operations
  - Only tracks truly undoable commands (read-only commands like `list`, `status`, `find` are not pushed to history)
  - Properly handles edge cases:
    - Attempting to undo when no commands are in history (returns warning message)
    - Prevents undoing an undo command itself (throws CommandException)
    - Validates all popped commands are undoable before execution
  - Works with storage layer to ensure undone operations are persisted to disk immediately
  - Each command type implements its own `undo()` logic for proper state reversal:
    - `CreateCommand`: Removes the added student at the stored index
    - `DeleteCommand`: Re-inserts the deleted student at its original position
    - `EditCommand`: Restores original student data from backup
    - `AddCommand`/`RemoveCommand`: Reverses module additions/removals
  - Uses assertion-based validation throughout for development-time safety
- **Implementation complexity**: Required designing a stack-based command history system that integrates across all command types. Each undoable command needed to store sufficient state information (e.g., deleted student object, original index, previous field values) to enable accurate reversal. The implementation also required careful coordination with the App's command execution flow to ensure commands are only pushed to history after successful execution without exceptions.
- **Code**:
  - `src/main/java/dextro/command/UndoCommand.java` (53 lines)
  - `src/main/java/dextro/command/CommandHistory.java` (31 lines)
  - Integration in `src/main/java/dextro/app/App.java` (command history push logic)

#### New Feature: Command History System
- **What it does**: Maintains a stack-based history of executed commands that can be undone.
- **Justification**: This is the underlying infrastructure that enables the undo feature. It tracks all undoable commands and manages the command stack.
- **Highlights**: Implemented using a Stack data structure for efficient LIFO (Last In First Out) operations. Integrates seamlessly with the Command interface through the `isUndoable()` method.
- **Code**: `src/main/java/dextro/command/CommandHistory.java:5-23`

#### Enhancement to Existing Features
- **CAP Calculation**: Implemented the `calculateCap()` method in the Student class to compute the Cumulative Average Point across all modules (`src/main/java/dextro/model/Student.java:67-78`)
- **Progress Status Tracking**: Implemented the `getProgressStatus()` method to categorize student progress based on MCs completed (`src/main/java/dextro/model/Student.java:85-98`)
- **Command Interface Enhancement**: Extended the Command interface to support undo operations by adding `undo()` and `isUndoable()` methods

### Contributions to testing

- **StatusCommandTest.java** (346 lines): Created comprehensive test suite covering:
  - Valid and invalid index handling (boundaries, negative, zero, out-of-range)
  - CAP calculation accuracy with various grade combinations
  - Progress status categorization for all 5 tiers (Just Started through Completed)
  - Module sorting verification (highest grade first)
  - Grade distribution counting accuracy
  - S/U grade exclusion from CAP statistics
  - Edge cases: empty database, no modules, multiple modules
  - A+/A ordering edge case for grades with identical CAP values

- **UndoCommandTest.java** (325 lines): Created comprehensive test suite covering:
  - Empty history warning messages
  - Single and multiple undo operations
  - Undo after create, delete, and edit commands
  - Mixed command sequences
  - Long chains of commands (50+ operations)
  - Partial undo sequences and history state management
  - Exception propagation from failing undo operations
  - Consecutive undos on empty history

- **CommandHistoryTest.java** (149 lines): Created test suite covering:
  - Empty state checking
  - Push/pop LIFO ordering verification
  - Clear functionality
  - Alternating push/pop sequences
  - Large-scale operations (100+ commands)
  - Edge cases and boundary conditions

### Contributions to the User Guide
- Added documentation for the Status command with:
  - Command format and parameters
  - Usage examples showing output format including CAP, MCs, and progress status
  - Explanation of progress status tiers and module statistics

- Added documentation for the Undo command with:
  - Command format
  - Usage examples demonstrating undo after create, delete, and edit operations
  - Explanation of undoable vs non-undoable commands
  - Warning message examples for edge cases

### Contributions to the Developer Guide
- Created comprehensive documentation for the Status Command feature including:
  - Class diagram showing relationships with other components (`StatusCommandClassDiagram.puml`)
  - Sequence diagram illustrating the execution flow with proper UML notation (`StatusCommandSD.puml`)
  - Implementation details explaining the CAP calculation logic and progress status categorization
- Created comprehensive documentation for the Undo Command feature including:
  - Class diagram showing the CommandHistory integration (`UndoCommandClassDiagram.puml`)
  - Sequence diagram illustrating the undo execution flow (`UndoCommandSD.puml`)
  - Design considerations for the stack-based command history system
- **Major contribution**: Debugged and improved the overall Developer Guide coherence by:
  - Fixed the Architecture Diagram to simplify structural dependencies (removed method call details, kept only component relationships)
  - Added arrow notation explanations (solid arrows for dependencies, dashed arrows for instantiation)
  - Updated **all sequence diagrams** across the entire project (14 diagrams total) to follow proper UML notation:
    - Added colons before all entity names (e.g., `:App`, `:Parser`, `:DeleteCommand`)
    - Added activation bars to show object lifetimes
    - Changed all return messages to use dashed arrows (`-->>`)
    - Showed command objects as separate lifelines instead of embedding them in App
  - Fixed the App Class Diagram to remove duplicate attribute/association representation and added multiplicities
  - Resolved UI component documentation contradiction about static vs instance methods
  - Regenerated all PNG diagram images from updated PlantUML source files

### Contributions to team-based tasks
- Set up project repository structure and initial documentation templates
- Reviewed and merged pull requests from team members
- Contributed to v1.0 and v2.0 release preparation and JAR packaging
- Coordinated Developer Guide improvements to address instructor feedback on UML diagram quality
- Helped team members debug issues with command execution flow and storage integration

### Review/mentoring contributions
- Reviewed pull requests from team members and provided constructive feedback on code quality and design decisions
- Assisted teammates with Git workflow, branch management, and merge conflict resolution
- Helped debug complex issues related to command execution and undo functionality across different command types
- Provided guidance on UML diagram creation and PlantUML syntax to ensure consistency across the Developer Guide
- Shared knowledge about assertion-based validation and defensive programming practices

### Contributions beyond the project team
- Actively participated in tutorial discussions and peer learning sessions
- Shared debugging techniques and testing strategies with classmates

