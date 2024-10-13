package com.modelTest.gameTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.model.game.ocean.Ocean;
public class OceanTest {

    @Test
    public void testOceanCreation_invalidHorizontalAndVerticalSize() {
        // Verificamos que se lanza IllegalArgumentException al intentar crear Ocean con tamaño inválido
        assertThrows(IllegalArgumentException.class, () -> {
            new Ocean(0, 0); // Ambos tamaños fuera del rango permitido
        });
    }

    @Test
    public void testOceanCreation_validSize() {
        Ocean ocean = new Ocean(10, 10);
        assertEquals(10, ocean.getSizeHorizontal(), "El tamaño horizontal debe ser 10");
        assertEquals(10, ocean.getSizeVertical(), "El tamaño vertical debe ser 10");
    }


}