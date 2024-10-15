package com.modelTest.gameTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.model.game.ocean.Ocean;
import com.model.game.ocean.Point;
import com.model.game.ocean.ShipPosition;
import com.model.game.ocean.ShipPosition.Direction;
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

    private Ocean ocean; 
    private Ship ship;    

    @BeforeEach
    void setUp() {
        ocean = new Ocean(10, 10); 
        ship = mock(Ship.class);
        when(ship.getLength()).thenReturn(5); 
    }


    @Test
    void testRandomPlace_SuccessfulPlacementFirstTry() {
        assertNotNull(ocean, "Ocean should be initialized");

        ShipPosition position = ShipPosition.getRandomShipPosition(ocean, ship);

        assertNotNull(position, "Position should not be null");
        assertTrue(position.getX() >= 0 && position.getX() < ocean.getSizeHorizontal(), 
                   "Position X should be within the ocean bounds");
        assertTrue(position.getY() >= 0 && position.getY() < ocean.getSizeVertical(), 
                   "Position Y should be within the ocean bounds");
    }

    @Test
    public void testRandomPlace_FailedAllAttempts() {
        List<Ship> ships = List.of(Mockito.mock(Ship.class));
        Ocean ocean = Mockito.mock(Ocean.class);
        
        Mockito.when(ocean.getSizeVertical()).thenReturn(10);
        Mockito.when(ocean.getSizeHorizontal()).thenReturn(10);
        Mockito.when(ocean.getRandomOcean(ships, ocean)).thenReturn(null);

        Ocean result = Ocean.randomPlace(ships, ocean);

        Assertions.assertNull(result, "El océano devuelto debería ser null después de alcanzar el número máximo de intentos fallidos.");
    }

    @Test
    void testRandomPlace_SuccessfulPlacementAfterAttempts() {
        Ship mockShip = mock(Ship.class);
        when(mockShip.getLength()).thenReturn(5);
        Ocean ocean = new Ocean(10, 10);
        ShipPosition validPosition = new ShipPosition(new Point(0, 0), ShipPosition.Direction.RIGHT);

        Ocean spyOcean = spy(ocean);
        doReturn(validPosition).when(spyOcean).tryPlaceShip(eq(mockShip), any(ShipPosition.class));

        Ocean result = Ocean.randomPlace(Collections.singletonList(mockShip), spyOcean);
        assertNotNull(result, "Result should not be null");
        verify(spyOcean, atLeastOnce()).tryPlaceShip(eq(mockShip), any(ShipPosition.class));
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
    public void testGetPointsOccupiedByShip_noShipInOcean() {
        Ocean ocean = new Ocean(5, 5);
        Ship ship = mock(Ship.class);  
        
        List<Point> result = ocean.getPointsOccupiedByShip(ship);
        assertTrue(result.isEmpty(), "Si no hay barcos en el océano, la lista de puntos debe estar vacía.");
    }

    @Test
    public void testGetPointsOccupiedByShip_singleShipInOcean() {
        Ocean ocean = new Ocean(3, 3);
        Ship ship = mock(Ship.class);
        when(ship.getLength()).thenReturn(1); 

        ShipPosition placedPosition = ocean.tryPlaceShip(ship, new ShipPosition(new Point(0, 0), ShipPosition.Direction.RIGHT));

        assertNotNull(placedPosition, "The ship should have been placed successfully");

        List<Point> result = ocean.getPointsOccupiedByShip(ship);

        assertEquals(1, result.size(), "There should be exactly one point in the list.");
        assertTrue(result.contains(new Point(0, 0)), "The list should contain the point (0, 0).");

        System.out.println("Ocean content:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(ocean.getShipByPosition(new Point(j, i)) != null ? "S " : "- ");
            }
            System.out.println();
        }
    }

    @Test
    public void testGetPointsOccupiedByShip_multiplePointsInOcean() {
        Ocean ocean = new Ocean(5, 5);
        Ship ship = mock(Ship.class);
        when(ship.getLength()).thenReturn(3);

        ocean.tryPlaceShip(ship, new ShipPosition(new Point(1, 1), ShipPosition.Direction.RIGHT));

        List<Point> result = ocean.getPointsOccupiedByShip(ship);
        
        assertEquals(3, result.size(), "Debería haber exactamente tres puntos en la lista.");
        assertTrue(result.contains(new Point(1, 1)), "La lista debería contener el punto (1, 1).");
        assertTrue(result.contains(new Point(2, 1)), "La lista debería contener el punto (2, 1).");
        assertTrue(result.contains(new Point(3, 1)), "La lista debería contener el punto (3, 1).");
    }

    @Test
    public void testGetPointsOccupiedByShip_shipOccupyingMultipleRows() {
        Ocean ocean = new Ocean(5, 5);
        Ship ship = mock(Ship.class);
        when(ship.getLength()).thenReturn(3);

        ocean.tryPlaceShip(ship, new ShipPosition(new Point(2, 1), ShipPosition.Direction.DOWN));

        List<Point> result = ocean.getPointsOccupiedByShip(ship);
        
        assertEquals(3, result.size(), "Debería haber exactamente tres puntos en la lista.");
        assertTrue(result.contains(new Point(2, 1)), "La lista debería contener el punto (2, 1).");
        assertTrue(result.contains(new Point(2, 2)), "La lista debería contener el punto (2, 2).");
        assertTrue(result.contains(new Point(2, 3)), "La lista debería contener el punto (2, 3).");
    }

    @Test
    void testGetRange_SinglePoint() {
        // Path: 1 -> 2 -> 3 -> F
        Point startPos = new Point(0, 0);
        Point endPos = new Point(0, 0);
        ShipPosition.Direction direction = ShipPosition.Direction.RIGHT;

        Point[] result = Point.getRange(startPos, endPos, direction);

        assertEquals(1, result.length);
        assertEquals(new Point(0, 0), result[0]);
    }

//         Point[] result = Point.getRange(start, end, direction);

//         assertNotNull(result, "El resultado no debería ser nulo");
//         assertEquals(expectedRange.length, result.length, "La longitud del array debería ser 6");

//         for (int i = 0; i < result.length; i++) {
//             assertEquals(expectedRange[i], result[i], "El punto en la posición " + i + " no es correcto");
//         }
//     }

//     @Test
//     public void testGetRange_DOWN() {
//         Point start = new Point(0, 5);
//         Point end = new Point(0, 0);
//         ShipPosition.Direction direction = ShipPosition.Direction.DOWN;
//         Point[] expectedRange = new Point[]{
//             new Point(0, 5),
//             new Point(0, 4),
//             new Point(0, 3),
//             new Point(0, 2),
//             new Point(0, 1),
//             new Point(0, 0)
//         };

//         Point[] result = Point.getRange(start, end, direction);

//         assertNotNull(result, "El resultado no debería ser nulo");
//         assertEquals(expectedRange.length, result.length, "La longitud del array debería ser 6");

//         for (int i = 0; i < result.length; i++) {
//             assertEquals(expectedRange[i], result[i], "El punto en la posición " + i + " no es correcto");
//         }
// }


//     @Test
//     public void testGetRange_LEFT() {
//         Point startPos = new Point(0, 0);
//         Point endPos = new Point(3, 0);
//         ShipPosition.Direction direction = ShipPosition.Direction.LEFT;

//         Point[] result = Point.getRange(startPos, endPos, direction);

//         assertArrayEquals(new Point[]{new Point(0, 0), new Point(-1, 0), new Point(-2, 0), new Point(-3, 0)}, result);
//     }
//     @Test
//     public void testGetRange_RIGHT() {
//         Point startPos = new Point(0, 0);
//         Point endPos = new Point(3, 0);
//         ShipPosition.Direction direction = ShipPosition.Direction.RIGHT;

//         Point[] result = Point.getRange(startPos, endPos, direction);

//         assertArrayEquals(new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)}, result);
//     }

    // @Test
    // public void testGetRange_WithCycle() {
    //     Point startPos = new Point(0, 0);
    //     Point endPos = new Point(0, 4); 
    //     ShipPosition.Direction direction = ShipPosition.Direction.UP;

    //     Point[] result = Point.getRange(startPos, endPos, direction);

    //     // Se espera un rango desde (0, 0) hasta (0, -4)
    //     assertArrayEquals(new Point[]{
    //         new Point(0, 0), 
    //         new Point(0, -1), 
    //         new Point(0, -2), 
    //         new Point(0, -3), 
    //         new Point(0, -4)
    //     }, result);
    // }



}


