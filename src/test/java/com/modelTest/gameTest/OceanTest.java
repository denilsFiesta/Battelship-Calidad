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
    void testRandomPlace_FailedAfterMultipleAttempts() {
        List<Ship> ships = List.of(ship);
        
        try (MockedStatic<Ocean> mockedOcean = mockStatic(Ocean.class)) {
            mockedOcean.when(() -> Ocean.getRandomOcean(any(), any()))
                       .thenReturn(null);

            mockedOcean.when(() -> Ocean.randomPlace(any(), any()))
                       .thenCallRealMethod();

            Ocean result = Ocean.randomPlace(ships, ocean);

            assertNull(result, "El océano devuelto debería ser null cuando todos los intentos fallan y se alcanza el número máximo de intentos.");
            mockedOcean.verify(() -> Ocean.getRandomOcean(any(), any()), times(30));
        }
    }
}