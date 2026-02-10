package com.ibosng.gatewayservice.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class ParsersTest {

    @Test
    void testIsValidDate() {
        assertTrue(Parsers.isValidDate("01.01.2022"));
        assertTrue(Parsers.isValidDate("2022-01-01"));
        assertTrue(Parsers.isValidDate("010122"));
        assertFalse(Parsers.isValidDate("2022-01-32")); // Invalid day
        assertFalse(Parsers.isValidDate("2022-13-01")); // Invalid month
        assertFalse(Parsers.isValidDate("invalid-date"));
    }

    @Test
    void testParseDate() {
        assertEquals(LocalDate.of(2022, 1, 1), Parsers.parseDate("01.01.2022"));
        assertEquals(LocalDate.of(2022, 1, 1), Parsers.parseDate("2022-01-01"));
        assertEquals(LocalDate.of(2022, 1, 1), Parsers.parseDate("010122"));
        assertNull(Parsers.parseDate("2022-01-32")); // Invalid day
        assertNull(Parsers.parseDate("2022-13-01")); // Invalid month
        assertNull(Parsers.parseDate("invalid-date"));
    }

}
