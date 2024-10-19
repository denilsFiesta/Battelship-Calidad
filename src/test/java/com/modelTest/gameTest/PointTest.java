package com.modelTest.gameTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.model.game.ocean.Point;
import com.model.game.ocean.ShipPosition;
import java.lang.reflect.Method;

public class PointTest {
    
    @Test
    public void testGetRangeUp() {
        Point startPos = new Point(2, 2);
        Point endPos = new Point(2, 5);
        ShipPosition.Direction direction = ShipPosition.Direction.UP;
        Point[] result = Point.getRange(startPos, endPos, direction);

        Point[] expected = { new Point(2, 2), new Point(2, 3), new Point(2, 4), new Point(2, 5) };
        assertArrayEquals(expected, result, "El rango hacia arriba no es el esperado.");
    }

    @Test
    public void testGetRangeDown() {
        Point startPos = new Point(2, 5);
        Point endPos = new Point(2, 2);
        ShipPosition.Direction direction = ShipPosition.Direction.DOWN;
        Point[] result = Point.getRange(startPos, endPos, direction);

        Point[] expected = { new Point(2, 5), new Point(2, 4), new Point(2, 3), new Point(2, 2) };
        assertArrayEquals(expected, result, "El rango hacia abajo no es el esperado.");
    }

    @Test
    public void testGetRangeLeft() {
        Point startPos = new Point(5, 2);
        Point endPos = new Point(2, 2);
        ShipPosition.Direction direction = ShipPosition.Direction.LEFT;
        Point[] result = Point.getRange(startPos, endPos, direction);

        Point[] expected = { new Point(5, 2), new Point(4, 2), new Point(3, 2), new Point(2, 2) };
        assertArrayEquals(expected, result, "El rango hacia la izquierda no es el esperado.");
    }

    @Test
    public void testGetRangeRight() {
        Point startPos = new Point(2, 2);
        Point endPos = new Point(5, 2);
        ShipPosition.Direction direction = ShipPosition.Direction.RIGHT;
        Point[] result = Point.getRange(startPos, endPos, direction);

        Point[] expected = { new Point(2, 2), new Point(3, 2), new Point(4, 2), new Point(5, 2) };
        assertArrayEquals(expected, result, "El rango hacia la derecha no es el esperado.");
    }

    @Test
    public void testGetRangeDiagonal() {
        Point startPos = new Point(2, 2);
        Point endPos = new Point(4, 4); 
        ShipPosition.Direction direction = ShipPosition.Direction.UP;

        Point[] result = Point.getRange(startPos, endPos, direction);

        Point[] expected = { new Point(2, 2), new Point(2, 3), new Point(2, 4) }; 
        assertArrayEquals(expected, result, "El rango diagonal no es el esperado.");
    }

    @Test
    public void testGetLength() throws Exception {
        Point startPos = new Point(1, 1);
        Point endPos = new Point(4, 5);

        Method getLengthMethod = Point.class.getDeclaredMethod("getLength", Point.class, Point.class);
        getLengthMethod.setAccessible(true); 
        
        int length = (int) getLengthMethod.invoke(null, startPos, endPos);

        int expectedLength = (int) Math.sqrt(Math.pow(startPos.x() - endPos.x(), 2) + Math.pow(startPos.y() - endPos.y(), 2)) + 1;
        assertEquals(expectedLength, length, "La longitud calculada no es la esperada.");
    }

    

    @Test
    public void testGetLeft() {
        Point initialPoint = new Point(2, 3);
        Point resultPoint = initialPoint.getLeft();

        Point expectedPoint = new Point(1, 3); 
        assertEquals(expectedPoint, resultPoint, "El punto devuelto no es el esperado.");
    }

    @Test
    public void testGetRight() {
        Point initialPoint = new Point(2, 3);
        Point resultPoint = initialPoint.getRight();

        Point expectedPoint = new Point(3, 3); 
        assertEquals(expectedPoint, resultPoint, "El punto devuelto no es el esperado.");
    }

    @Test
    public void testGetUp() {
        Point initialPoint = new Point(2, 3);
        Point resultPoint = initialPoint.getUp();

        Point expectedPoint = new Point(2, 4); 
        assertEquals(expectedPoint, resultPoint, "El punto devuelto no es el esperado.");
    }

    @Test
    public void testGetDown() {
        Point initialPoint = new Point(2, 3);

        Point resultPoint = initialPoint.getDown();

        Point expectedPoint = new Point(2, 4);
        assertEquals(expectedPoint, resultPoint, "El punto devuelto no es el esperado.");
    }

    @Test
    public void testGetUpAndRight() {
        Point initialPoint = new Point(2, 3);
        Point resultPoint = initialPoint.getUpAndRight();

        Point expectedPoint = new Point(3, 4); 
        assertEquals(expectedPoint, resultPoint, "El punto devuelto no es el esperado.");
    }

    @Test
    public void testGetDownAndRight() {
        Point initialPoint = new Point(2, 3);
        Point resultPoint = initialPoint.getDownAndRight();

        Point expectedPoint = new Point(3, 2); 
        assertEquals(expectedPoint, resultPoint, "El punto devuelto no es el esperado.");
    }

    @Test
    public void testGetUpAndLeft() {
        Point initialPoint = new Point(2, 3);
        Point resultPoint = initialPoint.getUpAndLeft();

        Point expectedPoint = new Point(1, 2); 
        assertEquals(expectedPoint, resultPoint, "El punto devuelto no es el esperado.");
    }

    @Test
    public void testGetDownAndLeft() {
        Point initialPoint = new Point(2, 3);
        Point resultPoint = initialPoint.getDownAndLeft();

        Point expectedPoint = new Point(1, 4); 
        assertEquals(expectedPoint, resultPoint, "El punto devuelto no es el esperado.");
    }

    @Test
    void testEquals_SameObject() {
        Point point = new Point(1, 2);
        assertTrue(point.equals(point), "Un punto debe ser igual a sí mismo");
    }

    @Test
    void testEquals_NullOrDifferentClass() {
        Point point = new Point(1, 2);
        assertFalse(point.equals(null), "Un punto no debe ser igual a null");
        assertFalse(point.equals("Not a Point"), "Un punto no debe ser igual a un objeto de otra clase");
    }

    @Test
    void testEquals_SameCoordinates() {
        Point point1 = new Point(1, 2);
        Point point2 = new Point(1, 2);

        assertTrue(point1.equals(point2), "Puntos con las mismas coordenadas deben ser iguales");
    }

    @Test
    void testEquals_DifferentXCoordinate() {
        Point point1 = new Point(1, 2);
        Point point2 = new Point(2, 2);

        assertFalse(point1.equals(point2), "Puntos con diferentes coordenadas X no deben ser iguales");
    }

    @Test
    void testEquals_DifferentYCoordinate() {
        Point point1 = new Point(1, 2);
        Point point2 = new Point(1, 3);

        assertFalse(point1.equals(point2), "Puntos con diferentes coordenadas Y no deben ser iguales");
    }

    @Test
    void testHashCode() {
        Point point1 = new Point(1, 2);
        Point point2 = new Point(1, 2);
        Point point3 = new Point(2, 1);

        assertEquals(point1.hashCode(), point2.hashCode(), "Puntos iguales deben tener el mismo hashCode");
        assertNotEquals(point1.hashCode(), point3.hashCode(), "Puntos diferentes deben tener hashCodes diferentes");
    }
}
