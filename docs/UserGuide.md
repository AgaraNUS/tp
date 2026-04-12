# User Guide

## Introduction

The Dextro app acts as a management system and provides NUS admins a way to manage student records and progression, using command line language.

Dextro will track students' progress and provide insights on how a student is faring by storing the grades and modules taken by students and calculating metrics like CAP and improvement.

## Quick Start

1. Ensure Java 17 is installed. Mac users: Ensure you have the precise JDK version prescribed here.
1. Download latest `dextro.jar` file.
1. Copy the file to the folder you want to use as the home folder for Dextro.
1. Open a command terminal, cd into the folder you put the jar file in, and use the `java -jar dextro.jar` command to run the application.
1. Proceed to execute commands, refer to the Features below for details of each command.

## Features

### `create`
**Description:** Creates a new student.

**Syntax:**
```
create n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [c/COURSE]
```

**Example:**
```
> create n/John Doe p/87654321 e/john@hmail.com a/20 Orchard Road #23-11 c/Computer Science
----------------------------------------------------------------------------------------------------
Student created: John Doe
----------------------------------------------------------------------------------------------------
```
Optional fields not provided or provided as blank will be stored as `N.A.`. NAME is compulsory, while the rest are optional.
Repeated fields not allowed. Order of fields does not matter.

The following command is valid for the reasons explained below:
```
create n/John/ p/87654321 e/
```
- Since prefixes must be separated from previous text using at least one space, the above command does not trigger an error for duplicate prefixes as the name is parsed as John/.
- Leaving optional fields empty will not trigger an error, neither does not including the corresponding prefix.

**Duplicate entries:**

Creating a new entry with a phone number, email and address that matches an existing student will result in a confirmation prompt.
Users can then input "y" to confirm 


---

### `delete`
**Description:** Deletes a student's database record.

**Syntax:**
```
delete INDEX
```

**Example:**
```
> delete 1
----------------------------------------------------------------------------------------------------
Successfully deleted student:
John Doe/87654321/john_doe@hmail.com/N.A./N.A.
----------------------------------------------------------------------------------------------------
```



---

### `edit`
**Description:** Edits an existing student.

**Syntax:**
```
edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [c/COURSE] [m/CODE/GRADE[/CREDITS]]
```

**Example:**
```
edit 1 n/Jane Doe p/98765432 m/CG2027/B/2
```
Minimum of one field must be provided. Repeated fields not allowed. Order of fields does not matter.

If the requested edit does not modify anything, the program will output a successful result anyways.

Prefixes must be separated from text before with a space. Example:
```
edit 2 n/John/ p/87654321
```
The above command does not trigger an error for duplicate prefixes as the name is parsed as John/, similar to `create`.



---

### `list`
**Description:** Lists all students.

**Syntax:**
```
list
```
Example output:
```
> list
----------------------------------------------------------------------------------------------------
1: Bronathan Binglebong/98765432/N.A./N.A./Data Science and Analytics
2: Charlie Chocolate/87654321/willywonka@jmail.com/Lakseside/N.A.
3: Dalton Dog/83463726/clifford@red.com/Tampines St 83/Computer Science
----------------------------------------------------------------------------------------------------
```
---

### `find`
**Description:** Finds students by info across all fields

**Syntax:**
```
find KEYWORD
```

**Example:**
```
> find John
----------------------------------------------------------------------------------------------------
Here are the matching students in your list:
1. John/87654321/N.A./N.A./N.A.
----------------------------------------------------------------------------------------------------
```
```
> find 8765
----------------------------------------------------------------------------------------------------
Here are the matching students in your list:
1. John/87654321/N.A./N.A./N.A.
----------------------------------------------------------------------------------------------------
```
---

### `search`
**Description:** Searches students either by a specific field

**Syntax:**
```
search [c/COURSE] [m/CODE/GRADE]
```
Only one field can be provided. Repeated fields not allowed.

Prefixes must be separated from text before with a space.

---

### `status`
**Description:** Shows the GPA, degree completion progress and summary of a student.

**Syntax:**
```
status INDEX
```

---

### `undo`
**Description:** Reverts the last command.

**Syntax:**
```
undo
```

---

### `sort`
**Description:** Displays a sorted list of the existing database entries

**Syntax:**
```
sort [name|course|cap|mcs]
```

Displays a temporary list. Does not affect the order of the database entries. 

---


### `exit`
**Description:** Exits the application.

**Syntax:**
```
exit
```

---

## 4. Module Commands

### `add`
**Description:** Adds a module to a student.

**Syntax:**
```
add INDEX CODE/GRADE[/CREDITS]
```

**Example:**
```
> add 1 CS1010/A
----------------------------------------------------------------------------------------------------
Added module CS2113 (A) to john
----------------------------------------------------------------------------------------------------
> add 1 MA1511/B+/2
----------------------------------------------------------------------------------------------------
Added module MA1511 (B+) to john
----------------------------------------------------------------------------------------------------
```
Adding duplicate modules under the same student is allowed, to accommodate module retakes.

Modules are validated against a pattern that fits all existing NUS module codes; non-existent modules that follow the same format are allowed.

---

### `remove`
**Description:** Removes all modules matching the input from a student.

**Syntax:**
```
remove INDEX CODE
```

**Example:**
```
remove 1 Cs101
remove 1 cG1111a
```

CODE is case-insensitive

---

##  Data Field Info
- `n/` → Name
  - Must be less than 100 characters long.
  - Must contain only letters and special chars `, ( ) . - / @ '`
  - Case is stored as given.
- `p/` → Phone number
  - Only valid Singaporean mobile number is allowed, i.e. begins with 8 or 9.
- `e/` → Email address
  - Local portion allows only letters, numbers and special chars `. _ % + -`
  - Must contain a `@` symbol
  - Domain portion allows only letters, numbers, hyphens and at least one dot `.`
  - Case-insensitive, converted to lowercase when stored and displayed
- `a/` → Home address
  - Must be less than 200 characters long
  - Case is stored as given.
- `c/` → Course
  - Must be less than 100 characters long
  - Case-insensitive, converted to uppercase when stored and displayed
- `m/` → Module
  - CODE Must follow the same format as actual NUS modules. Strings that follow the same format are allowed.
  - CODE and GRADE case-insensitive, converted to uppercase when stored and displayed
- `INDEX` → Index shown in the list

---

##  Command Format Notes

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Navigate to /data/DextroStudentList.txt and replace the target computer's file.

## Command Summary

* Add student named John Lim Jun Jie with a phone number 88664422:
```
create n/John Lim Jun Jie p/88664422
```
* Add module CS2113/B+ to John's info:
```
find John Lim Jun Jie
add <student_id> m/CS2113/B+
```
