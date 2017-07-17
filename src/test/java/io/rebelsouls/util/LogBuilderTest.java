package io.rebelsouls.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class LogBuilderTest {

    @Test
    public void shouldFormatLogs() {
        String result = new LogBuilder().with("a", "b").build();
        assertEquals("a=\"b\"", result);
    }
    
    @Test
    public void logShouldNotEndWithComma() {
        String result = new LogBuilder().with("one", "field").build();
        assertFalse(result.endsWith(","));

        result = new LogBuilder().with("one", "field").with("two", "fields").build();
        assertFalse(result.endsWith(","));
    }

    @Test
    public void shouldChangeQuotes() {
        String result = new LogBuilder().with("one", "fi\"eld").build();
        assertTrue(result.lastIndexOf("\"") > result.indexOf("'"));
        assertTrue(result.indexOf("\"") < result.indexOf("'"));
    }

}
