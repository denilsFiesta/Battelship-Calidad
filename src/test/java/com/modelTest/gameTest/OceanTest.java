package com.modelTest.gameTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.model.game.ocean.Ocean;
public class OceanTest {

    @Test
    public void testOceanCreation_validSize() {
        Ocean ocean = new Ocean(10, 10);
        assertEquals(10, ocean.getSizeHorizontal(), "El tamaño horizontal debe ser 10");
        assertEquals(10, ocean.getSizeVertical(), "El tamaño vertical debe ser 10");
    }


}