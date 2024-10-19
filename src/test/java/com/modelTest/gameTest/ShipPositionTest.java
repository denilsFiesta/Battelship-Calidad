package com.modelTest.gameTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import com.model.game.ocean.Ocean;
import com.model.game.ocean.Point;
import com.model.game.ocean.ShipPosition;
import com.model.ships.Ship;

public class ShipPositionTest {

    @Test
    public void testRandomDirection() {
        ShipPosition.Direction direction = ShipPosition.randomDirection();

        assertNotNull(direction, "La dirección no debe ser nula");
    }

    @Test
    public void testGetRandomShipPosition_Success() {
        Ocean oceanMock = mock(Ocean.class);
        Ship shipToPlace = mock(Ship.class); 

        when(oceanMock.getSizeHorizontal()).thenReturn(5);
        when(oceanMock.getSizeVertical()).thenReturn(5);

        ShipPosition shipPositionMock = new ShipPosition(new Point(2, 3), ShipPosition.Direction.RIGHT);

        when(oceanMock.tryPlaceShip(any(Ship.class), any(ShipPosition.class)))
                .thenReturn(shipPositionMock);

        ShipPosition result = ShipPosition.getRandomShipPosition(oceanMock, shipToPlace);

        assertNotNull(result, "Se esperaba una posición de barco válida");
        assertNotNull(result.getPosition(), "La posición no debe ser nula");
        assertNotNull(result.getDirection(), "La dirección no debe ser nula");
    }

    @Test
    public void testGetRandomShipPosition_Failure() {
        Ocean oceanMock = mock(Ocean.class);
        Ship shipToPlace = mock(Ship.class); 

        when(oceanMock.getSizeHorizontal()).thenReturn(5);
        when(oceanMock.getSizeVertical()).thenReturn(5);

        when(oceanMock.tryPlaceShip(any(Ship.class), any(ShipPosition.class)))
                .thenReturn(null);

        ShipPosition result = ShipPosition.getRandomShipPosition(oceanMock, shipToPlace);
        assertNull(result, "Se esperaba que no se pudiera colocar el barco, por lo tanto el resultado debe ser null");
    }

    @Test
    public void testGetX() {
        Point point = new Point(5, 8); 
        ShipPosition.Direction direction = ShipPosition.Direction.RIGHT;

        ShipPosition shipPosition = new ShipPosition(point, direction);
        assertEquals(5, shipPosition.getX(), "La coordenada X debería ser 5");
    }

    @Test
    public void testGetY() {
        Point point = new Point(4, 7); 
        ShipPosition.Direction direction = ShipPosition.Direction.LEFT;

        ShipPosition shipPosition = new ShipPosition(point, direction);
        assertEquals(7, shipPosition.getY(), "La coordenada Y debería ser 7");
    }

    @Test
    public void testGetPosition() {
        Point point = new Point(4, 5); 
        ShipPosition.Direction direction = ShipPosition.Direction.DOWN;

        ShipPosition shipPosition = new ShipPosition(point, direction);
        assertEquals(point, shipPosition.getPosition(), "La posición debería ser el punto (4, 5)");
    }
    
    @Test
    public void testGetDirection() {
        Point point = new Point(2, 3); 
        ShipPosition.Direction direction = ShipPosition.Direction.UP;

        ShipPosition shipPosition = new ShipPosition(point, direction);
        assertEquals(direction, shipPosition.getDirection(), "La dirección debería ser UP");
    }

    @Test
    public void testConstructor() {
        Point point = new Point(3, 6); 
        ShipPosition.Direction direction = ShipPosition.Direction.DOWN;

        ShipPosition shipPosition = new ShipPosition(point, direction);

        assertEquals(point, shipPosition.getPosition(), "La posición debería ser el punto (3, 6)");
        assertEquals(direction, shipPosition.getDirection(), "La dirección debería ser DOWN");
    }

}
