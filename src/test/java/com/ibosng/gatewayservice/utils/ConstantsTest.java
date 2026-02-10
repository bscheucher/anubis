package com.ibosng.gatewayservice.utils;

import com.ibosng.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest extends BaseIntegrationTest {

    @Test
    public void testConstantsValues() {
        assertEquals("Gateway Service", Constants.GATEWAY_SERVICE);
        assertEquals("FN_STAMMDATEN_ERFASSEN", Constants.FN_STAMMDATEN_ERFASSEN);
        assertEquals("FN_VERTRAGSDATEN_ERFASSEN", Constants.FN_VERTRAGSDATEN_ERFASSEN);
    }

    @Test
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        Throwable exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertTrue(exception.getCause() instanceof AssertionError);
        assertEquals("Cannot instantiate constant utility class", exception.getCause().getMessage());
    }
}