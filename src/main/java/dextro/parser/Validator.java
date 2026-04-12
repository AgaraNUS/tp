package dextro.parser;

import dextro.exception.ParseException;
import dextro.model.Grade;

public class Validator {
    public static String validateName(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name is compulsory for create command. Use \"n/\" prefix");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Name too long, must be less than 100 chars");
        }

        if (!name.matches("^[A-Za-z ,()/.\\-@']+$")){
            throw new IllegalArgumentException(
                    "Name must contain only alphabetical symbols, spaces and special symbols:, ( ) . - / @ '");
        }

        return name.replaceAll("\\s+", " ");
    }

    public static String validatePhone(String phone) throws ParseException {
        if (phone == null || phone.isBlank()) {
            return null;
        }

        if (!phone.matches("\\d{8}")) {
            throw new ParseException("Phone number must be 8 numerical digits");
        }

        int firstDigit = phone.charAt(0) - '0';
        if (firstDigit < 8 || firstDigit > 9) {
            throw new ParseException("Phone number is not a valid Singapore mobile number");
        }

        return phone;
    }

    public static String validateEmail(String email) throws ParseException {
        if (email == null || email.isBlank()) {
            return null;
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$")) {
            throw new ParseException("Invalid email format");
        }

        return email.toLowerCase();
    }

    public static String validateAddress(String address) throws ParseException {
        if (address == null || address.isBlank()) {
            return null;
        }

        if (address.length() > 200) {
            throw new ParseException("Address too long, must be less than 200 chars");
        }

        return address.replaceAll("\\s+", " ");
    }

    public static String validateModuleCode(String moduleCode) throws ParseException {
        if (!moduleCode.matches("^[A-Z]{2,4}\\d{4}[A-Z0-9]{0,5}$")) {
            throw new ParseException("Invalid module code: " + moduleCode);
        } else {
            return moduleCode;
        }
    }

    public static Grade validateGrade(String grade) throws ParseException {
        try {
            return Grade.fromString(grade.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid grade: " + grade);
        }
    }
}
