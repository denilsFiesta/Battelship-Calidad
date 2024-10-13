package com.modelTest.gameTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.model.game.ocean.Ocean;
import com.model.game.ocean.Point;
import com.model.game.ocean.ShipPosition;
import com.model.ships.Ship;
import java.lang.reflect.Method; 

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


    @Test
    public void testPlaceShipAfterMaxAttempts() {
        Ocean ocean = new Ocean(2, 2); 

        Ship ship = Mockito.mock(Ship.class);
        Mockito.when(ship.getLength()).thenReturn(3); 

        boolean shipPlaced = false;

        for (int y = 0; y < ocean.getSizeVertical(); y++) {
            for (int x = 0; x < ocean.getSizeHorizontal(); x++) {
                // Probar cada dirección válida
                for (ShipPosition.Direction direction : ShipPosition.Direction.values()) {
                    ShipPosition position = new ShipPosition(new Point(x, y), direction);
                    ShipPosition result = ocean.tryPlaceShip(ship, position);
                    if (result != null) {
                        shipPlaced = true;
                        break; 
                    }
                }
                if (shipPlaced) break; 
            }
            if (shipPlaced) break; 
        }

        Assertions.assertFalse(shipPlaced, "No debería ser posible colocar un barco de tamaño 3 en un océano 2x2.");

        for (int y = 0; y < ocean.getSizeVertical(); y++) {
            for (int x = 0; x < ocean.getSizeHorizontal(); x++) {
                Assertions.assertTrue(ocean.isEmpty(new Point(x, y)),
                    "El océano debería estar vacío en la posición (" + x + ", " + y + ")");
            }
        }
    }

    @Test
    public void testGetPointsOccupiedByShip_nullShip() {
        Ocean ocean = new Ocean(10, 10); 
        List<Point> result = ocean.getPointsOccupiedByShip(null);
        assertTrue(result.isEmpty(), "Si el barco es null, la lista de puntos debería estar vacía.");
    }

    @Test
    public void testGetPointsOccupiedByShip_emptyOcean() {
        Ocean ocean = mock(Ocean.class);
        when(ocean.getSizeVertical()).thenReturn(0);
        when(ocean.getSizeHorizontal()).thenReturn(0);
        
        Ship ship = mock(Ship.class);
        List<Point> result = ocean.getPointsOccupiedByShip(ship);
        assertTrue(result.isEmpty(), "Si el océano está vacío, la lista de puntos debería estar vacía.");
    }

    @Test
    public void testGetPointsOccupiedByShip_noMatchingPoints() {
        Ocean ocean = new Ocean(2, 2);
        Ship ship = mock(Ship.class);

        List<Point> result = ocean.getPointsOccupiedByShip(ship);
        assertTrue(result.isEmpty(), "Si el barco no está en ninguna posición, la lista de puntos debería estar vacía.");
    }
}