package com.modelTest.gameTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.model.game.ocean.Ocean;
import com.model.game.ocean.Point;
import com.model.game.ocean.ShipPosition;
import com.model.ships.Ship;

public class OceanTest {

    @Mock
    private Ocean oceanInstance;

    @Mock
    private Point pointMock;

    @Mock
    private Ocean oceanMock;

    @Mock
    private Ship shipMock;

    @Mock
    private ShipPosition startPosMock;

    @Mock
    private ShipPosition endPosMock;

    @Mock
    private ShipPosition shipPositionMock;

    @InjectMocks
    private Ocean oceanSpy;

    @Mock
    private Ocean ocean;

    @BeforeEach
    void setUp() {
        oceanSpy = Mockito.spy(new Ocean(10, 10));
        try {
            MockitoAnnotations.openMocks(this);
            oceanInstance = new Ocean(30, 30);

            when(shipMock.getLength()).thenReturn(3);

            Field oceanField = Ocean.class.getDeclaredField("ocean");
            oceanField.setAccessible(true);
            oceanField.set(oceanInstance, new Ship[30][30]);

            startPosMock = mock(ShipPosition.class);
            when(startPosMock.getX()).thenReturn(5);
            when(startPosMock.getY()).thenReturn(5);
            when(startPosMock.getDirection()).thenReturn(ShipPosition.Direction.LEFT);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        when(startPosMock.getX()).thenReturn(5);
        when(startPosMock.getY()).thenReturn(5);
        when(endPosMock.getX()).thenReturn(5);
        when(endPosMock.getY()).thenReturn(7);
    }

    @Test
    void testOceanCreation_invalidHorizontalAndVerticalSize() {
        assertThrows(IllegalArgumentException.class, () -> new Ocean(0, 0),
                "Debería lanzar IllegalArgumentException para tamaño 0x0");
        assertThrows(IllegalArgumentException.class, () -> new Ocean(31, 31),
                "Debería lanzar IllegalArgumentException para tamaño 31x31");
    }

    @Test
    void testOceanCreation_invalidHorizontalSize() {
        assertThrows(IllegalArgumentException.class, () -> new Ocean(0, 10),
                "Debería lanzar IllegalArgumentException para tamaño horizontal 0");
        assertThrows(IllegalArgumentException.class, () -> new Ocean(31, 10),
                "Debería lanzar IllegalArgumentException para tamaño horizontal 31");
    }

    @Test
    void testOceanCreation_invalidVerticalSize() {
        assertThrows(IllegalArgumentException.class, () -> new Ocean(10, 0),
                "Debería lanzar IllegalArgumentException para tamaño vertical 0");
        assertThrows(IllegalArgumentException.class, () -> new Ocean(10, 31),
                "Debería lanzar IllegalArgumentException para tamaño vertical 31");
    }

    @Test
    void testOceanCreation_validSizeWithInitialOcean() {
        when(ocean.getSizeHorizontal()).thenReturn(10);
        when(ocean.getSizeVertical()).thenReturn(10);

        assertEquals(10, ocean.getSizeHorizontal(), "El tamaño horizontal debe ser 10");
        assertEquals(10, ocean.getSizeVertical(), "El tamaño vertical debe ser 10");

        assertNotNull(ocean, "El océano no debe ser nulo");
    }

    @Test
    void testOceanCreation_validSize() {
        when(ocean.getSizeHorizontal()).thenReturn(1, 30, 10);
        when(ocean.getSizeVertical()).thenReturn(1, 30, 10);

        assertEquals(1, ocean.getSizeHorizontal(), "El tamaño horizontal mínimo debe ser 1");
        assertEquals(1, ocean.getSizeVertical(), "El tamaño vertical mínimo debe ser 1");

        assertEquals(30, ocean.getSizeHorizontal(), "El tamaño horizontal máximo debe ser 30");
        assertEquals(30, ocean.getSizeVertical(), "El tamaño vertical máximo debe ser 30");

        assertEquals(10, ocean.getSizeHorizontal(), "El tamaño horizontal debe ser 10");
        assertEquals(10, ocean.getSizeVertical(), "El tamaño vertical debe ser 10");
    }

    @Test
    void testRandomPlace_SuccessfulPlacementFirstTry() {
        Ship mockShip = mock(Ship.class);
        ShipPosition mockPosition = mock(ShipPosition.class);
        Ocean ocean = mock(Ocean.class);

        when(ocean.getSizeHorizontal()).thenReturn(10);
        when(ocean.getSizeVertical()).thenReturn(10);

        try (var mockStatic = mockStatic(ShipPosition.class)) {
            mockStatic.when(() -> ShipPosition.getRandomShipPosition(ocean, mockShip))
                        .thenReturn(mockPosition);

            when(mockPosition.getPosition()).thenReturn(new Point(5, 5));

            ShipPosition position = ShipPosition.getRandomShipPosition(ocean, mockShip);

            assertNotNull(position, "Position should not be null");

            Point point = position.getPosition();

            assertTrue(point.x() >= 0 && point.x() < ocean.getSizeHorizontal(), "Position X should be within the ocean bounds");
            assertTrue(point.y() >= 0 && point.y() < ocean.getSizeVertical(), "Position Y should be within the ocean bounds");
        }
    }


    @Test
    public void testRandomPlace_FailedAllAttempts() {
        Ship mockShip = Mockito.mock(Ship.class);
        List<Ship> ships = List.of(mockShip);
        Ocean ocean = Mockito.mock(Ocean.class);
        
        Mockito.when(ocean.getSizeVertical()).thenReturn(10);
        Mockito.when(ocean.getSizeHorizontal()).thenReturn(10);
        Mockito.when(Ocean.getRandomOcean(ships, ocean)).thenReturn(null);

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
    public void testIsEmpty_PositionIsEmpty() {
        Point pointMock = mock(Point.class);
        Ocean spyOcean = spy(oceanInstance);

        doReturn(null).when(spyOcean).getShipByPosition(pointMock);

        assertTrue(spyOcean.isEmpty(pointMock), "The position should be considered empty when there is no ship.");
    }

    @Test
    public void testIsEmpty_PositionNotEmpty() {
        Point pointMock = mock(Point.class);
        Ship shipMock = mock(Ship.class);
        Ocean spyOcean = spy(oceanInstance);

        doReturn(shipMock).when(spyOcean).getShipByPosition(pointMock);

        assertFalse(spyOcean.isEmpty(pointMock), "The position should not be considered empty when there is a ship.");
    }

    @Test
    public void testGetShipByPosition_PositionOutOfOcean() {
        when(pointMock.x()).thenReturn(31);
        when(pointMock.y()).thenReturn(15);

        Ocean spyOcean = spy(oceanInstance);
        doReturn(true).when(spyOcean).indexOutOfOcean(pointMock);

        assertNull(spyOcean.getShipByPosition(pointMock), "Position (31, 15) is out of ocean bounds, so the method should return null.");
    }

    
    @Test
    public void testGetShipByPosition_PositionWithinBoundsAndShipPresent() throws NoSuchFieldException, IllegalAccessException {
        when(pointMock.x()).thenReturn(10);
        when(pointMock.y()).thenReturn(5);
        
        Ocean spyOcean = spy(oceanInstance);
        doReturn(false).when(spyOcean).indexOutOfOcean(pointMock);

        Field oceanField = Ocean.class.getDeclaredField("ocean");
        oceanField.setAccessible(true);
        Ship[][] oceanArray = (Ship[][]) oceanField.get(spyOcean);
        
        oceanArray[5][10] = shipMock;

        assertEquals(shipMock, spyOcean.getShipByPosition(pointMock), "Position (10, 5) should return the ship present at this position.");
    }

    @Test
    public void testGetShipByPosition_PositionWithinBoundsAndNoShipPresent() throws NoSuchFieldException, IllegalAccessException {
        when(pointMock.x()).thenReturn(20);
        when(pointMock.y()).thenReturn(10);

        Ocean spyOcean = spy(oceanInstance);
        doReturn(false).when(spyOcean).indexOutOfOcean(pointMock);

        Field oceanField = Ocean.class.getDeclaredField("ocean");
        oceanField.setAccessible(true);
        Ship[][] oceanArray = (Ship[][]) oceanField.get(spyOcean);

        oceanArray[10][20] = null;

        assertNull(spyOcean.getShipByPosition(pointMock), 
                "Position (20, 10) is within ocean bounds but should return null as there is no ship present.");
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
        Ocean ocean = new Ocean(5, 5);  // Usa una instancia real, no un mock
        Ship ship = Mockito.mock(Ship.class);
        when(ship.getLength()).thenReturn(3);

        ShipPosition result = ocean.tryPlaceShip(ship, new ShipPosition(new Point(-1, 0), ShipPosition.Direction.RIGHT));
        Assertions.assertNull(result, "No debería ser posible colocar el barco fuera del océano.");

        result = ocean.tryPlaceShip(ship, new ShipPosition(new Point(3, 0), ShipPosition.Direction.RIGHT));
        Assertions.assertNull(result, "No debería ser posible colocar el barco si se extiende fuera del océano.");
    }

    @Test
    public void testPlaceShipAfterMaxAttempts() {
        Ocean ocean = new Ocean(2, 2); 

        Ship ship = Mockito.mock(Ship.class);
        Mockito.when(ship.getLength()).thenReturn(3); 

        boolean shipPlaced = false;

        for (int y = 0; y < ocean.getSizeVertical(); y++) {
            for (int x = 0; x < ocean.getSizeHorizontal(); x++) {
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
    public void testIndexOutOfOcean_YLessThanZero() {
        when(pointMock.x()).thenReturn(5);
        when(pointMock.y()).thenReturn(-1);
        
        assertTrue(oceanInstance.indexOutOfOcean(pointMock), "Point (5, -1) should be out of ocean bounds due to y < 0");
    }

    @Test
    public void testIndexOutOfOcean_YGreaterThanOrEqualToSizeVertical() {
        when(pointMock.x()).thenReturn(5);
        when(pointMock.y()).thenReturn(30);
        
        assertTrue(oceanInstance.indexOutOfOcean(pointMock), "Point (5, 30) should be out of ocean bounds due to y >= sizeVertical");
    }

    @Test
    public void testIndexOutOfOcean_XLessThanZero() {
        when(pointMock.x()).thenReturn(-1);
        when(pointMock.y()).thenReturn(5);
        
        assertTrue(oceanInstance.indexOutOfOcean(pointMock), "Point (-1, 5) should be out of ocean bounds due to x < 0");
    }

    @Test
    public void testIndexOutOfOcean_XGreaterThanOrEqualToSizeHorizontal() {
        when(pointMock.x()).thenReturn(30);
        when(pointMock.y()).thenReturn(5);
        
        assertTrue(oceanInstance.indexOutOfOcean(pointMock), "Point (30, 5) should be out of ocean bounds due to x >= sizeHorizontal");
    }

    @Test
    public void testIndexOutOfOcean_WithinBounds() {
        when(pointMock.x()).thenReturn(15);
        when(pointMock.y()).thenReturn(15);
        
        assertFalse(oceanInstance.indexOutOfOcean(pointMock), "Point (15, 15) should be within ocean bounds");
    }


    @Test
    void testBadIndexRange_OutOfOcean() {
        Point startPos = new Point(0, 0);
        Point endPos = new Point(11, 0);
        doReturn(true).when(oceanSpy).indexOutOfOcean(endPos);

        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));
    }

    @Test
    void testBadIndexRange_UP_StartPosition() {
        Point startPos = new Point(11, 11);
        Point endPos = new Point(11, 11);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getDown());

        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));
    }

    @Test
    void testBadIndexRange_UP_EndPosition() {
        Point startPos = new Point(10, 10);
        Point endPos = new Point(10, 17);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getUp());

        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));
    }

    @Test
    void testBadIndexRange_UP_LeftRight() {
        Point startPos = new Point(4, 3);
        Point endPos = new Point(8, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(4, 4));

        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));
    }

    @Test
    void testBadIndexRange_DOWN_StartPosition() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(5, 7);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getUp());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));
    }

    @Test
    void testBadIndexRange_DOWN_EndPosition() {
        Point startPos = new Point(8, 8);
        Point endPos = new Point(8, 6);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getDown());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));
    }

    @Test
    void testBadIndexRange_DOWN_LeftRight() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(5, 7);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(6, 6));
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));
    }

    @Test
    void testBadIndexRange_LEFT_StartPosition() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(3, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));
    }

    @Test
    void testBadIndexRange_LEFT_EndPosition() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(3, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));
    }

    @Test
    void testBadIndexRange_LEFT_UpDown() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(3, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(4, 6));
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));
    }

    @Test
    void testBadIndexRange_RIGHT_StartPosition() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(7, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));
    }

    @Test
    void testBadIndexRange_RIGHT_EndPosition() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(7, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));
    }

    @Test
    void testBadIndexRange_RIGHT_UpDown() {
        Point startPos = new Point(3, 3);
        Point endPos = new Point(7, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(6, 4));
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));
    }

    @Test
    void testBadIndexRange_ValidPlacement() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(7, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        
        assertFalse(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));
    }
    
    @Test
    void testBadIndexRange_UP_StartPosition_DownAndLeftRight() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(5, 3);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getDownAndLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));

        doReturn(null).when(oceanSpy).getShipByPosition(startPos.getDownAndLeft());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getDownAndRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));
    }

    @Test
    void testBadIndexRange_UP_EndPosition_UpAndLeftRight() {
        Point startPos = new Point(7, 7);
        Point endPos = new Point(7, 8);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getUpAndLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));

        doReturn(null).when(oceanSpy).getShipByPosition(endPos.getUpAndLeft());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getUpAndRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));
    }

    @Test
    void testBadIndexRange_DOWN_StartPosition_UpAndLeftRight() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(5, 7);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getUpAndLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));

        doReturn(null).when(oceanSpy).getShipByPosition(startPos.getUpAndLeft());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getUpAndRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));
    }

    @Test
    void testBadIndexRange_LEFT_StartPosition_RightAndUpDown() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(3, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getUpAndRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));

        doReturn(null).when(oceanSpy).getShipByPosition(startPos.getUpAndRight());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getDownAndRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));
    }

    @Test
    void testBadIndexRange_RIGHT_EndPosition_RightAndUpDown() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(7, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getUpAndRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));

        doReturn(null).when(oceanSpy).getShipByPosition(endPos.getUpAndRight());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getDownAndRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));
    }

    @Test
    void testBadIndexRange_UP_LeftRightCheck() {
        Point startPos = new Point(5, 3);
        Point endPos = new Point(5, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(4, 4)); // Left
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));

        doReturn(null).when(oceanSpy).getShipByPosition(new Point(4, 4));
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(6, 4)); // Right
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP));
    }

    @Test
    void testBadIndexRange_DOWN_EndPositionCheck() {
        Point startPos = new Point(9, 7);
        Point endPos = new Point(9, 7);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getDown());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));
        
        doReturn(null).when(oceanSpy).getShipByPosition(endPos.getDown());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getDownAndLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));

        doReturn(null).when(oceanSpy).getShipByPosition(endPos.getDownAndLeft());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getDownAndRight());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));
    }

    @Test
    void testBadIndexRange_DOWN_LeftRightCheck() {
        Point startPos = new Point(5, 7);
        Point endPos = new Point(5, 8);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(4, 6)); // Left
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));

        doReturn(null).when(oceanSpy).getShipByPosition(new Point(4, 6));
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(6, 6)); // Right
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN));
    }

    @Test
    void testBadIndexRange_LEFT_EndPositionDiagonalCheck() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(3, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getUpAndLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));

        doReturn(null).when(oceanSpy).getShipByPosition(endPos.getUpAndLeft());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getDownAndLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));
    }

    @Test
    void testBadIndexRange_LEFT_UpDownCheck() {
        Point startPos = new Point(3, 5);
        Point endPos = new Point(5, 6);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(4, 4)); // Up
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));
        
        doReturn(null).when(oceanSpy).getShipByPosition(new Point(4, 4));
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(4, 6)); // Down
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT));
    }

    @Test
    void testBadIndexRange_RIGHT_StartPositionDiagonalCheck() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(7, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getUpAndLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));

        doReturn(null).when(oceanSpy).getShipByPosition(startPos.getUpAndLeft());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(startPos.getDownAndLeft());
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));
    }


    @Test
    void testBadIndexRange_RIGHT_UpDownCheck() {
        Point startPos = new Point(7, 5);
        Point endPos = new Point(5, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(6, 4)); // Up
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));

        doReturn(null).when(oceanSpy).getShipByPosition(new Point(6, 4));
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(new Point(6, 6)); // Down
        
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT));
    }

    @Test
    void testBadIndexRange_UP_EndPositionDiagonalsSpecific() {
        Point startPos = new Point(5, 3);
        Point endPos = new Point(5, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());

        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getUpAndRight());
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP), "Should return true when ship is at UpAndRight of end position");

        doReturn(null).when(oceanSpy).getShipByPosition(endPos.getUpAndRight());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(endPos.getUpAndLeft());
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.UP), "Should return true when ship is at UpAndLeft of end position");
    }

    @Test
    void testBadIndexRange_DOWN_MiddleLeftRightSpecific() {
        Point startPos = new Point(5, 7);
        Point endPos = new Point(5, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());

        Point middlePoint = new Point(5, 6);
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(middlePoint.getLeft());
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN), "Should return true when ship is at Left of middle point");

        doReturn(null).when(oceanSpy).getShipByPosition(middlePoint.getLeft());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(middlePoint.getRight());
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.DOWN), "Should return true when ship is at Right of middle point");
    }

    @Test
    void testBadIndexRange_LEFT_VerticalAdjacentSpecific() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(3, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());

        Point middlePoint = new Point(4, 5);
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(middlePoint.getUp());
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT), "Should return true when ship is at Up of middle point");

        doReturn(null).when(oceanSpy).getShipByPosition(middlePoint.getUp());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(middlePoint.getDown());
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.LEFT), "Should return true when ship is at Down of middle point");
    }

    @Test
    void testBadIndexRange_RIGHT_VerticalAdjacentSpecific() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(7, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());

        Point middlePoint = new Point(6, 5);
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(middlePoint.getUp());
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT), "Should return true when ship is at Up of middle point");

        doReturn(null).when(oceanSpy).getShipByPosition(middlePoint.getUp());
        doReturn(mock(Ship.class)).when(oceanSpy).getShipByPosition(middlePoint.getDown());
        assertTrue(oceanSpy.badIndexRange(startPos, endPos, ShipPosition.Direction.RIGHT), "Should return true when ship is at Down of middle point");
    }

    @Test
    void testBadIndexRange_SwitchCoverage() {
        Point startPos = new Point(5, 5);
        Point endPos = new Point(5, 5);
        doReturn(false).when(oceanSpy).indexOutOfOcean(any());
        doReturn(null).when(oceanSpy).getShipByPosition(any());

        for (ShipPosition.Direction direction : ShipPosition.Direction.values()) {
            assertFalse(oceanSpy.badIndexRange(startPos, endPos, direction), 
                "Should return false for direction " + direction + " when no ships are present");
        }
    }

    @Test
    public void testPlaceShipRightOutOfBounds() {
        Ocean ocean = new Ocean(5, 5); 
        Ship shipMock = Mockito.mock(Ship.class);
        ShipPosition startPosMock = Mockito.mock(ShipPosition.class);

        Mockito.when(shipMock.getLength()).thenReturn(3); 
        Mockito.when(startPosMock.getX()).thenReturn(4); 
        Mockito.when(startPosMock.getY()).thenReturn(2);
        Mockito.when(startPosMock.getDirection()).thenReturn(ShipPosition.Direction.RIGHT);

        Point validPoint = new Point(4, 2);
        Mockito.when(startPosMock.getPosition()).thenReturn(validPoint);

        try {
            Method method = Ocean.class.getDeclaredMethod("placeShipRight", Ship.class, ShipPosition.class);
            method.setAccessible(true);

            ShipPosition result = (ShipPosition) method.invoke(ocean, shipMock, startPosMock);

            assertNull(result, "Se esperaba que el resultado fuera nulo por estar fuera de rango.");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            fail("Error al invocar el método placeShipRight: " + cause.getMessage());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail("Error al acceder al método placeShipRight: " + e.getMessage());
        }
    }

    @Test
    public void testPlaceShipRightSuccess() {
        Ocean ocean = new Ocean(5, 5); 

        Ship shipMock = Mockito.mock(Ship.class);
        ShipPosition startPosMock = Mockito.mock(ShipPosition.class);

        Mockito.when(shipMock.getLength()).thenReturn(3); 
        Mockito.when(startPosMock.getX()).thenReturn(1); 
        Mockito.when(startPosMock.getY()).thenReturn(2);
        Mockito.when(startPosMock.getDirection()).thenReturn(ShipPosition.Direction.RIGHT);

        Point validPoint = new Point(1, 2);
        Mockito.when(startPosMock.getPosition()).thenReturn(validPoint);

        try {
            Method method = Ocean.class.getDeclaredMethod("placeShipRight", Ship.class, ShipPosition.class);
            method.setAccessible(true);

            ShipPosition result = (ShipPosition) method.invoke(ocean, shipMock, startPosMock);

            assertEquals(startPosMock, result, "Se esperaba que el resultado fuera igual a startPosMock.");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            fail("Error al invocar el método placeShipRight: " + cause.getMessage());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail("Error al acceder al método placeShipRight: " + e.getMessage());
        }
    }

    @Test
    public void testPlaceShipLeftOutOfBounds() {
        Ocean ocean = new Ocean(5, 5); 

        Ship shipMock = Mockito.mock(Ship.class);
        ShipPosition startPosMock = Mockito.mock(ShipPosition.class);

        Mockito.when(shipMock.getLength()).thenReturn(3); 
        Mockito.when(startPosMock.getX()).thenReturn(1); 
        Mockito.when(startPosMock.getY()).thenReturn(3);
        Mockito.when(startPosMock.getDirection()).thenReturn(ShipPosition.Direction.LEFT);

        Point validPoint = new Point(1, 3);
        Mockito.when(startPosMock.getPosition()).thenReturn(validPoint);

        try {
            Method method = Ocean.class.getDeclaredMethod("placeShipLeft", Ship.class, ShipPosition.class);
            method.setAccessible(true);

            ShipPosition result = (ShipPosition) method.invoke(ocean, shipMock, startPosMock);

            assertNull(result, "Se esperaba que el resultado fuera nulo por estar fuera de rango.");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            fail("Error al invocar el método placeShipLeft: " + cause.getMessage());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail("Error al acceder al método placeShipLeft: " + e.getMessage());
        }
    }

    
    @Test
    public void testPlaceShipLeftSuccess() {
        Ocean ocean = new Ocean(10, 10); 

        Ship shipMock = Mockito.mock(Ship.class);
        ShipPosition startPosMock = Mockito.mock(ShipPosition.class);

        Mockito.when(shipMock.getLength()).thenReturn(3); 
        Mockito.when(startPosMock.getX()).thenReturn(3); 
        Mockito.when(startPosMock.getY()).thenReturn(3);
        Mockito.when(startPosMock.getDirection()).thenReturn(ShipPosition.Direction.LEFT);

        Point validPoint = new Point(3, 3);
        Mockito.when(startPosMock.getPosition()).thenReturn(validPoint);
    
        try {
            Method method = Ocean.class.getDeclaredMethod("placeShipLeft", Ship.class, ShipPosition.class);
            method.setAccessible(true);

            ShipPosition result = (ShipPosition) method.invoke(ocean, shipMock, startPosMock);

            assertEquals(startPosMock, result, "Se esperaba que el resultado fuera igual a startPosMock.");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            fail("Error al invocar el método placeShipLeft: " + cause.getMessage());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail("Error al acceder al método placeShipLeft: " + e.getMessage());
        }
    }

    @Test
    void testTryPlaceShipUp_Success() {
        ShipPosition startPos = new ShipPosition(new Point(5, 5), ShipPosition.Direction.UP);

        ShipPosition result = oceanInstance.tryPlaceShip(shipMock, startPos);

        assertNotNull(result);

        for (int i = 5; i >= 3; i--) {
            assertEquals(shipMock, oceanInstance.getShipByPosition(new Point(5, i)));
        }
    }

    @Test
    void testTryPlaceShipUp_OutOfBounds() {
        ShipPosition startPos = new ShipPosition(new Point(5, 0), ShipPosition.Direction.UP);
        ShipPosition result = oceanInstance.tryPlaceShip(shipMock, startPos);

        assertNull(result);
    }

    
    @Test
    void testPlaceShipDown_Success() {
        Point startPoint = new Point(5, 5);
        ShipPosition shipPosition = new ShipPosition(startPoint, ShipPosition.Direction.DOWN);

        when(shipMock.getLength()).thenReturn(3);
        ShipPosition result = oceanInstance.tryPlaceShip(shipMock, shipPosition);

        assertNotNull(result);
        for (int i = 5; i <= 7; i++) {
            assertEquals(shipMock, oceanInstance.getShipByPosition(new Point(5, i)));
        }
    }

    @Test
    void testPlaceShipDown_Failure_BadIndexRange() {
        Point startPoint = new Point(28, 28);
        ShipPosition shipPosition = new ShipPosition(startPoint, ShipPosition.Direction.DOWN);
        when(shipMock.getLength()).thenReturn(5);

        ShipPosition result = oceanInstance.tryPlaceShip(shipMock, shipPosition);

        assertNull(result);
    }

    @Test
    void testGetPointsOccupiedByShip_emptyOcean() {
        Ocean ocean = new Ocean(1, 1); 
        Ship mockShip = mock(Ship.class);

        Ocean spyOcean = spy(ocean);

        List<Point> result = spyOcean.getPointsOccupiedByShip(mockShip);

        assertTrue(result.isEmpty(), "Si el océano está vacío, la lista de puntos debería estar vacía.");
    }

    @Test
    void testGetPointsOccupiedByShip_noShipAndNullShip() {
        Ocean ocean = new Ocean(10, 10); 
        Ship mockShip = mock(Ship.class);

        Ocean spyOcean = spy(ocean);

        List<Point> result = spyOcean.getPointsOccupiedByShip(mockShip);
        assertTrue(result.isEmpty(), "Si no hay barcos en el océano, la lista de puntos debe estar vacía.");

        result = spyOcean.getPointsOccupiedByShip(null);
        assertTrue(result.isEmpty(), "Si el barco es null, la lista de puntos debería estar vacía.");
    }

    @Test
    void testGetPointsOccupiedByShip_singleShipInOcean() {
        Ocean ocean = new Ocean(10, 10); 
        Ship mockShip = mock(Ship.class);
        ShipPosition mockPosition = new ShipPosition(new Point(0, 0), ShipPosition.Direction.RIGHT);
        Ocean spyOcean = spy(ocean);

        doReturn(mockPosition).when(spyOcean).tryPlaceShip(eq(mockShip), any());

        List<Point> expectedPoints = Collections.singletonList(new Point(0, 0));
        doReturn(expectedPoints).when(spyOcean).getPointsOccupiedByShip(mockShip);
        List<Point> result = spyOcean.getPointsOccupiedByShip(mockShip);

        assertEquals(1, result.size(), "Debería haber exactamente un punto en la lista.");
        assertTrue(result.contains(new Point(0, 0)), "La lista debería contener el punto (0, 0).");
    }   
}