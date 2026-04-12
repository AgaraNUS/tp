package dextro.parser;

public class Normalizer {
    public static String normalizeName(String name) {
        return name == null ? null : name.strip().replaceAll("\\s+", " ").trim();
    }

    public static String normalizePhone(String name) {
        return name == null ? null : name.strip();
    }

    public static String normalizeEmail(String email) {
        return email == null ? null : email.strip().toLowerCase();
    }

    public static String normalizeAddress(String name) {
        return name == null ? null : name.strip().replaceAll("\\s+", " ").trim();
    }

    public static String normalizeCourse(String name) {
        return name == null ? null : name.strip().replaceAll("\\s+", " ").trim();
    }

    public static String normalizeModuleCode(String name) {
        return name == null ? null : name.strip().toUpperCase();
    }

    public static String normalizeGrade(String name) {
        return name == null ? null : name.strip().toUpperCase();
    }

    public static String normalizeGeneral(String value) {
        return value == null ? null : value.strip();
    }

}
