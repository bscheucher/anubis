package com.ibosng.gatewayservice.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Test
    public void testHome() {
        String expectedResponse = "This is the test home for the ibosng-backend!!!";

        String response = homeController.home();

        assertEquals(expectedResponse, response);
    }
}
