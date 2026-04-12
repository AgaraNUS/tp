package dextro.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NormalizerTest {

    @Test
    void normalizeName_null_returnsNull() {
        assertNull(Normalizer.normalizeName(null));
    }

    @Test
    void normalizeName_lowercase_convertsToUppercase() {
        assertEquals("JOHN DOE", Normalizer.normalizeName("john doe"));
    }

    @Test
    void normalizeName_withLeadingTrailingSpaces_stripped() {
        assertEquals("JOHN", Normalizer.normalizeName("  john  "));
    }

    @Test
    void normalizeName_alreadyUppercase_unchanged() {
        assertEquals("JOHN", Normalizer.normalizeName("JOHN"));
    }

    @Test
    void normalizeEmail_null_returnsNull() {
        assertNull(Normalizer.normalizeEmail(null));
    }

    @Test
    void normalizeEmail_uppercase_convertsToLowercase() {
        assertEquals("john@mail.com", Normalizer.normalizeEmail("JOHN@MAIL.COM"));
    }

    @Test
    void normalizeEmail_withSpaces_stripped() {
        assertEquals("john@mail.com", Normalizer.normalizeEmail("  john@mail.com  "));
    }

    @Test
    void normalizeGeneral_null_returnsNull() {
        assertNull(Normalizer.normalizeGeneral(null));
    }

    @Test
    void normalizeGeneral_withSpaces_stripped() {
        assertEquals("hello", Normalizer.normalizeGeneral("  hello  "));
    }

    @Test
    void normalizeGeneral_preservesCase() {
        assertEquals("Orchard Road", Normalizer.normalizeGeneral("Orchard Road"));
    }
}
