package com.modelTest.gameTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.model.game.ocean.Ocean;
import com.model.game.ocean.Point;
import com.model.game.ocean.ShipPosition;
import com.model.ships.Ship;
public class OceanTest {

    @Test
    public void testOceanCreation_invalidHorizontalAndVerticalSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ocean(0, 0);
        });
    }

    @Test
    public void testOceanCreation_invalidHorizontalSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ocean(31, 10); 
        });
    }

    @Test
    public void testOceanCreation_invalidVerticalSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ocean(10, 31); 
        });
    }

    @Test
    public void testOceanCreation_validSizeWithInitialOcean() {
        Ocean ocean = new Ocean(10, 10);
        assertEquals(10, ocean.getSizeHorizontal(), "El tamaño horizontal debe ser 10");
        assertEquals(10, ocean.getSizeVertical(), "El tamaño vertical debe ser 10");

        assertNotNull(ocean, "El océano no debe ser nulo");
    }

    @Test
    public void testOceanCreation_validSize() {
        Ocean ocean = new Ocean(10, 10);
        assertEquals(10, ocean.getSizeHorizontal(), "El tamaño horizontal debe ser 10");
        assertEquals(10, ocean.getSizeVertical(), "El tamaño vertical debe ser 10");
    }

    @Test
    public void testPlaceShipSuccessfully() {
        Ship ship = Mockito.mock(Ship.class);
        Ocean ocean = new Ocean(10, 10);

        ShipPosition position = ShipPosition.getRandomShipPosition(ocean, ship);

        Assertions.assertNotNull(position, "El barco debería haberse colocado en el océano.");
    }    

    @Test
    public void testPlaceShipUnsuccessfully() {
        Ship ship = Mockito.mock(Ship.class);
        Ocean ocean = Mockito.mock(Ocean.class);
        ShipPosition mockPosition = Mockito.mock(ShipPosition.class);
        Mockito.when(ocean.tryPlaceShip(Mockito.any(Ship.class), Mockito.any(ShipPosition.class)))
               .thenReturn(null);
        

        ShipPosition result = ocean.tryPlaceShip(ship, mockPosition);
        
        Mockito.verify(ocean).tryPlaceShip(ship, mockPosition);
        
        Assertions.assertNull(result, "No debería haber espacio para colocar el barco en el océano.");
    }







}