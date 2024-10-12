package test.oceanTest.modelTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import battleship.model.game.ocean.Ocean;

public class OceanTest {

    @Test
    public void testOceanCreation_validSize() {
        Ocean ocean = new Ocean(10, 10);
        assertEquals("El tamaño horizontal debe ser 10", 10, ocean.getSizeHorizontal());
        assertEquals("El tamaño vertical debe ser 10", 10, ocean.getSizeVertical());
    }

}